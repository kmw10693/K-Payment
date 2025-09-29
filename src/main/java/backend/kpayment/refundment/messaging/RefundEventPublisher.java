import backend.kpayment.refundment.event.RefundRequestedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RefundEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RefundEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRequested(RefundRequestedEvent event) {
        rabbitTemplate.convertAndSend("refund.exchange", "refund.requested", event);
    }
}
