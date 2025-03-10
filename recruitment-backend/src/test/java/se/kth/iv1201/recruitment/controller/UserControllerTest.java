package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtProvider;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Tests that a user can be registered with the correct format.
     * Also tests that the user is persisted in the database.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testRegisterCorrectFormat() throws Exception {
        String userName = "TestUser" + System.currentTimeMillis();
        String contentData = "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\""
                +
                userName +
                "\",\"email\":\"email@example.com\",\"password\":\"password\"}";

        mockMvc
                .perform(
                        post("/api/users/register")
                                .header("Content-Type", "application/json")
                                .content(contentData))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User registered successfully")));

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
     * 
     * @throws Exception if the test fails.
     */

    @Test
    void testRegisterAlreadyExistingUser() throws Exception {
        String userName = "TestRecruiter";
        String contentData = "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\""
                +
                userName +
                "\",\"email\":\"email@example.com\",\"password\":\"password\"}";

        mockMvc
                .perform(
                        post("/api/users/register")
                                .header("Content-Type", "application/json")
                                .content(contentData))
                .andExpect(status().isBadRequest())
                .andExpect(
                        content()
                                .string(containsString(
                                        "{\"error\":\"Username already exists\"}")));
    }

    /**
     * Tests that editing a user profile with all fields returns a success message.
     * 
     * It performs a mock HTTP POST request to the /api/users/edit endpoint
     * with user data that includes all fields.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithAllFields() throws Exception {
        String contentData = "{\"username\":\"TestRecruiter\",\"email\":\"newEmail@example.com\",\"personNumber\":\"19991212-4444\",\"lastname\":\"newLastName\",\"firstname\":\"newFirstName\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User updated successfully")));
    }

    /**
     * Tests that editing a user profile with no username returns a 400 Bad Request
     * error.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithNoUsername() throws Exception {
        String contentData = "{\"email\":\"newEmail@example.com\",\"personNumber\":\"19991212-4444\",\"lastname\":\"newLastName\",\"firstname\":\"newFirstName\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().is(400));

    }

    /**
     * Tests that editing a user profile with an incorrect username returns a 400
     * Bad Request error.
     * So that a user can not try to edit an other users profile.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithWrongUsername() throws Exception {
        String contentData = "{\"username\":\"AustinMueller\",\"email\":\"newEmail@example.com\",\"personNumber\":\"19991212-4444\",\"lastname\":\"newLastName\",\"firstname\":\"newFirstName\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().is(400))
                .andExpect(content().string(containsString("Invalid session")));

    }

    /**
     * Tests that editing a user profile with a non existing session returns a 403
     * Forbidden error.
     * So a user can not edit a profile without being logged in.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithNonExistingSession() throws Exception {
        String contentData = "{\"username\":\"AustinMueller\",\"email\":\"newEmail@example.com\",\"personNumber\":\"19991212-4444\",\"lastname\":\"newLastName\",\"firstname\":\"newFirstName\"}";
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData))
                .andExpect(status().is(403));

    }

    /**
     * Tests that editing a user profile with only email returns a success message.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithOnlyEmail() throws Exception {
        String contentData = "{\"username\":\"TestRecruiter\",\"email\":\"newEmail@example.com\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User updated successfully")));
    }

    /**
     * Tests that editing a user profile with only person number returns a success
     * message.
     * As long as the user does not already have a person number.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithOnlyPersonNumber() throws Exception {
        String contentData = "{\"username\":\"TestRecruiter\",\"personNumber\":\"19991212-4444\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User updated successfully")));
    }

    /**
     * Tests that editing a user profile with only last name returns a success
     * message.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithOnlyLastName() throws Exception {
        String contentData = "{\"username\":\"TestRecruiter\",\"lastname\":\"newLastName\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User updated successfully")));
    }

    /**
     * Tests that editing a user profile with only first name returns a success
     * message.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithOnlyFirstName() throws Exception {
        String contentData = "{\"username\":\"TestRecruiter\",\"firstname\":\"newFirstName\"}";
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("User updated successfully")));
    }

    /**
     * Tests that editing a user profile with already filled person number returns a
     * 400 Bad Request error.
     * As the user should only be able to fill in the person number once.
     * 
     * @throws Exception if the test fails.
     */
    @Test
    void testEditProfileWithAlreadyFilledPersonNumber() throws Exception {
        String contentData = "{\"username\":\"TestApplicant\",\"personNumber\":\"19991212-4444\"}";
        String token = jwtProvider.generateToken("TestApplicant");
        MockCookie jwtCookie = new MockCookie("jwt", token);
        mockMvc
                .perform(
                        post("/api/users/edit")
                                .header("Content-Type", "application/json")
                                .content(contentData).cookie(jwtCookie))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Field already filled")));
    }

}