package se.kth.iv1201.recruitment.controller;

import java.util.List;

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


/**
 * Controller for Login and Register post APIs
 */
@RestController
public class LoginRegisterController {

    @Autowired
    private PersonRepository repository;
    @Autowired 
    private LoginRegisterService service;

    private Person acc; // for testing
    
    @GetMapping("/mytest")
    public List<Person> test() {
        return repository.findAll();
    }

    @GetMapping("/mytest2")
    public Person test2() {
        return acc;
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
    public String loginPerson(@RequestBody String username, @RequestBody String password) throws Exception{
        PersonDTO person = service.findPerson(username);
        if(person == null)
            throw new Exception();


        return "Logged in";
    }
    



}
