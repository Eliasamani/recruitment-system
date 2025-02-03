package se.kth.iv1201.recruitment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;
import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.repository.PersonRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Logger;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtUtil jwtUtil;
    private final PersonRepository personRepository; // ✅ Inject repository to fetch user

    public JwtAuthenticationFilter(JwtUtil jwtUtil, PersonRepository personRepository) {
        this.jwtUtil = jwtUtil;
        this.personRepository = personRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info("JwtAuthenticationFilter: Request received for " + request.getRequestURI());

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            LOGGER.warning("JwtAuthenticationFilter: No JWT token found in request");
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7); // Remove "Bearer " prefix
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            LOGGER.warning("JwtAuthenticationFilter: Invalid JWT token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            return;
        }

        LOGGER.info("JwtAuthenticationFilter: Authenticated user - " + username);

        // Fetch role_id from database
        Optional<Person> optionalPerson = personRepository.findPersonByUsername(username);

        if (optionalPerson.isEmpty()) {
            LOGGER.warning("JwtAuthenticationFilter: User not found in database!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
            return;
        }

        Person person = optionalPerson.get();
        int roleId = person.getRole(); // Get role_id from database

        // Map role_id to Spring Security role
        String role = switch (roleId) {
            case 1 -> "ROLE_RECRUITER";
            case 2 -> "ROLE_USER";
            default -> "ROLE_DEFAULT"; // Fallback role
        };

        LOGGER.info("JwtAuthenticationFilter: Assigned role - " + role);

        User principal = new User(username, "", Collections.singletonList(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
