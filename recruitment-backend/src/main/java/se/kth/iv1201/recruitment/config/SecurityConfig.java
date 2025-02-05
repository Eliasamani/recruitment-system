package se.kth.iv1201.recruitment.config;

import java.util.Arrays;
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
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfigurationSource;
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
        http.csrf(csrf->csrf.disable()).authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/api/session","/api/login", "/api/register","/index.html","/static/**","/*.png","/*.json","/*.ico","/*.txt","/error").permitAll() // ✅ Allow public access
            .requestMatchers("/api/protected").hasRole("RECRUITER")) // ✅ Ensure role is checked correctly
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, personRepository), UsernamePasswordAuthenticationFilter.class)            .formLogin(login -> login.loginPage("/")
            .permitAll()); // ✅ Pass repository
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
    /* 
    @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
    throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      )
      .authorizeHttpRequests(auth ->
        auth
          .requestMatchers(
            "/",
            "/api/login",
            "/api/register",
            "/index.html",
            "/static/**",
            "/*.png",
            "/*.json",
            "/*.ico",
            "/*.txt",
            "/error"
          )
          .permitAll()
          .requestMatchers("/api/protected")
          .hasRole("RECRUITER")
          .anyRequest()
          .authenticated()
      )
      .addFilterBefore(
        jwtAuthenticationFilter,
        UsernamePasswordAuthenticationFilter.class
      )
      .formLogin(login -> login.loginPage("/").permitAll());

    return http.build();
  }

  @Bean
  public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.setAllowedOrigins(List.of("http://localhost:8081")); // ✅ Allow React frontend
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
      source.registerCorsConfiguration("/**", config);
      return new CorsFilter(source);
  }
      */
}
