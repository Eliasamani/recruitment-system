package se.kth.iv1201.recruitment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * form for user registration
 * also handles validation
 * 
 * Username, personNumber and mail are necessary fields
 */
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    @Override
    public String toString() {
        return "RegisterForm [username=" + username + ", password=" + password + ", firstname=" + firstname
                + ", lastname=" + lastname + ", email=" + email + ", personNumber=" + personNumber + "]";
    }

}
