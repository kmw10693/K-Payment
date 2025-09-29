package backend.kpayment.refundment.event;

import java.io.Serializable;

public record RefundCompletedEvent(String orderId, String idempotencyKey) implements Serializable {
}
