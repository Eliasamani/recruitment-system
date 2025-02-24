package se.kth.iv1201.recruitment.controller;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.InvalidSessionException;
import se.kth.iv1201.recruitment.model.exception.NonExistingEmailException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.exception.UserServiceException;
import se.kth.iv1201.recruitment.service.SessionService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ControllerAdvice
public class ExceptionController implements ErrorController {

    private static final Logger LOGGER = Logger.getLogger(
            ExceptionController.class.getName());

    private final SessionService sessionService;

    public ExceptionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Handles an ExpiredJwtException, which is thrown when a JWT token has expired.
     * 
     * @param exception the exception to be handled
     * @param response  the HTTP response to be sent back to the client
     * 
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the token has expired and the
     *         user should log in again
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(Exception exception, HttpServletResponse response) {
        LOGGER.log(Level.INFO, "Token expired: " + exception.getMessage());
        response.addCookie(sessionService.removeCookie());
        return ResponseEntity.status(400).body(Map.of("error", "Token expired, please log in again"));
    }

    /**
     * Handles a BadCredentialsException, which is thrown when the user enters
     * an invalid username or password.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key,
     *         where the value is a message explaining that the username or
     *         password was invalid
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("error", "Invalid username or password"));
    }

    /**
     * Handles a JwtException, which is thrown when a JWT token is invalid.
     * 
     * @param exception the exception to be handled
     * @param response  the HTTP response to be sent back to the client
     * 
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the token is invalid.
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(Exception exception, HttpServletResponse response) {
        LOGGER.log(Level.WARNING, "Invalid token: " + exception.getMessage());
        response.addCookie(sessionService.removeCookie());
        return ResponseEntity.status(400).body(Map.of("error", "Invalid token"));
    }

    /**
     * Handles an InvalidSessionException, which is thrown when a user's session is
     * deemed invalid.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key,
     *         indicating that the session is invalid
     */

    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<?> handleInvalidSessionException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("error", "Invalid session"));
    }

    /**
     * Handles a UserServiceException, which is thrown when there is an unexpected
     * error in the user service layer.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key,
     *         indicating that an unexpected error occurred and the user should
     *         try again later
     */

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<?> handleUserServiceException(
            Exception exception) {
        LOGGER.severe(exception.getMessage());
        return ResponseEntity.internalServerError().body(Map.of("error", "Unexpected error occured try again later"));
    }

    /**
     * Handles a UserAlreadyExistsException, which is thrown when the user tries
     * to register with a username that already exists.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the username already exists
     */
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