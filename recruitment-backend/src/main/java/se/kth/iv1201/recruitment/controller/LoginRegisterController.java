package se.kth.iv1201.recruitment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.service.LoginRegisterService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.kth.iv1201.recruitment.security.JwtUtil;




/**
 * Controller for Login and Register post APIs
 */
@RestController
public class LoginRegisterController {

    @Autowired
    private PersonRepository repository; // for testing

    @Autowired 
    private LoginRegisterService service;

    @Autowired
    private JwtUtil jwtUtil;


    
    @GetMapping("/mytest")
    public List<Person> test() {
        return repository.findAll();
    }

  

    @PostMapping(value = "/api/register", consumes = "application/json")
    // change parameter object by defining new one for the from with validation included, 
    // decouples validation during creation of person obj, validaion more related to presentation
    public String registerPerson(@Valid @RequestBody RegisterForm regform) throws Exception { 
        service.createPerson(regform.getFirstname(), regform.getLastname(), regform.getPersonNumber(), regform.getEmail(), regform.getUsername(), regform.getPassword());
        // Do acc creation logic in service as this is Business logic related

        return "ACCOUNT CREATED";        
    }

    @PostMapping(value = "/api/login", consumes = "application/json")
    public String loginPerson(@RequestBody Map<String, String> json) throws Exception{
       
        PersonDTO person = service.findPerson(json.get("username"));
        if(person == null)
            throw new Exception("User Does not exist"); //TODO write custom exceptions
        System.out.println(person.toString());

         // Compare password hashes
        //PasswordEncoder encoder = new BCryptPasswordEncoder();
        //if (!encoder.matches(json.get("password"), person.getPassword())) {
        //    throw new Exception("Invalid credentials");
        //}

        // Generate JWT Token
        return "Bearer "+jwtUtil.generateToken(person.getUsername()).toString();


        //return "Logged in "+person.toString();
    }
    



}
