package se.kth.iv1201.recruitment.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se.kth.iv1201.recruitment.model.RegisterForm;
import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;

import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.service.JobApplicationService;
import se.kth.iv1201.recruitment.repository.JobApplicationRepository;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.service.UserService;

/**
 * Controller for user-related API operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(
            UserController.class.getName());

    private final PersonRepository repository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final JobApplicationService jobApplicationService;

    private final JobApplicationRepository JobApplicationRepository;

    public UserController(
            PersonRepository repository,
            UserService userService,
            PasswordEncoder passwordEncoder,

            JobApplicationService jobApplicationService,
            JobApplicationRepository jobApplicationRepository) {
        this.repository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        this.jobApplicationService = jobApplicationService;
        this.JobApplicationRepository = jobApplicationRepository;
    }

    /**
     * Temporary test endpoint to return all users (for testing purposes).
     * 
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
     * gets all applications in db
     * 
     */
    @GetMapping("/testFetchAllJobApplications")
    public List<JobApplication> fetchJobApps() throws Exception {
        return jobApplicationService.getAllApplications();
    }

    @GetMapping("/testJobApplication")
    public JobApplication jobApp() throws Exception {
        return jobApplicationService.findJobApplicationbyUsername("test123456");
    }

    @GetMapping("/testApplicationSave")
    public JobApplication saveApp() throws Exception {
        Person pers = repository.findPersonByUsername("test123");
        List<Competence> competences = new ArrayList();

        competences.add(new Competence(1, 2));
        competences.add(new Competence(2, 3));
        return jobApplicationService.saveApplication(pers, competences, null);
    }

    @GetMapping("/testApplicationDelete")
    public String getMethodName() {
        Person person = repository.findPersonByUsername("test123");
        this.JobApplicationRepository.deleteByPerson(person);
        return "Deleted test123 jobapplication";
    }

    /**
     * Handles user registration.
     * 
     * @param registerForm Request body containing user registration details.
     * @return ResponseEntity with success message or error if the username already
     *         exists.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterForm registerForm) {
        LOGGER.info(
                "Received registration request for username: " +
                        registerForm.getUsername());
        try {
            userService.createPerson(
                    registerForm.getFirstname(),
                    registerForm.getLastname(),
                    registerForm.getPersonNumber(),
                    registerForm.getEmail(),
                    registerForm.getUsername(),
                    passwordEncoder.encode(registerForm.getPassword()));
            LOGGER.info(
                    "User registered successfully: " + registerForm.getUsername());
            return ResponseEntity.ok(
                    Map.of("message", "User registered successfully"));
        } catch (UserAlreadyExistsException e) {
            LOGGER.warning(
                    "Registration failed - Username already exists: " +
                            registerForm.getUsername());
            return ResponseEntity
                    .status(400)
                    .body(Map.of("error", "Username already exists"));
        } catch (Exception e) {
            LOGGER.log(
                    Level.SEVERE,
                    "Unexpected error during registration for user: " +
                            registerForm.getUsername(),
                    e);
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of("error", "An internal error occurred. Please try again later."));
        }
    }
}
