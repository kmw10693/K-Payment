package backend.kpayment.payment.messaging;

import backend.kpayment.payment.event.PaymentCompletedEvent;
import backend.kpayment.payment.event.PaymentFailedEvent;
import backend.kpayment.refundment.event.RefundCompletedEvent;
import backend.kpayment.refundment.event.RefundFailedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public PaymentEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishCompleted(PaymentCompletedEvent event) {
        rabbitTemplate.convertAndSend("payment.exchange", "payment.completed", event);
    }

    public void publishFailed(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend("payment.exchange", "payment.failed", event);
    }

    public void publishRefundCompleted(RefundCompletedEvent event) {
        rabbitTemplate.convertAndSend("refund.exchange", "refund.completed", event);
    }

    public void publishRefundFailed(RefundFailedEvent event) {
        rabbitTemplate.convertAndSend("refund.exchange", "refund.failed", event);
    }
}

