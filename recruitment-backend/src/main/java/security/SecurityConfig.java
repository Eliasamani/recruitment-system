package security;

import java.beans.BeanProperty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

    return httpSecurity
    .formLogin(loginform -> {
        loginform.loginPage("/login").permitAll();  
    })

    .authorizeHttpRequests(register -> {
        register.requestMatchers("/register","/test").permitAll(); //TODO permit css / js stuff as well 
        register.anyRequest().authenticated(); // any other require perms
    } )

    .build();
    }
}
