package se.kth.iv1201.recruitment.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import se.kth.iv1201.recruitment.model.exception.InvalidSessionException;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.security.JwtProvider;

@Service
public class SessionService {

    private static final Logger LOGGER = Logger.getLogger(SessionService.class.getName());
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public SessionService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, Object> checkSession(Cookie[] cookies) {
        String token = null;
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) {
            throw new InvalidSessionException("Session validation failed - No token found");
        }

        String username = jwtProvider.validateToken(token);
        PersonDTO person = userService.findPerson(username);
        if (person == null) {
            throw new InvalidSessionException("Session validation failed - User not found: " + username);
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", person.getId());
        userData.put("username", person.getUsername());
        userData.put("firstName", person.getFirstname() != null ? person.getFirstname() : "");
        userData.put("lastName", person.getLastname() != null ? person.getLastname() : "");
        userData.put("personNumber", person.getPersonNumber() != null ? person.getPersonNumber() : "");
        userData.put("email", person.getEmail() != null ? person.getEmail() : "");
        userData.put("role", person.getRole().ordinal());

        LOGGER.info("Session validated for user: " + username + " with role: " + person.getRole());
        return userData;
    }

    public Cookie login(Map<String, String> json) {
        String username = json.get("username");
        LOGGER.info("Login attempt for username: " + username);

        PersonDTO person = userService.findPerson(username);
        if (person == null || (!passwordEncoder.matches(json.get("password"), person.getPassword()))) {
            LOGGER.warning("Login failed for " + username);
            throw new BadCredentialsException("Invalid username or password");
        }

        // Generate JWT
        String jwt = jwtProvider.generateToken(person.getUsername());

        // Create Secure HTTP-Only Cookie
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration

        LOGGER.info("Login successful for user: " + username);
        return jwtCookie;
    }

    public Cookie removeCookie() {
        Cookie jwtCookie = new Cookie("jwt", "");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        return jwtCookie;
    }
}
