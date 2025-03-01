package se.kth.iv1201.recruitment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import se.kth.iv1201.recruitment.config.SecurityConfig;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.service.UserService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(
            JwtAuthenticationFilter.class.getName());
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public JwtAuthenticationFilter(
            JwtProvider jwtProvider,
            UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return SecurityConfig.PUBLIC_ENDPOINTS.contains(path);
    }

    /**
     * Checks if the request contains a valid JWT, and if so, sets the
     * authentication context according to role.
     * If no JWT is present, or if the token is invalid, the request is passed to
     * the next filter in the chain
     * without setting the authentication context.
     * 
     * @param request     The request being processed
     * @param response    The response being constructed
     * @param filterChain The filter chain to pass the request to if the token is
     *                    invalid or not present
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Request received for: " + request.getRequestURI());

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
            LOGGER.warning("No JWT found in request");
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtProvider.validateToken(token);
        if (username == null) {
            LOGGER.warning("Invalid JWT provided");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT");
            return;
        }

        // Fetch user details and set authentication context
        PersonDTO person = userService.findPerson(username);
        if (person == null) {
            LOGGER.warning("Authentication failed - User not found: " + username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
            return;
        }

        String role = switch (person.getRole()) {
            case PersonDTO.roles.RECRUITER -> "ROLE_RECRUITER";
            case PersonDTO.roles.APPLICANT -> "ROLE_USER";
            default -> "ROLE_DEFAULT";
        };

        LOGGER.info(
                "Authentication successful for user: " + username + " with role: " + role);

        User principal = new User(
                username,
                "",
                Collections.singletonList(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
