package se.kth.iv1201.recruitment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.security.JwtProvider;
import se.kth.iv1201.recruitment.service.SessionService;
import se.kth.iv1201.recruitment.service.UserService;

/**
 * Handles authentication-related operations (login, logout, session
 * validation).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final SessionService sessionService;

    public AuthController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * the post mapping for login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> json, HttpServletResponse response) {
        Cookie jwtCookie = sessionService.login(json);
        response.addCookie(jwtCookie);
        return ResponseEntity.ok(Map.of("message", "Logged in successfully"));
    }

    /**
     * the get method for session
     */
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Map<String, Object> userData = null;
        if (cookies != null) {
            userData = sessionService.checkSession(cookies);
        } else {
            LOGGER.warning("Session validation failed - No token found");
            return ResponseEntity.status(400).body(Map.of("error", "Not authenticated"));
        }
        return ResponseEntity.ok(userData);
    }

    /**
     * Logs out the user by clearing the JWT from the cookies.
     * 
     * @param response The HTTP response object
     * @return HTTP response with status 200 (OK) and a JSON object with key
     *         "message" and value "Logged out successfully"
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(sessionService.removeCookie());
        LOGGER.info("User logged out successfully");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * Handles protected API endpoint access. Returns a success message if the user
     * is authenticated, otherwise a 403 error is returned.
     * 
     * Mostly meant for testing purposes.
     * 
     * @return Success message if authenticated, otherwise a 403 error message.
     */
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.warning("Unauthorized access attempt to protected resource");
            return ResponseEntity.status(403).body(Map.of("error", "Forbidden: You are not authenticated!"));
        }

        LOGGER.info("Protected resource accessed by user: " + authentication.getName());
        return ResponseEntity
                .ok(Map.of("message", "Hello " + authentication.getName() + ", you accessed a protected resource!"));
    }
}