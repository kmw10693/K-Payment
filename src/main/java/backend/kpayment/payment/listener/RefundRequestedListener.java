package backend.kpayment.payment.listener;

import backend.kpayment.payment.service.RefundProcessingService;
import backend.kpayment.refundment.event.RefundRequestedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RefundRequestedListener {

    private final RefundProcessingService refundProcessingService;

    public RefundRequestedListener(RefundProcessingService refundProcessingService) {
        this.refundProcessingService = refundProcessingService;
    }

    @RabbitListener(queues = "refund.requested.queue")
    public void onRefundRequested(RefundRequestedEvent event) {
        refundProcessingService.handleRefundRequest(event);
    }
}
