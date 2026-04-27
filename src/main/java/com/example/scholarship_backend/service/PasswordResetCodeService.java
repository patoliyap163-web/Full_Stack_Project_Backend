package com.example.scholarship_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordResetCodeService {

    private final Map<String, ResetCodeEntry> resetCodes = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${password.reset.code-expiration-ms:300000}")
    private long codeExpirationMs;

    public String generateCodeForEmail(String email) {
        cleanupExpiredCodes();
        String code = String.format("%04d", secureRandom.nextInt(10000));
        Instant expiresAt = Instant.now().plusMillis(codeExpirationMs);
        resetCodes.put(email.toLowerCase(), new ResetCodeEntry(code, expiresAt));
        return code;
    }

    public ResetCodeValidationResult validateCode(String email, String code) {
        cleanupExpiredCodes();

        ResetCodeEntry entry = resetCodes.get(email.toLowerCase());
        if (entry == null) {
            return ResetCodeValidationResult.INVALID;
        }

        if (Instant.now().isAfter(entry.expiresAt())) {
            resetCodes.remove(email.toLowerCase());
            return ResetCodeValidationResult.EXPIRED;
        }

        if (!entry.code().equals(code)) {
            return ResetCodeValidationResult.INVALID;
        }

        return ResetCodeValidationResult.VALID;
    }

    public void invalidateCode(String email) {
        resetCodes.remove(email.toLowerCase());
    }

    private void cleanupExpiredCodes() {
        Instant now = Instant.now();
        resetCodes.entrySet().removeIf(entry -> now.isAfter(entry.getValue().expiresAt()));
    }

    private record ResetCodeEntry(String code, Instant expiresAt) {
    }

    public enum ResetCodeValidationResult {
        VALID,
        INVALID,
        EXPIRED
    }
}
