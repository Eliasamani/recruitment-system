package se.kth.iv1201.recruitment.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPassResetForm {
    @Email(message = "invalid email format")
    @NotBlank(message = "Email field is empty")
    private String email;

    private String password;

    @NotBlank(message = "Code field is empty")
    @Min(value = 100000, message = "Code should be above 100000")
    @Max(value = 1000000, message = "Code should be below 1000000")
    private String code;

    private String username;

    @Override
    public String toString() {
        return "PassResetForm [email=" + email + ", password=" + password + ", code=" + code + "]";
    }

}
