package se.kth.iv1201.recruitment.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.servlet.http.Cookie;
import org.springframework.mock.web.MockCookie;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.model.EditProfileForm;
import se.kth.iv1201.recruitment.model.exception.FieldAlreadyFilledException;
import se.kth.iv1201.recruitment.model.exception.InvalidSessionException;
import se.kth.iv1201.recruitment.model.exception.NoCookiesInRequestException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.security.JwtProvider;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PersonRepository repository;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Verifies that the createPerson method creates a person with the
     * specified parameters and that the created person is stored in the
     * database.
     */
    @Test
    void testCreatePerson() {
        String username = "UniqueUser" + System.currentTimeMillis();
        assertDoesNotThrow(() -> userService.createPerson(
                "First",
                "Last",
                "19991212-4444",
                "email@example.com",
                username,
                "password"));
        assertNotNull(repository.findPersonByUsername(username));
    }

    /**
     * Verifies that creating a person with an existing username throws
     * a UserAlreadyExistsException.
     */

    @Test
    void testCreateExistingPersonThrowsException() {
        String username = "TestRecruiter";
        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createPerson(
                        "First",
                        "Last",
                        "19991212-4444",
                        "email@example.com",
                        username,
                        "password"));
    }

    /**
     * Verifies that a user can be found by username.
     */
    @Test
    void testFindPerson() {
        PersonDTO person = userService.findPerson("TestRecruiter");
        assertNotNull(person);
        assertEquals("TestRecruiter", person.getUsername());
    }

    /**
     * Verifies that a null is returned when a person with the given username does
     * not exist.
     */
    @Test
    void testFindNonExistentPersonReturnsNull() {
        PersonDTO person = userService.findPerson("NonExistentUser");
        assertNull(person);
    }

    /**
     * Verifies that the editSelectedPerson method edits the selected person with
     * the
     * specified parameters and that the edited person is stored in the database.
     */
    @Test
    void testEditSelectedPersonWithAllFields() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setLastname("newLastName");
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setEmail("newEmail@example.com");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };
        PersonDTO person = userService.editSelectedPerson(editProfileForm, cookies);
        assertEquals("newFirstName", person.getFirstname());
        assertEquals("newLastName", person.getLastname());
        assertEquals("19991212-4444", person.getPersonNumber());
        assertEquals("newEmail@example.com", person.getEmail());
    }

    /**
     * Verifies that the editSelectedPerson method throws an InvalidSessionException
     * when there is not a username in the request
     */
    @Test
    void testEditSelectedPersonWithNoUserName() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setLastname("newLastName");
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setEmail("newEmail@example.com");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };

        assertThrows(InvalidSessionException.class, () -> userService.editSelectedPerson(editProfileForm, cookies));

    }

    /**
     * Verifies that the editSelectedPerson method throws an InvalidSessionException
     * When there are no cookies in the request
     */
    @Test
    void testEditSelectedPersonWithNoCookies() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setLastname("newLastName");
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setEmail("newEmail@example.com");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = null;
        assertThrows(NoCookiesInRequestException.class, () -> userService.editSelectedPerson(editProfileForm, cookies));

    }

    /**
     * Verifies that the editSelectedPerson method throws an InvalidSessionException
     * When there is no token in the cookies
     */
    @Test
    void testEditSelectedPersonWithNoToken() {
        EditProfileForm editProfileForm = new EditProfileForm();

        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setLastname("newLastName");
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setEmail("newEmail@example.com");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("NotJWT", "HELLO") };
        assertThrows(InvalidSessionException.class, () -> userService.editSelectedPerson(editProfileForm, cookies));
    }

    /**
     * Verifies that the editSelectedPerson method throws an InvalidSessionException
     * When the username in the request does not match the username in the token
     * meaning the user is truing to edit another user
     */
    @Test
    void testEditSelectedPersonWithInvalidUsername() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setLastname("newLastName");
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setEmail("newEmail@example.com");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("NotTestRecruiter")) };
        assertThrows(InvalidSessionException.class, () -> userService.editSelectedPerson(editProfileForm, cookies));
    }

    /**
     * Verifies that the editSelectedPerson method will edit the selected person
     * when only first name is provided
     * and that the edited person is stored in the database.
     */
    @Test
    void testEditSelectedPersonWithOnlyFirstName() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setFirstname("newFirstName");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };
        PersonDTO person = userService.editSelectedPerson(editProfileForm, cookies);
        assertEquals("newFirstName", person.getFirstname());
    }

    /**
     * Verifies that the editSelectedPerson method will edit the selected person
     * when only last name is provided
     * and that the edited person is stored in the database.
     * 
     */
    @Test
    void testEditSelectedPersonWithOnlyLastName() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setLastname("newLastName");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };
        PersonDTO person = userService.editSelectedPerson(editProfileForm, cookies);
        assertEquals("newLastName", person.getLastname());
    }

    /**
     * Verifies that the editSelectedPerson method will edit the selected person
     * when only person number is provided
     * and that the edited person is stored in the database.
     */
    @Test
    void testEditSelectedPersonWithOnlyPersonNumber() {
        EditProfileForm editProfileForm = new EditProfileForm();

        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };
        PersonDTO person = userService.editSelectedPerson(editProfileForm, cookies);
        assertEquals("19991212-4444", person.getPersonNumber());
    }

    /**
     * Verifies that the editSelectedPerson method will edit the selected person
     * when only email is provided
     * and that the edited person is stored in the database.
     * 
     */
    @Test
    void testEditSelectedPersonWithOnlyEmail() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setEmail("newEmail@example.com");
        editProfileForm.setUsername("TestRecruiter");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestRecruiter")) };
        PersonDTO person = userService.editSelectedPerson(editProfileForm, cookies);
        assertEquals("newEmail@example.com", person.getEmail());
    }

    /**
     * Verifies that the editSelectedPerson method will throw a
     * FieldAlreadyFilledException
     * when the person number is already filled to prevent the user from changing
     * their person number
     */
    @Test
    void testEditSelectedPersonWithAlreadyFilledPersonNumber() {
        EditProfileForm editProfileForm = new EditProfileForm();
        editProfileForm.setPersonNumber("19991212-4444");
        editProfileForm.setUsername("TestApplicant");
        Cookie[] cookies = { new Cookie("jwt", jwtProvider.generateToken("TestApplicant")) };
        assertThrows(FieldAlreadyFilledException.class, () -> userService.editSelectedPerson(editProfileForm, cookies));
    }

}
