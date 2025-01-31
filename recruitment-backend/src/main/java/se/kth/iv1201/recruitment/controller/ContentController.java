package se.kth.iv1201.recruitment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/***
 * Controller for view api, so only get maps here
 */
@RestController
public class ContentController {

 

    @GetMapping("/api/login")
    public String login() {
        return "login";
    }


    @GetMapping("/api/register")
    public String register() {
        return "register";
    }
}
