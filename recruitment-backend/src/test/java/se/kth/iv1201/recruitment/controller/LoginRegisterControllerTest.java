package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import se.kth.iv1201.recruitment.security.JwtUtil;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtUtil.class)
@Transactional
public class LoginRegisterControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PersonRepository repository;

    /**
     * Tests the login endpoint with valid credentials.
     * Expects:
     * - HTTP 200 OK status
     * - A "Set-Cookie" header containing the JWT token.
     */
    @Test
    void testLoginWithValidCredentials() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/login")
                .header("Content-Type", "application/json")
                .content("{\"username\":\"TestRecruiter\", \"password\":\"testpassword\"}"))
               .andExpect(status().isOk()) // Expect successful login
               .andReturn();
        
        // Check if the JWT token is set in the cookie
        String cookieHeader = result.getResponse().getHeader("Set-Cookie");
        assert cookieHeader != null && cookieHeader.contains("jwt");
    }

    /**
     * Tests the login endpoint with invalid credentials.
     * Expects:
     * - HTTP 401 Unauthorized status
     */
    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        mockMvc.perform(post("/api/login")
                .header("Content-Type", "application/json")
                .content("{\"username\":\"TestRecruiter\", \"password\":\"wrongpassword\"}"))
               .andExpect(status().isUnauthorized()); // Expect failure due to invalid password
    }

    /**
     * Tests the session validation endpoint with a valid JWT token.
     * Expects:
     * - HTTP 200 OK status
     * - Response containing the username of the authenticated user.
     */
    @Test
    void testSessionValidationWithCorrectToken() throws Exception {
        String token = jwtUtil.generateToken("TestRecruiter"); // Generate a JWT for testing
        MockCookie jwtCookie = new MockCookie("jwt", token);
        
        mockMvc.perform(get("/api/session").cookie(jwtCookie))
               .andExpect(status().isOk()) // Expect successful authentication
               .andExpect(content().string(containsString("TestRecruiter"))); // Expect the correct username
    }

    /**
     * Tests the session validation endpoint without a JWT token.
     * Expects:
     * - HTTP 401 Unauthorized status
     */
    @Test
    void testSessionValidationWithMissingToken() throws Exception {
        mockMvc.perform(get("/api/session"))
               .andExpect(status().isUnauthorized()); // Expect failure due to missing token
    }

    /**
     * Tests the logout functionality.
     * Expects:
     * - HTTP 200 OK status
     * - Response containing a logout success message.
     * - JWT cookie should be cleared and have a max-age of 0.
     */
    @Test
    void testLogoutClearsJwtCookie() throws Exception {
        String token = jwtUtil.generateToken("TestRecruiter"); // Generate a JWT for testing
        MockCookie jwtCookie = new MockCookie("jwt", token);
    
        mockMvc.perform(post("/api/logout").cookie(jwtCookie))
               .andExpect(status().isOk()) // Expect successful logout
               .andExpect(content().string(containsString("Logged out successfully"))) // Expect logout message
               .andExpect(cookie().value("jwt", ""))  // Ensure JWT cookie is cleared
               .andExpect(cookie().maxAge("jwt", 0)); // Ensure the cookie expires immediately
    }
    @Test
    void testRegisterCorrectFormat() throws Exception {


        String userName = String.valueOf(System.currentTimeMillis());
        String contentdata = "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\""+userName+"\",\"email\":\"email@example.com\",\"password\":\"password\"}";
        mockMvc.perform(post("/api/register").header("Content-Type", "application/json").content(contentdata)).andExpect(status().isOk()).andExpect(content().string(containsString("ACCOUNT CREATED")));
        PersonDTO person = repository.findPersonByUsername(userName); 
        if(person == null){
            // we got problem
            fail("The registered acc was not persisted in db");
        }

    }
    @Test
    void testRegisterAlreadyExistingUser() throws Exception {
        String userName = "TestRecruiter";
        String contentdata = "{\"firstname\":\"name\",\"lastname\":\"name\",\"personNumber\":\"19991212-4444\",\"username\":\""+userName+"\",\"email\":\"email@example.com\",\"password\":\"password\"}";
        
        mockMvc.perform(post("/api/register").header("Content-Type", "application/json").content(contentdata)).andExpect(status().isBadRequest()).andExpect(content().string(containsString("{\"message\":\"Username already exists\"}")));
    }
}
