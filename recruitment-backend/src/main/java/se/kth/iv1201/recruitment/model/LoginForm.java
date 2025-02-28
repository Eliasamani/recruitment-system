package se.kth.iv1201.recruitment.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotBlank(message = "Username is left empty ")
    private String username;

    @NotBlank(message = "Password field is empty")
    private String password;
}
