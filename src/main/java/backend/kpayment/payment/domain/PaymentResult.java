package backend.kpayment.payment.domain;

public record PaymentResult(
        String orderId,
        PaymentStatus status,
        String message
) {}
