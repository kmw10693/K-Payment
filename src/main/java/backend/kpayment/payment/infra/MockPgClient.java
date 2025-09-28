package backend.kpayment.payment.infra;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MockPgClient {

    private final Random random = new Random();

    /**
     * 가짜 결제 승인 메소드
     * @return true = 결제 성공, false = 결제 실패
     */
    public boolean approvePayment(String orderId, int amount) {
        // 70% 확률로 성공, 30% 확률로 실패
        return random.nextInt(10) < 7;
    }
}

