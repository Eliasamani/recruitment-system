package se.kth.iv1201.recruitment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
/**
 * form for editing user profile
 * backend validation of fields are done here
 * 
 * Username is the only obligatory field
 * 
 */
@Getter
@Setter
public class EditProfileForm {

    @NotBlank(message = "Username is left empty ")
    private String username;

    @Email(message = "invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{8}-\\d{4}$", message = "invalid person number format, Expected:YYYYMMDD-NNNN")
    private String personNumber;

    private String lastname;

    private String firstname;

}
