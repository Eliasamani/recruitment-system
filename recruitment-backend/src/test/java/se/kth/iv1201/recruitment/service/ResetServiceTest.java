package se.kth.iv1201.recruitment.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.UserPassResetForm;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.NonExistingEmailException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.resettoken.ResetToken;
import se.kth.iv1201.recruitment.model.resettoken.ResetTokenDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.repository.ResetTokenRepository;

@SpringBootTest
@Transactional
public class ResetServiceTest {

    @Autowired
    private ResetService resetService;
    @Autowired
    private ResetTokenRepository resetTokenRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
/**
 * Tests that an exception is thrown if the email doesn't exist
 */
    @Test
    void testGenerateTokenWithNonExistingEmail() {
        assertThrows(NonExistingEmailException.class,
                () -> resetService.generateToken("nonexistingemail"));

    }
/**
 * Tests that a valid code is returend if an existing code is given
 */
    @Test
    void testGenerateCodeWithCorrectEmail() {
        ResetTokenDTO genratedToken = resetService.generateToken("appltest@test.com");
        assertNotNull(genratedToken);
        assertNotNull(resetTokenRepository.findResetTokenByPersonAndValidTrue(genratedToken.getPerson()));
    }
/**
 * Tests that it is possible to reset both username and password
 */
    @Test
    void testResetUsernameAndPassword() {
        assertNotEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "resetUsername");
        assertFalse(passwordEncoder.matches("resetTestPass",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(resetService.generateToken("appltest@test.com").getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("resetUsername");
        resetService.resetUsernameAndPassword(userPassResetForm);
        assertEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "resetUsername");
        assertTrue(passwordEncoder.matches("resetTestPass",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
    }
/**
 * Tests that it is possible to reset only password
 */
    @Test
    void testResetUsernameAndPasswordWithOnlyPassword() {
        assertEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "TestApplicant");
        assertFalse(passwordEncoder.matches("resetTestPass",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(resetService.generateToken("appltest@test.com").getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("");
        resetService.resetUsernameAndPassword(userPassResetForm);
        assertEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "TestApplicant");
        assertTrue(passwordEncoder.matches("resetTestPass",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
    }
/**
 * Tests that it is possible to reset only username
 */
    @Test
    void testResetUsernameWithOnlyUsername() {
        assertNotEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "resetUsername");
        assertTrue(passwordEncoder.matches("testpassword",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(resetService.generateToken("appltest@test.com").getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("");
        userPassResetForm.setUsername("resetUsername");
        resetService.resetUsernameAndPassword(userPassResetForm);
        assertEquals(personRepository.findPersonByEmail("appltest@test.com").getUsername(), "resetUsername");
        assertTrue(passwordEncoder.matches("testpassword",
                personRepository.findPersonByEmail("appltest@test.com").getPassword()));
    }
/**
 * Tests that an error is thrown if incorrect email
 */
    @Test
    void testResetUsernameAndPasswordWithNonExistingEmail() {
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(resetService.generateToken("appltest@test.com").getResetToken()));
        userPassResetForm.setEmail("appltest.test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("resetUsername");

        assertThrows(NonExistingEmailException.class, () -> resetService.resetUsernameAndPassword(userPassResetForm));
    }
/**
 * Tests that an exception is thrown if the username that the user is trying to reset to already exists
 */
    @Test
    void testResetUsernameAndPasswordWithAlreadyExistingUserName() {
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(resetService.generateToken("appltest@test.com").getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("TestRecruiter");

        assertThrows(UserAlreadyExistsException.class, () -> resetService.resetUsernameAndPassword(userPassResetForm));
    }
/**
 * Tests that an exception is thrown if an incorrect token is used
 */
    @Test
    void testResetUsernameAndPasswordWithIncorrectToken() {
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        userPassResetForm.setCode(String.valueOf(10));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("resetUsername");

        assertThrows(IncorrectResetCodeException.class, () -> resetService.resetUsernameAndPassword(userPassResetForm));
    }
/**
 * Tests that an exception is thrown if token is invalid i.e. alredy used or a new one has been issued
 */
    @Test
    void testResetUsernameAndPasswordWithInvalidToken() {
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        ResetTokenDTO resetTokenDTO = resetService.generateToken("appltest@test.com");
        ResetToken token = resetTokenRepository.findResetTokenByPersonAndResetToken(
                personRepository.findPersonByEmail("appltest@test.com"), resetTokenDTO.getResetToken());
        token.setValid(false);
        resetTokenDTO = resetTokenRepository.save(token);
        userPassResetForm.setCode(String.valueOf(resetTokenDTO.getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("resetUsername");

        assertThrows(IncorrectResetCodeException.class, () -> resetService.resetUsernameAndPassword(userPassResetForm));
    }
/**
 * Tests that an exception is thrown if token is expired
 */
    @Test
    void testResetUsernameAndPasswordWithExpiredToken() {
        UserPassResetForm userPassResetForm = new UserPassResetForm();
        ResetTokenDTO resetTokenDTO = resetService.generateToken("appltest@test.com");
        ResetToken token = resetTokenRepository.findResetTokenByPersonAndResetToken(
                personRepository.findPersonByEmail("appltest@test.com"), resetTokenDTO.getResetToken());
        token.setCreateTime(Instant.EPOCH);
        resetTokenDTO = resetTokenRepository.save(token);
        userPassResetForm.setCode(String.valueOf(resetTokenDTO.getResetToken()));
        userPassResetForm.setEmail("appltest@test.com");
        userPassResetForm.setPassword("resetTestPass");
        userPassResetForm.setUsername("resetUsername");

        assertThrows(IncorrectResetCodeException.class, () -> resetService.resetUsernameAndPassword(userPassResetForm));
    }
}
