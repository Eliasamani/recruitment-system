package se.kth.iv1201.recruitment.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se.kth.iv1201.recruitment.dto.RegisterForm;
import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.service.JobApplicationService;
import se.kth.iv1201.recruitment.service.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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


  private final CompetenceRepository comp_repository;
  private final AvailabilityRepository availability_repository;
  private JobApplicationService job_applicationService;
    
      public UserController(
        PersonRepository repository,
        UserService userService,
        PasswordEncoder passwordEncoder,
        CompetenceRepository comp_repository,
        AvailabilityRepository availability_repository,
        JobApplicationService job_applicationService
      ) {
        this.repository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.comp_repository = comp_repository;
        this.availability_repository = availability_repository;
        this.job_applicationService = job_applicationService;
  }

  /**
   * Temporary test endpoint to return all users (for testing purposes).
   * @return List of all persons in the database.
   */
  @GetMapping("/testFetchAll")
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
   * Temporary test endpoint to return all competences of a user 11  (for testing purposes).
   * @return List of all persons in the database.
   */
  @GetMapping("/testCompetences")
  public List<Competence> Competences() {
  
      return  comp_repository.findCompetencesByPersonId(11);
  }

  @GetMapping("/testAvalibility")
  public List<Availability> availabilities() {
      return  availability_repository.findAvailabilitiesByPersonId(11);
  }

  @GetMapping("/testJobApplication")
  public JobApplication jobApp() throws Exception{
      return  job_applicationService.findJobApplicationByPersonId(11);
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
