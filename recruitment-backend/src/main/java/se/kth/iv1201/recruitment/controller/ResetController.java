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
import se.kth.iv1201.recruitment.dto.ResetTokenDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.service.ResetService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/reset")
public class ResetController {


    private static final Logger LOGGER = Logger.getLogger(ResetController.class.getName());

    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ResetService resetService;

    public ResetController(PersonRepository repository, PasswordEncoder passwordEncoder,ResetService resetService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.resetService = resetService;

    }

    @PostMapping("/code")
    public ResponseEntity<?> generateCode(@RequestBody @Valid @Email(message = "invalid email format") @NotBlank(message = "Email field is empty") String email) {
        try{
            ResetTokenDTO resetToken = resetService.generateToken(email);
            if (resetToken != null){
                System.out.println("Send email containing token: "+resetToken.getResetToken());
            }
            return ResponseEntity.ok(Map.of("message","Will have been sent if email was correct"));
        }
        catch(Exception e){
            return ResponseEntity
            .status(500)
            .body(
              Map.of("error", "An internal error occurred. Please try again later.")
            );
        }
        
        
    }
    




}
