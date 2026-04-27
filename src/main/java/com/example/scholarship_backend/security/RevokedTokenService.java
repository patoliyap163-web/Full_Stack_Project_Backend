package com.example.scholarship_backend.security;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RevokedTokenService {

    private final Map<String, Date> revokedTokens = new ConcurrentHashMap<>();

    public void revokeToken(String token, Date expiration) {
        cleanupExpiredTokens();
        revokedTokens.put(token, expiration);
    }

    public boolean isTokenRevoked(String token) {
        cleanupExpiredTokens();
        return revokedTokens.containsKey(token);
    }

    private void cleanupExpiredTokens() {
        Date now = new Date();
        revokedTokens.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue().before(now));
    }
}
