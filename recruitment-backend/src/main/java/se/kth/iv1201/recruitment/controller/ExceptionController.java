package se.kth.iv1201.recruitment.controller;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.NonExistingEmailException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.exception.UserServiceException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ControllerAdvice
public class ExceptionController implements ErrorController {

    private static final Logger LOGGER = Logger.getLogger(
            ExceptionController.class.getName());

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<?> handleUserServiceException(Exception exception) {
        LOGGER.severe(exception.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occured try again later"));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(Exception exception) {
        LOGGER.warning(exception.getMessage());

        return ResponseEntity
                .status(400)
                .body(Map.of("error", "Username already exists"));
    }

    @ExceptionHandler(NonExistingEmailException.class)
    public ResponseEntity<?> handleNonExisitingEmailException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        if (exception.getMessage().contains("generateToken")) {
            return ResponseEntity.ok("");
        } else {
            return handleEmailAndCodeError();
        }
    }

    @ExceptionHandler(IncorrectResetCodeException.class)
    public ResponseEntity<?> handleIncorrectResetCodeException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return handleEmailAndCodeError();
    }

    private ResponseEntity<?> handleEmailAndCodeError() {
        return ResponseEntity.status(400).body(Map.of("error", "Email or code was incorrect"));
    }

    @PostMapping("/error")
    @ResponseBody
    public ResponseEntity<?> handlePostMessage(HttpServletRequest request, HttpServletResponse response) {
        int statusCode = Integer.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());

        if (statusCode == 400) {
            return ResponseEntity.status(statusCode)
                    .body(request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString());
        }
        if (statusCode >= 401 && statusCode <= 405) {
            LOGGER.warning("User tried to access an unaccessible post endpoint");
            response.setStatus(400);
            statusCode = 400;
        }

        else {
            LOGGER.severe("NON NORMAL STATUS APPEARED ON POST REQUEST STATUS WAS " + statusCode);
        }
        return ResponseEntity.status(statusCode).body(Map.of("error", "Unknown error occurred during operation"));

    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        int statusCode = Integer.valueOf(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        model.addAttribute("statusCode", statusCode);

        if (statusCode >= 400 && statusCode <= 405) {
            LOGGER.warning("User tried to access an unaccessible page");
            return ("redirect:/");
        }

        LOGGER.severe("THE ERROR PAGE WAS GENERATED WITH STATUS " + statusCode);
        return "errorPage"; // return custom error view

    }

}