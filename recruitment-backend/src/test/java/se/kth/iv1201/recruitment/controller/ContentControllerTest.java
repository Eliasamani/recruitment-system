package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import se.kth.iv1201.recruitment.security.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtUtil.class)
public class ContentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * Tests accessing the protected API endpoint without authentication.
   * Expects:
   * - HTTP 403 Forbidden status (user is not logged in).
   */
  @Test
  void testCorrectApiProtectedGetWithNotLoggedIn() throws Exception {
    mockMvc.perform(get("/api/protected"))
           .andExpect(status().isForbidden()); // Expect 403 Forbidden
  }

  /**
   * Tests accessing the protected API endpoint with a non-authorized user.
   * The user "test123" is assumed to have a role that does not grant access.
   * Expects:
   * - HTTP 403 Forbidden status (user lacks required permissions).
   */
  @Test
  void testCorrectApiProtectedGetWithNonAuthorizedUser() throws Exception {
    String token = jwtUtil.generateToken("test123");
    MockCookie jwtCookie = new MockCookie("jwt", token);
    
    mockMvc.perform(get("/api/protected").cookie(jwtCookie))
           .andExpect(status().isForbidden()); // Expect 403 Forbidden
  }

  /**
   * Tests accessing the protected API endpoint with an authorized user.
   * The user "AustinMueller" is assumed to have the required role.
   * Expects:
   * - HTTP 200 OK status.
   * - Response containing a confirmation message that access was granted.
   */
  @Test
  void testCorrectApiProtectedGetWithLoggedIn() throws Exception {
    String token = jwtUtil.generateToken("AustinMueller");
    MockCookie jwtCookie = new MockCookie("jwt", token);
    
    mockMvc.perform(get("/api/protected").cookie(jwtCookie))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString(
               "Hello AustinMueller, you accessed a protected resource!"
           )));
  }
}
