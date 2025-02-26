package com.Gift_DB.api.util;

import java.util.Map;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Environment env;

    public JwtUtil(Environment env) {
        this.env = env;
    }

    public String generateJwt(Map<String, Object> payload) {
        String secret = Objects.requireNonNull(env.getProperty("jwt.secret"));
        long expirationTimeInMinutes = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.expirationTimeInMinutes")));

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withPayload(payload)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expirationTimeInMinutes)))
                .sign(algorithm);
    }

}
