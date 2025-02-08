package se.kth.iv1201.recruitment.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtAuthenticationFilter;
import se.kth.iv1201.recruitment.security.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private JwtTokenProvider jwtTokenProvider;
  private PersonRepository personRepository;

  public SecurityConfig(
    JwtTokenProvider jwtTokenProvider,
    PersonRepository personRepository
  ) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.personRepository = personRepository;
    this.jwtAuthenticationFilter =
      new JwtAuthenticationFilter(jwtTokenProvider, personRepository);
  }

  /**
   * Configures the security filter chain to secure the application.
   * The filter chain is responsible for authenticating and authorizing requests.
   * It is configured to secure all endpoints except for a few public endpoints
   * (/, /api/auth/session, /api/auth/login, /api/users/register, /index.html, /static/**, /*.png, /*.json, /*.ico, /*.txt, /error),
   * which are accessible by anyone.
   * The /api/auth/logout endpoint requires authentication, but does not require any specific role.
   * The /api/auth/protected endpoint requires the RECRUITER role.
   * All other endpoints require authentication.
   * Session management is disabled, making authentication fully stateless (JWT required for every request).
   * @param http The HttpSecurity object to configure
   * @return The configured SecurityFilterChain
   * @throws Exception If an error occurs while configuring the filter chain
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(
            "/",
            "/api/auth/session",
            "/api/auth/login",
            "/api/users/register",
            "/index.html",
            "/static/**",
            "/*.png",
            "/*.json",
            "/*.ico",
            "/*.txt",
            "/error"
          )
          .permitAll() // Public endpoints
          .requestMatchers("/api/auth/logout")
          .authenticated() // Only authenticated users can logout
          .requestMatchers("/api/auth/protected")
          .hasRole("RECRUITER") // Only recruiters can access protected route
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      ) //Ensure JWT authentication happens before Spring Security's built-in authentication filter
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      ); //Disable session storage, making authentication fully stateless (JWT required for every request)

    return http.build();
  }

/**
 * Provides a PasswordEncoder bean that uses BCrypt hashing algorithm for encrypting passwords.
 * BCrypt is a strong hashing algorithm that automatically handles salting and is designed to be computationally intensive
 * to protect against brute-force attacks.
 * 
 * @return an instance of BCryptPasswordEncoder
 */

  @Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

/**
 * Configures CORS (Cross-Origin Resource Sharing) settings for the application.
 * This configuration allows the application to accept requests from specific origins,
 * enabling the frontend and backend to communicate securely across different domains.
 * 
 * The configuration allows credentials to be included in requests and specifies
 * which HTTP methods and headers are permitted.
 * 
 * Allowed origins:
 * - http://localhost:3000
 * - https://iv1201-recr-0486e0c122b1.herokuapp.com/
 * - https://recruitment.jontek.xyz/
 * 
 * Allowed methods: GET, POST
 * Allowed headers: Authorization, Content-Type
 * 
 * @return An instance of UrlBasedCorsConfigurationSource with the defined CORS settings.
 */

  @Bean
  UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(
      List.of(
        "http://localhost:3000",
        "https://iv1201-recr-0486e0c122b1.herokuapp.com/",
        "https://recruitment.jontek.xyz/"
      )
    );
    configuration.setAllowedMethods(List.of("GET", "POST"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
