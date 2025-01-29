package se.kth.iv1201.recruitment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.kth.iv1201.recruitment.model.Account;
import se.kth.iv1201.recruitment.repository.AccountRepository;


@RestController
public class LoginController {

    @Autowired
    private AccountRepository repository;


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String getMethodName() {
        return "register_page";
    }
    
    @GetMapping("/mytest")
    public List<Account> test() {
        return repository.findAll();
    }
    
}
