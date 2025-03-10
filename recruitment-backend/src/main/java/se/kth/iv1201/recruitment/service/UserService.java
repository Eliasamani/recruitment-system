package se.kth.iv1201.recruitment.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.exception.UserServiceException;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * Implements login/register related business logic.
 */
@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(
            UserService.class.getName());

    private final PersonRepository repository;

    public UserService(PersonRepository repository) {
        this.repository = repository;
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

        try {
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
        } catch (DataIntegrityViolationException e) {
            throw new UserServiceException(
                    "Database error while creating user: " + username + " Exception:" + e);
        } catch (Exception e) {
            throw new UserServiceException(
                    "Unexpected error during user creation: " + username + " Exception:" + e);
        }
    }

    /**
     * Searches for a person with the specified username.
     *
     * @param username The username of the person.
     * @return The found PersonDTO, or null if no user is found.
     */
    public PersonDTO findPerson(String username) {
        LOGGER.info("Searching for user: " + username);
        try {
            PersonDTO person = repository.findPersonByUsername(username);
            if (person == null) {
                LOGGER.warning("User not found: " + username);
            } else {
                LOGGER.info("User found: " + username);
            }
            return person;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user: " + username, e);
            return null;
        }
    }
}
