package se.kth.iv1201.recruitment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

import java.util.Optional;

/**
 * Implements login/register related business logic.
 */
@Service
public class LoginRegisterService {

    @Autowired
    private PersonRepository repository;

    /**
     * Creates a new person (user) if the username is not already taken.
     *
     * @param firstname  The first name of the person.
     * @param lastname   The last name of the person.
     * @param personNum  The personal number of the person.
     * @param email      The email of the person.
     * @param username   The username of the person.
     * @param password   The password of the person.
     * @return The created PersonDTO object.
     * @throws Exception If the user already exists.
     */
    public PersonDTO createPerson(String firstname, String lastname, String personNum, 
                                  String email, String username, String password) throws Exception {

        // ✅ Use Optional to check if the username exists
        if (repository.findPersonByUsername(username) != null) {
            throw new Exception("User already exists"); // TODO: Replace with a specific exception
        }

        // ✅ Create and save the new person
        Person newPerson = new Person(firstname, lastname, personNum, email, username, password);
        return repository.save(newPerson);
    }

    /**
     * Searches for a person with the specified username.
     *
     * @param username The username of the person.
     * @return The found PersonDTO, or null if no user is found.
     */
    public PersonDTO findPerson(String username) {
        return repository.findPersonByUsername(username); // ✅ Return the PersonDTO or null if not found
    }
}
