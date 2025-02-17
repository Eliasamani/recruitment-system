package se.kth.iv1201.recruitment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final Logger LOGGER = Logger.getLogger(
            JwtProvider.class.getName());

    private String secret;

    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public JwtProvider() {
        if (System.getenv("DATABASE_URL").length() > 42) {
            this.secret = System.getenv("DATABASE_URL").substring(10, 42);
        } else {
            this.secret = System.getenv("DATABASE_URL");
        }
    }

    /**
     * Generates a JWT for the specified username.
     *
     * @param username the username for which the token is to be generated
     * @return a JWT as a String
     */

    public String generateToken(String username) {
        LOGGER.info("Generating JWT for user: " + username);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts
                .builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        LOGGER.info("JWT successfully generated for user: " + username);
        return token;
    }

    /**
     * Validates a JWT, returning the subject (i.e. the username) if the token is
     * valid, or null if the token is invalid.
     *
     * @param token the JWT to validate
     * @return the subject of the token if valid, or null if invalid
     */
    public String validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(
                    secret.getBytes(StandardCharsets.UTF_8));
            JwtParser parser = Jwts.parser().verifyWith(key).build();
            String username = parser
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            LOGGER.info("JWT successfully validated for user: " + username);
            return username;
        } catch (JwtException e) {
            LOGGER.log(Level.WARNING, "Invalid JWT: " + e.getMessage());
            return null;
        }
    }
}
