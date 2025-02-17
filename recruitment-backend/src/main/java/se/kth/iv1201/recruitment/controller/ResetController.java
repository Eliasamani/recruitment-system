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

    @PostMapping("/code")
    public ResponseEntity<?> generateCode(
            @RequestBody @Valid @Email(message = "invalid email format") @NotBlank(message = "Email field is empty") String email) {
        LOGGER.info("Recieved password/username reset code request for " + email);
        try {
            ResetTokenDTO resetToken = resetService.generateToken(email);
            LOGGER.info("Sent code to " + email);
            System.out.println("Send email containing token: " + resetToken.getResetToken());

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
