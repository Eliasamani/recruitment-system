package se.kth.iv1201.recruitment.controller;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se.kth.iv1201.recruitment.model.RegisterForm;
import se.kth.iv1201.recruitment.service.UserService;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;

/**
 * Controller for user-related API operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Handles user registration.
     *
     * @param registerForm Request body containing user registration details.
     * @return ResponseEntity with success message or error if the username already
     *         exists.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterForm registerForm) {
        LOGGER.info("Received registration request for username: " + registerForm.getUsername());

        userService.createPerson(
                registerForm.getFirstname(),
                registerForm.getLastname(),
                registerForm.getPersonNumber(),
                registerForm.getEmail(),
                registerForm.getUsername(),
                passwordEncoder.encode(registerForm.getPassword()));
        LOGGER.info("User registered successfully: " + registerForm.getUsername());
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));

    }
}
