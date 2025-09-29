package backend.kpayment.refundment.event;

import java.io.Serializable;

public record RefundFailedEvent(String orderId, String idempotencyKey, String reason) implements Serializable {}
