package se.kth.iv1201.recruitment.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import se.kth.iv1201.recruitment.model.UserPassResetForm;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.NonExistingEmailException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.model.resettoken.ResetTokenDTO;
import se.kth.iv1201.recruitment.service.ResetService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/reset")
public class ResetController {

    private static final Logger LOGGER = Logger.getLogger(ResetController.class.getName());

    private final PasswordEncoder passwordEncoder;
    private final ResetService resetService;

    public ResetController(PasswordEncoder passwordEncoder, ResetService resetService) {
        this.passwordEncoder = passwordEncoder;
        this.resetService = resetService;

    }

    /**
     * Generates a code for the user to reset their username and/or password which
     * is later sent to their email
     * 
     * @param email The users email address
     * @return Simple message telling the user the email might have been sent, to
     *         not leak which emails are registred
     */
    @PostMapping("/code")
    public ResponseEntity<?> generateCode(
            @RequestBody @Valid @Email(message = "invalid email format") @NotBlank(message = "Email field is empty") String email) {
        LOGGER.info("Recieved password/username reset code request for " + email);
        try {
            ResetTokenDTO resetToken = resetService.generateToken(email);
            resetService.sendMail(email, resetToken);
            LOGGER.info("Sent code " + resetToken.getResetToken() + " to " + email);

        } catch (NonExistingEmailException e) {
            LOGGER.warning("Email " + email + "did not exist could not send code");

        } catch (Exception e) {
            LOGGER.severe("Unexcpected error occurred during handling of reset code send for" + email + "Error:" + e);
            return ResponseEntity
                    .status(500)
                    .body(
                            Map.of("error", "An internal error occurred. Please try again later."));
        }
        return ResponseEntity.ok(Map.of("message", "Will have been sent if email was correct"));
    }

    /**
     * Allows user to use a code and their email to reset their username and/or
     * password for the website
     * 
     * @param userPassResetForm A json object containing username, email, password
     *                          and code
     * @return Either success or an error message describing why the function would
     *         have failed.
     */
    @PostMapping("/password")
    public ResponseEntity<?> setNewPassword(@RequestBody @Valid UserPassResetForm userPassResetForm) {
        try {
            PersonDTO resetUser = resetService.resetUsernameAndPassword(userPassResetForm);
            LOGGER.info("successfully reset password for user " + resetUser.getId());
            return ResponseEntity.ok().body(Map.of("message", "Reset successful"));
        }

        catch (IncorrectResetCodeException e) {
            LOGGER.warning("Reset code used with email " + userPassResetForm.getEmail() + " was incorrect " + e);
            return ResponseEntity
                    .status(400)
                    .body(Map.of("error", "Email or code was incorrect"));
        } catch (NonExistingEmailException e) {
            LOGGER.warning("Email " + userPassResetForm.getEmail() + "did not exist");
            return ResponseEntity
                    .status(400)
                    .body(Map.of("error", "Email or code was incorrect"));
        } catch (UserAlreadyExistsException e) {
            LOGGER.warning("User with email " + userPassResetForm.getEmail() + "tried to set username to "
                    + userPassResetForm.getUsername() + " but it is already taken");
            return ResponseEntity
                    .status(400)
                    .body(Map.of("error", "Username already exists"));
        }

    }

}
