package se.kth.iv1201.recruitment.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import se.kth.iv1201.recruitment.model.RegisterForm;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.service.UserService;

/**
 * Controller for user-related API operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

  private static final Logger LOGGER = Logger.getLogger(
    UserController.class.getName()
  );

  private final PersonRepository repository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public UserController(
    PersonRepository repository,
    UserService userService,
    PasswordEncoder passwordEncoder
  ) {
    this.repository = repository;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Temporary test endpoint to return all users (for testing purposes).
   * @return List of all persons in the database.
   */
  @GetMapping("/mytest")
  public ResponseEntity<List<Person>> test() {
    LOGGER.info("Fetching all users from database");
    try {
      List<Person> users = repository.findAll();
      LOGGER.info("Successfully retrieved users");
      return ResponseEntity.ok(users);
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error fetching users", e);
      return ResponseEntity.status(500).build();
    }
  }

  /**
   * Handles user registration.
   * @param registerForm Request body containing user registration details.
   * @return ResponseEntity with success message or error if the username already exists.
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(
    @Valid @RequestBody RegisterForm registerForm
  ) {
    LOGGER.info(
      "Received registration request for username: " +
      registerForm.getUsername()
    );
    try {
      userService.createPerson(
        registerForm.getFirstname(),
        registerForm.getLastname(),
        registerForm.getPersonNumber(),
        registerForm.getEmail(),
        registerForm.getUsername(),
        passwordEncoder.encode(registerForm.getPassword())
      );
      LOGGER.info(
        "User registered successfully: " + registerForm.getUsername()
      );
      return ResponseEntity.ok(
        Map.of("message", "User registered successfully")
      );
    } catch (UserAlreadyExistsException e) {
      LOGGER.warning(
        "Registration failed - Username already exists: " +
        registerForm.getUsername()
      );
      return ResponseEntity
        .status(400)
        .body(Map.of("error", "Username already exists"));
    } catch (Exception e) {
      LOGGER.log(
        Level.SEVERE,
        "Unexpected error during registration for user: " +
        registerForm.getUsername(),
        e
      );
      return ResponseEntity
        .status(500)
        .body(
          Map.of("error", "An internal error occurred. Please try again later.")
        );
    }
  }
}
