package backend.kpayment.payment.event;

import java.io.Serializable;

/**
 * 결제 실패 이벤트
 *
 * - OrderService → PaymentService 로 전달되는 이벤트
 * - 유저가 특정 주문(orderId)에 대해 결제를 시도할 때 발행됨
 * - 메시지 브로커(RabbitMQ 등)를 통해 비동기로 전달
 */
public record PaymentFailedEvent(String orderId, String idempotencyKey, String reason) implements Serializable {}

