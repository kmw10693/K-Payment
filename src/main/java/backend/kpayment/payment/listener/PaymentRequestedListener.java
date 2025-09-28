package backend.kpayment.payment.listener;

import backend.kpayment.payment.event.PaymentRequestedEvent;
import backend.kpayment.payment.service.PaymentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestedListener {

    private final PaymentService paymentService;

    public PaymentRequestedListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @RabbitListener(queues = "payment.requested.queue")
    public void onPaymentRequested(PaymentRequestedEvent event) {
        paymentService.handlePaymentRequest(event);
    }
}
