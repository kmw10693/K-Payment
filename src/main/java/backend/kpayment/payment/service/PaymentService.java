package backend.kpayment.payment.service;

import backend.kpayment.payment.domain.PaymentStatus;
import backend.kpayment.payment.event.PaymentCompletedEvent;
import backend.kpayment.payment.event.PaymentFailedEvent;
import backend.kpayment.payment.event.PaymentRequestedEvent;
import backend.kpayment.payment.infra.MockPgClient;
import backend.kpayment.payment.infra.RedisIdempotencyStore;
import backend.kpayment.payment.messaging.PaymentEventPublisher;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private final RedisIdempotencyStore redisStore;
    private final MockPgClient pgClient;
    private final PaymentEventPublisher publisher;
    private final RedissonClient redissonClient;

    public PaymentService(RedisIdempotencyStore redisStore,
                          MockPgClient pgClient,
                          PaymentEventPublisher publisher,
                          RedissonClient redissonClient) {
        this.redisStore = redisStore;
        this.pgClient = pgClient;
        this.publisher = publisher;
        this.redissonClient = redissonClient;
    }

    public void handlePaymentRequest(PaymentRequestedEvent event) {
        String key = "lock:payment:" + event.idempotencyKey();
        RLock lock = redissonClient.getLock(key);

        try {
            // 3초 동안 락 획득 대기, 락은 10초 후 자동 만료
            if (lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                PaymentStatus existing = redisStore.getStatus(event.idempotencyKey());
                if (existing != null) {
                    // 이미 처리된 요청
                    if (existing == PaymentStatus.SUCCESS) {
                        publisher.publishCompleted(new PaymentCompletedEvent(event.orderId(), event.idempotencyKey()));
                    } else if (existing == PaymentStatus.FAILED) {
                        publisher.publishFailed(new PaymentFailedEvent(event.orderId(), event.idempotencyKey(), "Cached Failure"));
                    }
                    return;
                }

                // 신규 요청 처리
                redisStore.saveStatus(event.idempotencyKey(), PaymentStatus.PROCESSING);
                boolean approved = pgClient.approvePayment(event.orderId(), event.amount());

                if (approved) {
                    redisStore.saveStatus(event.idempotencyKey(), PaymentStatus.SUCCESS);
                    publisher.publishCompleted(new PaymentCompletedEvent(event.orderId(), event.idempotencyKey()));
                } else {
                    redisStore.saveStatus(event.idempotencyKey(), PaymentStatus.FAILED);
                    publisher.publishFailed(new PaymentFailedEvent(event.orderId(), event.idempotencyKey(), "PG Approval Failed"));
                }
            } else {
                throw new RuntimeException("결제 요청이 동시에 처리 중입니다. 잠시 후 다시 시도하세요.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("락 획득 실패", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
