package backend.kpayment.refundment.controller;

import backend.kpayment.refundment.service.RefundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    public ResponseEntity<String> requestRefund(
            @RequestParam String orderId,
            @RequestParam int amount,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        String key = (idempotencyKey != null && !idempotencyKey.isBlank())
                ? idempotencyKey
                : UUID.randomUUID().toString();

        refundService.requestRefund(orderId, amount, key);
        return ResponseEntity.ok("RefundRequestedEvent published with key=" + key);
    }
}
