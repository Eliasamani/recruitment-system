package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PersonRepository repository;

  /**
   * Tests that a user can be registered with the correct format.
   * Also tests that the user is persisted in the database.
   * @throws Exception if the test fails.
   */
  @Test
  void testRegisterCorrectFormat() throws Exception {
    String userName = "TestUser" + System.currentTimeMillis();
    String contentData =
      "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\"" +
      userName +
      "\",\"email\":\"email@example.com\",\"password\":\"password\"}";

    mockMvc
      .perform(
        post("/api/users/register")
          .header("Content-Type", "application/json")
          .content(contentData)
      )
      .andExpect(status().isOk())
      .andExpect(
        content().string(containsString("User registered successfully"))
      );

    PersonDTO person = repository.findPersonByUsername(userName);
    if (person == null) {
      fail("The registered user was not persisted in DB");
    }
  }

/**
 * Tests that registering a user with an already existing username returns a
 * 400 Bad Request error with the appropriate error message.
 * 
 * It performs a mock HTTP POST request to the /api/users/register endpoint
 * with user data that includes a username that is already taken.
 * 
 * Asserts that the response status is 400 and the response body contains
 * the error message indicating the username already exists.
 */

  @Test
  void testRegisterAlreadyExistingUser() throws Exception {
    String userName = "TestRecruiter";
    String contentData =
      "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\"" +
      userName +
      "\",\"email\":\"email@example.com\",\"password\":\"password\"}";

    mockMvc
      .perform(
        post("/api/users/register")
          .header("Content-Type", "application/json")
          .content(contentData)
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content()
          .string(containsString("{\"error\":\"Username already exists\"}"))
      );
  }
}
