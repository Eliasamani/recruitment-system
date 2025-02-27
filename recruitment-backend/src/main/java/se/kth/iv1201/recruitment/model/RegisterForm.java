package se.kth.iv1201.recruitment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * form for user registration
 * backend validation of fields are done here
 * 
 * Username, personNumber and mail are obligatory fields
 */
@Getter
@Setter
@ToString
public class RegisterForm {

    @NotBlank(message = "Username is left empty ")
    private String username;

    @NotBlank(message = "Password field is empty")
    private String password;

    @NotBlank(message = "Firstname field is empty")
    private String firstname;

    @NotBlank(message = "Lastname field is empty")
    private String lastname;

    @Email(message = "invalid email format")
    @NotBlank(message = "Email field is empty")
    private String email;

    @Pattern(regexp = "^\\d{8}-\\d{4}$", message = "invalid person number format, Expected:YYYYMMDD-NNNN")
    @NotBlank(message = "person number field is empty")
    private String personNumber;

}
