package backend.kpayment.payment.service;

import backend.kpayment.payment.infra.MockPgClient;
import backend.kpayment.payment.messaging.PaymentEventPublisher;
import backend.kpayment.refundment.event.RefundCompletedEvent;
import backend.kpayment.refundment.event.RefundFailedEvent;
import backend.kpayment.refundment.event.RefundRequestedEvent;
import org.springframework.stereotype.Service;

@Service
public class RefundProcessingService {

    private final MockPgClient pgClient;
    private final PaymentEventPublisher publisher;

    public RefundProcessingService(MockPgClient pgClient, PaymentEventPublisher publisher) {
        this.pgClient = pgClient;
        this.publisher = publisher;
    }

    public void handleRefundRequest(RefundRequestedEvent event) {
        boolean refunded = pgClient.approvePayment(event.orderId(), event.amount());

        if (refunded) {
            publisher.publishRefundCompleted(new RefundCompletedEvent(event.orderId(), event.idempotencyKey()));
        } else {
            publisher.publishRefundFailed(new RefundFailedEvent(event.orderId(), event.idempotencyKey(), "PG 환불 실패"));
        }
    }
}

