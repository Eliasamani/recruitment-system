package se.kth.iv1201.recruitment.service;

import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;
import se.kth.iv1201.recruitment.model.EditProfileForm;
import se.kth.iv1201.recruitment.model.exception.FieldAlreadyFilledException;
import se.kth.iv1201.recruitment.model.exception.InvalidSessionException;
import se.kth.iv1201.recruitment.model.exception.NoCookiesInRequestException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtProvider;

/**
 * Implements user related business logic.
 */
@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(
            UserService.class.getName());

    private final PersonRepository repository;
    private final JwtProvider jwtProvider;

    public UserService(PersonRepository repository, JwtProvider jwtProvider) {
        this.repository = repository;
        this.jwtProvider = jwtProvider;

    }

    /**
     * Creates a new person (user) if the username is not already taken.
     *
     * @param firstname The first name of the person.
     * @param lastname  The last name of the person.
     * @param personNum The personal number of the person.
     * @param email     The email of the person.
     * @param username  The username of the person.
     * @param password  The password of the person.
     * @return The created PersonDTO object.
     */
    public PersonDTO createPerson(
            String firstname,
            String lastname,
            String personNum,
            String email,
            String username,
            String password) {
        LOGGER.info("Attempting to create user: " + username);

        if (repository.findPersonByUsername(username) != null) {
            throw new UserAlreadyExistsException("User creation failed - Username already exists: " + username);
        }

        Person newPerson = new Person(
                firstname,
                lastname,
                personNum,
                email,
                username,
                password);
        PersonDTO savedPerson = repository.save(newPerson);
        LOGGER.info("User created successfully: " + username);
        return savedPerson;

    }

    /**
     * Searches for a person with the specified username.
     *
     * @param username The username of the person.
     * @return The found PersonDTO, or null if no user is found.
     */
    public PersonDTO findPerson(String username) {
        LOGGER.info("Searching for user: " + username);

        PersonDTO person = repository.findPersonByUsername(username);
        if (person == null) {
            LOGGER.warning("User not found: " + username);
        } else {
            LOGGER.info("User found: " + username);
        }
        return person;
    }


    /**
     * Edits the selected person with the information provided in the {@link EditProfileForm}.
     * @param editProfileForm The form containing the information to edit.
     * @param cookies The cookies from the request used to validate the user is the same as the edited user.
     * @return the updated person info
     */
    public PersonDTO editSelectedPerson(EditProfileForm editProfileForm, Cookie[] cookies) {
        if (cookies == null) {
            throw new NoCookiesInRequestException("User edit failed - No token found");
        }

        String token = null;
        for (Cookie cookie : cookies) {
            if ("jwt".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) {
            throw new InvalidSessionException("User edit failed - No token found");
        }
        String usernameToEdit = editProfileForm.getUsername();
        if (usernameToEdit == null || !usernameToEdit.equals(jwtProvider.validateToken(token))) {
            throw new InvalidSessionException("User edit failed - Logged in user did not match edited user");
        }
        Person personToEdit = repository.findPersonByUsername(usernameToEdit);
        if (!(editProfileForm.getFirstname() == null || editProfileForm.getFirstname() == "")) {
            personToEdit.setFirstname(editProfileForm.getFirstname());
            LOGGER.info("Changed " + personToEdit + " first name");
        }
        if (!(editProfileForm.getLastname() == null || editProfileForm.getLastname() == "")) {
            personToEdit.setLastname(editProfileForm.getLastname());
            LOGGER.info("Changed " + personToEdit + " last name");
        }
        if (!(editProfileForm.getPersonNumber() == null || editProfileForm.getPersonNumber() == "")) {
            if (personToEdit.getPersonNumber() != null) {
                throw new FieldAlreadyFilledException("Person:" + personToEdit + " already has a person number");
            }
            personToEdit.setPersonNumber(editProfileForm.getPersonNumber());

        }
        if (!(editProfileForm.getEmail() == null || editProfileForm.getEmail() == "")) {
            personToEdit.setEmail(editProfileForm.getEmail());
            LOGGER.info("Changed " + personToEdit + " email");
        }
        PersonDTO savedPerson = repository.save(personToEdit);
        return savedPerson;
    }
}
