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
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

import jakarta.servlet.http.Cookie;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationFilter.class.getName());
    private final JwtUtil jwtUtil;
    private final PersonRepository personRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, PersonRepository personRepository) {
        this.jwtUtil = jwtUtil;
        this.personRepository = personRepository;
    }

    /**
     * Checks if the request contains a valid JWT token, and if so, sets the authentication context according to role.
     * If no JWT token is present, or if the token is invalid, the request is passed to the next filter in the chain
     * without setting the authentication context.
     * @param request The request being processed
     * @param response The response being constructed
     * @param filterChain The filter chain to pass the request to if the token is invalid or not present
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        LOGGER.info("JwtAuthenticationFilter: Request received for " + request.getRequestURI());

        String token = null;

        // Retrieve JWT from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtUtil.validateToken(token);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");
            return;
        }

            // Fetch user details and set authentication context
        Person person = personRepository.findPersonByUsername(username);
        if (person == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
            return;
        }
        
        String role = switch (person.getRoleType()) {
            case PersonDTO.roles.RECRUITER -> "ROLE_RECRUITER";
            case PersonDTO.roles.APPLICANT -> "ROLE_USER";
            default -> "ROLE_DEFAULT";
        };

        User principal = new User(username, "", Collections.singletonList(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
