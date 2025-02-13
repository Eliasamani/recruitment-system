package se.kth.iv1201.recruitment.controller;

import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import se.kth.iv1201.recruitment.repository.PersonRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/reset")
public class ResetController {


    private static final Logger LOGGER = Logger.getLogger(ResetController.class.getName());

    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ResetController(PersonRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/code")
    public ResponseEntity<?> generateCode(@RequestBody String email) {
        
        
        return entity;
    }
    




}
