package se.kth.iv1201.recruitment.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtAuthenticationFilter;
import se.kth.iv1201.recruitment.security.JwtUtil;
import se.kth.iv1201.recruitment.service.LoginRegisterService;
import se.kth.iv1201.recruitment.service.UserAlreadyExistsException;
import java.util.logging.Logger;


/**
 * Controller for Login and Register post APIs
 */
@RestController
public class LoginRegisterController {
  private static final Logger LOGGER = Logger.getLogger(LoginRegisterController.class.getName());

  @Autowired
  private PersonRepository repository; // for testing

  @Autowired
  private LoginRegisterService service;

  @Autowired
  private JwtUtil jwtUtil;

  private   PasswordEncoder encoder = new BCryptPasswordEncoder();

  @GetMapping("/mytest")
  public List<Person> test() {
    return repository.findAll();
  }

  @PostMapping(value = "/api/register", consumes = "application/json")
  // change parameter object by defining new one for the from with validation included,
  // decouples validation during creation of person obj, validaion more related to presentation
  public ResponseEntity<String> registerPerson(@Valid @RequestBody RegisterForm regform)
    throws Exception {
      try {
      service.createPerson(
        regform.getFirstname(),
        regform.getLastname(),
        regform.getPersonNumber(),
        regform.getEmail(),
        regform.getUsername(),
        encoder.encode(regform.getPassword())
      );
      } catch (UserAlreadyExistsException e) {
        return ResponseEntity.status(400).body("{\"message\":\"Username already exists\"}");
      }

    // Do acc creation logic in service as this is Business logic related

    return ResponseEntity.ok("ACCOUNT CREATED");
  }

  @PostMapping(value = "/api/login", consumes = "application/json")
  public ResponseEntity<?> loginPerson(
    @RequestBody Map<String, String> json,
    HttpServletResponse response
  ) throws Exception {
    PersonDTO person = service.findPerson(json.get("username"));
    if (person == null) {
      return  ResponseEntity.status(401).body("User does not exist");
    }

    // Compare password hashes
  
    if (!encoder.matches(json.get("password"), person.getPassword())) {
      return  ResponseEntity.status(401).body("Invalid password");
      // throw new Exception("Invalid credentials");
    }

    // Generate JWT Token
    String jwt = jwtUtil.generateToken(person.getUsername());

    // Create Secure HTTP-Only Cookie
    Cookie jwtCookie = new Cookie("jwt", jwt);
    jwtCookie.setHttpOnly(true); // Prevents JavaScript access (XSS protection)
    jwtCookie.setSecure(false); // Send only over HTTPS (set to false for local dev)
    jwtCookie.setPath("/"); // Available for all API routes
    jwtCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days expiration

    response.addCookie(jwtCookie);
    return new ResponseEntity(person, HttpStatusCode.valueOf(200));
  }

  @GetMapping("/api/session")
  public ResponseEntity<?> checkSession(HttpServletRequest request) {
    String token = null;

    // Get JWT from cookies
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("jwt".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    // Log if token is missing
    if (token == null) {
      LOGGER.warning("No JWT found in cookies!");
      return ResponseEntity
        .status(401)
        .body(Map.of("error", "Not authenticated"));
    }

    // Validate JWT Token
    String username = jwtUtil.validateToken(token);
    if (username == null) {
      LOGGER.severe("Invalid JWT token!");
      return ResponseEntity.status(403).body(Map.of("error", "Invalid token"));
    }

    LOGGER.info("User authenticated: " + username);
    return ResponseEntity.ok(Map.of("username", username));
  }

@PostMapping("/api/logout")
public ResponseEntity<?> logout(HttpServletResponse response) {
    Cookie jwtCookie = new Cookie("jwt", "");
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(0); // Expire immediately
    response.addCookie(jwtCookie);

    return ResponseEntity.ok("Logged out successfully");
}
}
