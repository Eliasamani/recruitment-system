package se.kth.iv1201.recruitment.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtAuthenticationFilter;
import se.kth.iv1201.recruitment.security.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private JwtUtil jwtUtil;
  private PersonRepository personRepository;

  public SecurityConfig(JwtUtil jwtUtil, PersonRepository personRepository) {
    this.jwtUtil = jwtUtil;
    this.personRepository = personRepository;
    this.jwtAuthenticationFilter =
      new JwtAuthenticationFilter(jwtUtil, personRepository);
  }

  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/", "/api/session", "/api/login", "/api/register", 
                               "/index.html", "/static/**", "/*.png", "/*.json", "/*.ico", "/*.txt", "/error")
              .permitAll() // Public endpoints
              .requestMatchers("/api/logout").authenticated() // Only authenticated users can logout
              .requestMatchers("/api/protected").hasRole("RECRUITER") // Only recruiters can access protected route
              .anyRequest().authenticated()
          )
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) //Ensure JWT authentication happens before Spring Security's built-in authentication filter
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //Disable session storage, making authentication fully stateless (JWT required for every request)
  
      return http.build();
  }
  
    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // denna behövdes
        configuration.setAllowedOrigins(List.of("http://localhost:3000","https://iv1201-recr-0486e0c122b1.herokuapp.com/","https://recruitment.jontek.xyz/"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
