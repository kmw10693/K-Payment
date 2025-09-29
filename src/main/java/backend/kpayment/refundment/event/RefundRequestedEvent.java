package backend.kpayment.refundment.event;

import java.io.Serializable;

public record RefundRequestedEvent(String orderId, int amount, String idempotencyKey) implements Serializable {}
