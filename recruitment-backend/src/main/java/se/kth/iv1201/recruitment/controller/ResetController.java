package se.kth.iv1201.recruitment.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
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

    private final ResetService resetService;

    public ResetController(ResetService resetService) {

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

        ResetTokenDTO resetToken = resetService.generateToken(email);
        resetService.sendMail(email, resetToken);
        LOGGER.info("Sent code " + resetToken.getResetToken() + " to " + email);

        return ResponseEntity.ok("");
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

        PersonDTO resetUser = resetService.resetUsernameAndPassword(userPassResetForm);
        LOGGER.info("successfully reset password for user " + resetUser.getId());
        return ResponseEntity.ok().body(Map.of("message", "Reset successful"));

    }

}
