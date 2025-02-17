package se.kth.iv1201.recruitment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.security.JwtProvider;
import se.kth.iv1201.recruitment.service.UserService;

/**
 * Handles authentication-related operations (login, logout, session
 * validation).
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(
            AuthController.class.getName());

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            JwtProvider jwtProvider,
            UserService userService,
            PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles login requests.
     *
     * @param json     JSON object with keys "username" and "password"
     * @param response HTTP response object
     * @return HTTP response with status 200 (OK) if successful, 401 (Unauthorized)
     *         if not
     *         Response body is a JSON object with key "message" and value "Login
     *         successful" if successful,
     *         or a JSON object with key "error" and value "User does not exist" or
     *         "Invalid password" if not
     *         The response will also include a secure HTTP-only cookie with the JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> json,
            HttpServletResponse response) {
        String username = json.get("username");
        LOGGER.info("Login attempt for username: " + username);

        try {
            PersonDTO person = userService.findPerson(username);
            if (person == null) {
                LOGGER.warning("Login failed - User does not exist: " + username);
                return ResponseEntity
                        .status(401)
                        .body(Map.of("error", "User does not exist"));
            }

            if (!passwordEncoder.matches(json.get("password"), person.getPassword())) {
                LOGGER.warning("Login failed - Invalid password for user: " + username);
                return ResponseEntity
                        .status(401)
                        .body(Map.of("error", "Invalid password"));
            }

            // Generate JWT
            String jwt = jwtProvider.generateToken(person.getUsername());

            // Create Secure HTTP-Only Cookie
            Cookie jwtCookie = new Cookie("jwt", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration

            response.addCookie(jwtCookie);
            LOGGER.info("Login successful for user: " + username);
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        } catch (Exception e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Unexpected error during login for user: " + username,
                    e);
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of("error", "An internal error occurred. Please try again later."));
        }
    }

    /**
     * Checks if the user is authenticated by verifying the presence of a valid JWT
     * in the request.
     * If the user is authenticated, the response body will contain a JSON object
     * with key "username" and value
     * equal to the username of the authenticated user.
     * If the user is not authenticated, the response will have a status of 401
     * Unauthorized, and the response body
     * will contain a JSON object with key "error" and value "Not authenticated".
     * 
     * @param request The incoming request
     * @return HTTP response with the result of the authentication check
     */
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || jwtProvider.validateToken(token) == null) {
            LOGGER.warning("Session validation failed - No valid token found");
            return ResponseEntity
                    .status(401)
                    .body(Map.of("error", "Not authenticated"));
        }

        String username = jwtProvider.validateToken(token);
        LOGGER.info("Session validated for user: " + username);
        return ResponseEntity.ok(Map.of("username", username));
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
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);
        LOGGER.info("User logged out successfully");
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * Handles protected API endpoint access. Returns a success message if the user
     * is authenticated, otherwise a 403
     * error is returned.
     *
     * Mostly meant for testing purposes.
     *
     * @return Success message if authenticated, otherwise a 403 error message.
     */
    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.warning("Unauthorized access attempt to protected resource");
            return ResponseEntity
                    .status(403)
                    .body(Map.of("error", "Forbidden: You are not authenticated!"));
        }

        LOGGER.info(
                "Protected resource accessed by user: " + authentication.getName());
        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Hello " +
                                authentication.getName() +
                                ", you accessed a protected resource!"));
    }
}
