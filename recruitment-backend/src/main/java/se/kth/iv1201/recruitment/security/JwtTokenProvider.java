package se.kth.iv1201.recruitment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private String secret;

    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public JwtTokenProvider() {
        if (System.getenv("DATABASE_URL").length() >42) {
            this.secret = System.getenv("DATABASE_URL").substring(10,42);
        }
        else {
            this.secret = System.getenv("DATABASE_URL");
        }
    }

    /**
     * Generates a JWT token for the specified username.
     * 
     * @param username the username for which the token is to be generated
     * @return a JWT token as a String
     */

    public String generateToken(String username) {


        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Validates a JWT token, returning the subject (i.e. the username) if the token is valid, or null if the token is invalid.
     * 
     * @param token the JWT token to validate
     * @return the subject of the token if valid, or null if invalid
     */
    public String validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            JwtParser parser = Jwts.parser().verifyWith(key).build();
            return parser.parseSignedClaims(token).getPayload().getSubject();
        } catch (JwtException e) {
            return null;
        }
    }
}
