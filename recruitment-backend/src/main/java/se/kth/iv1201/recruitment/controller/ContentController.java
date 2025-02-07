package se.kth.iv1201.recruitment.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.logging.Logger;


/***
 * Controller for view api, so only get maps here
 */
@RestController
public class ContentController {

    private static final Logger LOGGER = Logger.getLogger(ContentController.class.getName());

 

    @GetMapping("/api/login")
    public String login() {
        return "login";
    }


    @GetMapping("/api/register")
    public String register() {
        return "register";
    }

    @GetMapping("/api/protected")
    public String protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            LOGGER.warning("ContentController: User is NOT authenticated");
            return "Forbidden: You are not authenticated!";
        }

        LOGGER.info("ContentController: Authentication Object - " + authentication);
        LOGGER.info("ContentController: Authenticated user - " + authentication.getName());
        LOGGER.info("ContentController: User authorities - " + authentication.getAuthorities());

        return "Hello " + authentication.getName() + ", you accessed a protected resource!";
    }
}
