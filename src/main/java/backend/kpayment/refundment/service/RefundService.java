package backend.kpayment.refundment.service;

import backend.kpayment.refundment.event.RefundRequestedEvent;
import backend.kpayment.refundment.messaging.RefundEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefundService {

    private final RefundEventPublisher publisher;

    public RefundService(RefundEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void requestRefund(String orderId, int amount, String idempotencyKey) {
        String key = (idempotencyKey != null && !idempotencyKey.isBlank())
                ? idempotencyKey : UUID.randomUUID().toString();

        publisher.publishRequested(new RefundRequestedEvent(orderId, amount, key));
    }
}
