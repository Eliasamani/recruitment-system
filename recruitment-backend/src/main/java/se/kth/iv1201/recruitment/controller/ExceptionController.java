package se.kth.iv1201.recruitment.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.kth.iv1201.recruitment.model.exception.*;

import se.kth.iv1201.recruitment.service.SessionService;

import org.springframework.http.HttpStatus;
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
     * Handles a FieldAlreadyFilledException, which is thrown when the user tries to
     * change a field that is already filled.
     * 
     * @param exception the exception to be handled
     * @return
     */
    @ExceptionHandler(FieldAlreadyFilledException.class)
    public ResponseEntity<?> handleFieldAlreadyFilledException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("error", "Field already filled"));
    }

    /**
     * Handles an ApplicationNotFoundException, which is thrown when the user tries
     * to access an application that does not exist.
     * 
     * @param exception the exception to be handled
     * @return
     */
    @ExceptionHandler(ApplicationNotFoundException.class)
    public ResponseEntity<?> handleApplicationNotFoundException(ApplicationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles an ApplicationAlreadyExistsException, which is thrown when the user
     * already has an application.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the application already exists
     */
    @ExceptionHandler(ApplicationAlreadyExistsException.class)
    public ResponseEntity<?> handleApplicationAlreadyExistsException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("error", "Application already exists"));
    }

    /**
     * Handles a NoCookiesInRequestException, which is thrown when the user tries to
     * access an endpoint without a JWT token.
     * 
     * @param exception the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the user is not authenticated
     */
    @ExceptionHandler(NoCookiesInRequestException.class)
    public ResponseEntity<?> handleNoCookiesInRequestException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return ResponseEntity.status(400).body(Map.of("error", "Not authenticated"));
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

    /**
     * Handles a NonExistingEmailException, which is thrown when the user tries to
     * reset their password with an email that does not exist in the database.
     * 
     * @param exception the exception to be handled
     * @return
     */
    @ExceptionHandler(NonExistingEmailException.class)
    public ResponseEntity<?> handleNonExisitingEmailException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        if (exception.getMessage().contains("generateToken")) {
            return ResponseEntity.ok("");
        } else {
            return handleEmailAndCodeError();
        }
    }

    /**
     * Handles an IncorrectResetCodeException, which is thrown when the user tries
     * to reset their password with an incorrect code.
     * 
     * @param exception
     * @return
     */
    @ExceptionHandler(IncorrectResetCodeException.class)
    public ResponseEntity<?> handleIncorrectResetCodeException(Exception exception) {
        LOGGER.warning(exception.getMessage());
        return handleEmailAndCodeError();
    }

    /**
     * Handles when the email or code is incorrect
     * 
     * @return
     */
    private ResponseEntity<?> handleEmailAndCodeError() {
        return ResponseEntity.status(400).body(Map.of("error", "Email or code was incorrect"));
    }

    /**
     * Handles validation exceptions from @Valid annotated objects in the request
     * body.
     * 
     * @param ex the exception to be handled
     * @return a ResponseEntity containing a JSON object with a list of error
     *         messages
     *         indicating which fields were invalid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles an InvalidStatusException, which is thrown when the user tries to
     * update an application with an invalid status.
     * 
     * @param ex the exception to be handled
     * @return a ResponseEntity containing a JSON object with an "error" key, where
     *         the value is a message explaining that the status is invalid
     */
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<?> handleInvalidStatusException(InvalidStatusException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Handles the generic error page for postmappings which is shown when an
     * internal error happens
     * If simple error return 400 otherwise return original status code
     * 
     * @param request
     * @param response
     * @return
     */
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

    /**
     * Handles the generic error page for getmappings which is shown when an
     * internal error happens
     * If simple error redirect to home page otherwise return errorPage
     * 
     * @param request
     * @param model
     * @return
     */
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