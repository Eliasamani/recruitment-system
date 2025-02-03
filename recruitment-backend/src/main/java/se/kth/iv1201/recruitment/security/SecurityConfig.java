package se.kth.iv1201.recruitment.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final PersonRepository personRepository; // ✅ Inject PersonRepository

    public SecurityConfig(JwtUtil jwtUtil, PersonRepository personRepository) {
        this.jwtUtil = jwtUtil;
        this.personRepository = personRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/", "/api/login", "/api/register").permitAll() // ✅ Allow public access
            .requestMatchers("/api/protected").hasRole("RECRUITER") // ✅ Ensure role is checked correctly
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, personRepository), UsernamePasswordAuthenticationFilter.class); // ✅ Pass repository

        return http.build();
    }
}
