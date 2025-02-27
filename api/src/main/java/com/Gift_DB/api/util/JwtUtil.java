package com.Gift_DB.api.util;

import java.util.Map;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Environment env;
    private final String secret;
    private final long expirationTimeInMinutes;

    public JwtUtil(Environment env) {
        this.env = env;
        this.secret = Objects.requireNonNull(env.getProperty("jwt.secret"));
        this.expirationTimeInMinutes = Long.parseLong(Objects.requireNonNull(env.getProperty("jwt.expirationTimeInMinutes")));
    }

    // Generate JWT using environment variable JWT secret
    public String generateJwt(Map<String, Object> payload) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withPayload(payload)
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expirationTimeInMinutes)))
                .sign(algorithm);
    }

    public int validateJwt(String sessionToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .acceptLeeway(1)
                    .build();

            DecodedJWT jwt = verifier.verify(sessionToken);
            return jwt.getClaim("id").asInt();

        } catch (JWTVerificationException e) {
            return -1;
        }
    }

}
