package se.kth.iv1201.recruitment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Controller for Login and Register post APIs
 */
@RestController
public class LoginRegisterController {

    @Autowired
    private PersonRepository repository;


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
    public String registerPerson(@RequestBody PersonDTO person) { 

        // Do acc creation logic in service as this is Business logic related

        return "ACCOUNT CREATED";        
    }

}
