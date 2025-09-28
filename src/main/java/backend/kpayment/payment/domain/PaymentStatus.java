package backend.kpayment.payment.domain;

public enum PaymentStatus {
    PENDING,     // 요청 접수(옵션)
    PROCESSING,  // PG 승인 요청 중
    SUCCESS,     // 결제 승인
    FAILED       // 결제 실패
}