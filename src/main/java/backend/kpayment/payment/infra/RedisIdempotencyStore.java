package backend.kpayment.payment.infra;

import backend.kpayment.payment.domain.PaymentStatus;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisIdempotencyStore {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisIdempotencyStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public PaymentStatus getStatus(String key) {
        String status = redisTemplate.opsForValue().get(key);
        return status == null ? null : PaymentStatus.valueOf(status);
    }

    public void saveStatus(String key, PaymentStatus status) {
        redisTemplate.opsForValue().set(key, status.name());
    }
}
