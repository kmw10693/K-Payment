package backend.kpayment.payment.messaging;

import backend.kpayment.payment.event.PaymentCompletedEvent;
import backend.kpayment.payment.event.PaymentFailedEvent;
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
}

