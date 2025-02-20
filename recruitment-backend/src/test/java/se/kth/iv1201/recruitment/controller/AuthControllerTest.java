package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import se.kth.iv1201.recruitment.security.JwtProvider;
import se.kth.iv1201.recruitment.service.UserService;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    /**
     * Tests the login endpoint with valid credentials to ensure it returns a status
     * of 200 OK
     * and sets the JWT cookie in the response.
     * 
     * @throws Exception if the login request fails
     */
    @Test
    void testLoginWithValidCredentials() throws Exception {
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"TestRecruiter\", \"password\":\"testpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwt"));
    }

    /**
     * Verifies that the login endpoint returns a 401 Unauthorized status when given
     * invalid credentials.
     * 
     * @throws Exception
     */
    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"TestRecruiter\", \"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Verifies that the session validation endpoint returns the username of the
     * authenticated user
     * when given a valid JWT.
     * 
     * @throws Exception
     */
    @Test
    void testSessionValidationWithCorrectToken() throws Exception {
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);

        mockMvc
                .perform(get("/api/auth/session").cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("TestRecruiter")));
    }

    /**
     * Verifies that the session validation endpoint returns a 401 Unauthorized
     * status when no valid JWT cookie is
     * present in the request.
     */
    @Test
    void testSessionValidationWithMissingToken() throws Exception {
        mockMvc
                .perform(get("/api/auth/session"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Verifies that the logout endpoint properly clears the JWT cookie by setting
     * its value to an empty string
     * and its max age to 0.
     * 
     * @throws Exception
     */
    @Test
    void testLogoutClearsJwtCookie() throws Exception {
        String token = jwtProvider.generateToken("TestRecruiter");
        MockCookie jwtCookie = new MockCookie("jwt", token);

        mockMvc
                .perform(post("/api/auth/logout").cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out successfully")))
                .andExpect(cookie().value("jwt", ""))
                .andExpect(cookie().maxAge("jwt", 0));
    }
}
