package se.kth.iv1201.recruitment.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.dto.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private PersonRepository repository;

  /**
   * Verifies that the createPerson method creates a person with the
   * specified parameters and that the created person is stored in the
   * database.
   */
  @Test
  void testCreatePerson() {
    String username = "UniqueUser" + System.currentTimeMillis();
    assertDoesNotThrow(() ->
      userService.createPerson(
        "First",
        "Last",
        "19991212-4444",
        "email@example.com",
        username,
        "password"
      )
    );
    assertNotNull(repository.findPersonByUsername(username));
  }

  /**
   * Verifies that creating a person with an existing username throws 
   * a UserAlreadyExistsException.
   */

  @Test
  void testCreateExistingPersonThrowsException() {
    String username = "TestRecruiter";
    assertThrows(
      UserAlreadyExistsException.class,
      () ->
        userService.createPerson(
          "First",
          "Last",
          "19991212-4444",
          "email@example.com",
          username,
          "password"
        )
    );
  }

  /**
   * Verifies that a user can be found by username.
   */
  @Test
  void testFindPerson() {
    PersonDTO person = userService.findPerson("TestRecruiter");
    assertNotNull(person);
    assertEquals("TestRecruiter", person.getUsername());
  }

  /**
   * Verifies that a null is returned when a person with the given username does not exist.
   */
  @Test
  void testFindNonExistentPersonReturnsNull() {
    PersonDTO person = userService.findPerson("NonExistentUser");
    assertNull(person);
  }
}
