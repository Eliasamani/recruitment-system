package se.kth.iv1201.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import se.kth.iv1201.recruitment.model.LoginForm;
import se.kth.iv1201.recruitment.model.exception.InvalidSessionException;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.security.JwtProvider;

@SpringBootTest
@Transactional
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    /**
     * Tests that an exception is thrown when no JWT cookie is present in the
     * request.
     */
    @Test
    void testCheckSessionNoJwtCookie() {
        Cookie[] cookies = new Cookie[0];
        assertThrows(InvalidSessionException.class, () -> sessionService.checkSession(cookies));
    }

    /**
     * Tests that an exception is thrown when the JWT token is invalid.
     */
    @Test
    void testCheckSessionInvalidToken() {
        String token = "invalid_token";
        Cookie jwtCookie = new Cookie("jwt", token);
        Cookie[] cookies = { jwtCookie };
        when(jwtProvider.validateToken(token)).thenThrow(new RuntimeException("Invalid token"));
        assertThrows(RuntimeException.class, () -> sessionService.checkSession(cookies));
    }

    /**
     * Tests that an exception is thrown when the user is not found for a valid
     * token.
     */
    @Test
    void testCheckSessionUserNotFound() {
        String token = "valid_token";
        String username = "testuser";
        Cookie jwtCookie = new Cookie("jwt", token);
        Cookie[] cookies = { jwtCookie };
        when(jwtProvider.validateToken(token)).thenReturn(username);
        when(userService.findPerson(username)).thenReturn(null);
        assertThrows(InvalidSessionException.class, () -> sessionService.checkSession(cookies));
    }

    /**
     * Tests that a valid session returns correct user data.
     */
    @Test
    void testCheckSessionValid() {
        String token = "valid_token";
        String username = "testuser";
        Cookie jwtCookie = new Cookie("jwt", token);
        Cookie[] cookies = { jwtCookie };

        PersonDTO person = mock(PersonDTO.class);
        when(person.getId()).thenReturn(1L);
        when(person.getUsername()).thenReturn(username);
        when(person.getFirstname()).thenReturn("John");
        when(person.getLastname()).thenReturn("Doe");
        when(person.getPersonNumber()).thenReturn("1234567890");
        when(person.getEmail()).thenReturn("john.doe@example.com");
        when(person.getRole()).thenReturn(PersonDTO.roles.APPLICANT);

        when(jwtProvider.validateToken(token)).thenReturn(username);
        when(userService.findPerson(username)).thenReturn(person);

        Map<String, Object> userData = sessionService.checkSession(cookies);
        assertNotNull(userData);
        assertEquals(1L, userData.get("id"));
        assertEquals(username, userData.get("username"));
        assertEquals("John", userData.get("firstName"));
        assertEquals("Doe", userData.get("lastName"));
        assertEquals("1234567890", userData.get("personNumber"));
        assertEquals("john.doe@example.com", userData.get("email"));
        assertEquals(PersonDTO.roles.APPLICANT.ordinal(), userData.get("role"));
    }

    /**
     * Tests that an exception is thrown when the username does not exist during
     * login.
     */
    @Test
    void testLoginUsernameNotFound() {
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("unknown");
        loginForm.setPassword("password");

        when(userService.findPerson("unknown")).thenReturn(null);
        assertThrows(BadCredentialsException.class, () -> sessionService.login(loginForm));
    }

    /**
     * Tests that an exception is thrown when the password is incorrect.
     */
    @Test
    void testLoginIncorrectPassword() {
        String username = "testuser";
        String password = "wrongpassword";
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername(username);
        loginForm.setPassword(password);

        PersonDTO person = mock(PersonDTO.class);
        when(person.getPassword()).thenReturn("encoded_password");

        when(userService.findPerson(username)).thenReturn(person);
        when(passwordEncoder.matches(password, "encoded_password")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> sessionService.login(loginForm));
    }

    /**
     * Tests that a successful login returns a valid JWT cookie.
     */
    @Test
    void testLoginSuccess() {
        String username = "testuser";
        String password = "correctpassword";
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername(username);
        loginForm.setPassword(password);

        PersonDTO person = mock(PersonDTO.class);
        when(person.getUsername()).thenReturn(username);
        when(person.getPassword()).thenReturn("encoded_password");

        when(userService.findPerson(username)).thenReturn(person);
        when(passwordEncoder.matches(password, "encoded_password")).thenReturn(true);

        String token = "generated_token";
        when(jwtProvider.generateToken(username)).thenReturn(token);

        Cookie cookie = sessionService.login(loginForm);
        assertNotNull(cookie);
        assertEquals("jwt", cookie.getName());
        assertEquals(token, cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.getSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(7 * 24 * 60 * 60, cookie.getMaxAge());
    }

    /**
     * Tests that removeCookie returns a cookie configured to expire the JWT cookie.
     */
    @Test
    void testRemoveCookie() {
        Cookie cookie = sessionService.removeCookie();
        assertNotNull(cookie);
        assertEquals("jwt", cookie.getName());
        assertEquals("", cookie.getValue());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.getSecure());
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
    }
}