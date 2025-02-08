package se.kth.iv1201.recruitment.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.kth.iv1201.recruitment.RecruitmentBackendApplication;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
public class JwtTokenProviderTest {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  /**
   * Tests that the generateToken method correctly generates a JWT token
   * for a given username. This ensures that the method returns a valid token
   * that can be used to authenticate a user.
   */
  @Test
  void testGenerateToken() {
    String token = jwtTokenProvider.generateToken("TestUser");
    assertNotNull(token, "Token should not be null");
  }

  /**
   * Tests that a valid JWT token can be validated to return the original username.
   * This ensures that the validateToken method correctly identifies and verifies
   * valid tokens by returning the username stored in the token.
   */
  @Test
  void testValidateToken() {
    String token = jwtTokenProvider.generateToken("TestUser");
    String username = jwtTokenProvider.validateToken(token);
    assertEquals("TestUser", username, "Username should match");
  }

/**
 * Tests that an invalid JWT token returns null when validated.
 * This ensures that the validateToken method correctly identifies
 * and handles invalid tokens by returning a null value.
 */

  @Test
  void testValidateInvalidToken() {
    String username = jwtTokenProvider.validateToken("invalidToken");
    assertNull(username, "Invalid token should return null");
  }
}
