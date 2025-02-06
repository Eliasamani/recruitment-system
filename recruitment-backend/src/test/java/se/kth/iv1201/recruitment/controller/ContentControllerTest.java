package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.security.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtUtil.class)
@Transactional
public class ContentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtUtil jwtUtil;

  /**
   * Tests that the get endpoint for login simply returns the word login
   * Expects:
  * - HTTP 200 OK status
  * - Response containing the word login.   
  */
  @Test
  void testCorrectApiLoginGetWithNotLoggedIn() throws Exception{
      mockMvc.perform(get("/api/login")).andExpect(status().isOk()).andExpect(content().string(containsString("login")));
  }
  /**
   * Tests that the get endpoint for login simply returns the word login also when logged in
   * Expects:
  * - HTTP 200 OK status
  * - Response containing the word login.   
  */
  @Test
  void testCorrectApiLoginGetWithLoggedIn() throws Exception{
      String token = jwtUtil.generateToken("TestApplicant");
      MockCookie jwtCookie = new MockCookie("jwt", token);
      mockMvc.perform(get("/api/login").cookie(jwtCookie)).andExpect(status().isOk()).andExpect(content().string(containsString("login")));
  }
  /**
   * Tests that the get endpoint for register simply returns the word register
   * Expects:
  * - HTTP 200 OK status
  * - Response containing the word login.   
  */
  @Test
  void testCorrectApiRegisterGetWithNotLoggedIn() throws Exception{
      mockMvc.perform(get("/api/register")).andExpect(status().isOk()).andExpect(content().string(containsString("register")));
  }
  /**
   * Tests that the get endpoint for register simply returns the word register also when logged in
   * Expects:
  * - HTTP 200 OK status
  * - Response containing the word login.   
  */
  @Test
  void testCorrectApiRegisterGetWithLoggedIn() throws Exception{
      String token = jwtUtil.generateToken("TestApplicant");
      MockCookie jwtCookie = new MockCookie("jwt", token);
      mockMvc.perform(get("/api/register").cookie(jwtCookie)).andExpect(status().isOk()).andExpect(content().string(containsString("register")));

  }
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
   * The user "TestApplicant" is assumed to have a role that does not grant access.
   * Expects:
   * - HTTP 403 Forbidden status (user lacks required permissions).
   */
  @Test

  void testCorrectApiProtectedGetWithNonAuthorizedUser() throws Exception {
    String token = jwtUtil.generateToken("TestApplicant");
    MockCookie jwtCookie = new MockCookie("jwt", token);
    
    mockMvc.perform(get("/api/protected").cookie(jwtCookie))
           .andExpect(status().isForbidden()); // Expect 403 Forbidden
  }

  /**
   * Tests accessing the protected API endpoint with an authorized user.
   * The user "TestRecruiter" is assumed to have the required role.
   * Expects:
   * - HTTP 200 OK status.
   * - Response containing a confirmation message that access was granted.
   */
  @Test
  void testCorrectApiProtectedGetWithAuthorizedUser() throws Exception {
    String token = jwtUtil.generateToken("TestRecruiter");
    MockCookie jwtCookie = new MockCookie("jwt", token);
    
    mockMvc.perform(get("/api/protected").cookie(jwtCookie))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString(
               "Hello TestRecruiter, you accessed a protected resource!"
           )));
  }
}
