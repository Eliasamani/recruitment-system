package se.kth.iv1201.recruitment.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.kth.iv1201.recruitment.model.UserPassResetForm;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.IncorrectResetCodeException;
import se.kth.iv1201.recruitment.model.exception.NonExistingEmailException;
import se.kth.iv1201.recruitment.model.exception.UserAlreadyExistsException;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.person.PersonDTO;
import se.kth.iv1201.recruitment.model.resettoken.ResetToken;
import se.kth.iv1201.recruitment.model.resettoken.ResetTokenDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;
import se.kth.iv1201.recruitment.repository.ResetTokenRepository;

@Service
@Transactional(rollbackFor = Exception.class)
public class ResetService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final ResetTokenRepository resetTokenRepository;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public ResetService(ResetTokenRepository resetTokenRepository, PersonRepository personRepository,
            PasswordEncoder passwordEncoder, JavaMailSender javaMailSender) {
        this.resetTokenRepository = resetTokenRepository;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;

    }

    public ResetTokenDTO generateToken(String email) {

        Person emailPerson = personRepository.findPersonByEmail(email);
        if (emailPerson == null) {
            throw new NonExistingEmailException("email: " + email + " did not exist");
        } else {
            List<ResetToken> pastTokens = resetTokenRepository.findResetTokenByPersonAndValidTrue(emailPerson);
            if (pastTokens != null) {
                for (ResetToken pastToken : pastTokens) {
                    pastToken.setValid(false);
                    resetTokenRepository.save(pastToken);
                }
            }
            LOGGER.info("Unvlaidated all reset tokens for " + emailPerson);
            SecureRandom random = new SecureRandom();
            ResetToken newToken = new ResetToken(emailPerson, random.nextInt(100000, 1000000));
            ResetTokenDTO savedToken = resetTokenRepository.save(newToken);
            LOGGER.info("Created new token for " + emailPerson);
            return savedToken;

        }

    }

    public PersonDTO resetUsernameAndPassword(UserPassResetForm userPassResetForm) {
        Person personToEdit = personRepository.findPersonByEmail(userPassResetForm.getEmail());
        if (personToEdit == null) {
            throw new NonExistingEmailException("email: " + userPassResetForm.getEmail() + " did not exist");
        }

        ResetToken usedToken = resetTokenRepository.findResetTokenByPersonAndResetToken(personToEdit,
                Integer.valueOf(userPassResetForm.getCode()));

        if (usedToken != null && usedToken.getValid()
                && Duration.between(usedToken.getCreateTime(), Instant.now()).getSeconds() <= 900) {

            if (!(userPassResetForm.getUsername() == null || userPassResetForm.getUsername() == "")) {
                if (personRepository.findPersonByUsername(userPassResetForm.getUsername()) != null) {
                    LOGGER.warning(
                            "Username reset failed - Username already exists: " + userPassResetForm.getUsername());
                    throw new UserAlreadyExistsException("User already exists");
                }
                personToEdit.setUsername(userPassResetForm.getUsername());
                LOGGER.info("Changed " + personToEdit + " username");
            }
            if (!(userPassResetForm.getPassword() == null || userPassResetForm.getPassword() == "")) {
                personToEdit.setPassword(passwordEncoder.encode(userPassResetForm.getPassword()));
                LOGGER.info("Changed " + personToEdit + " password");
            }
            usedToken.setValid(false);
            resetTokenRepository.save(usedToken);
            PersonDTO savedPerson = personRepository.save(personToEdit);
            return savedPerson;
        } else {
            if (usedToken == null) {
                throw new IncorrectResetCodeException("Entered code was not found");
            }
            if (!usedToken.getValid()) {
                throw new IncorrectResetCodeException("Entered code was not valid");
            }
            if (!(Duration.between(usedToken.getCreateTime(), Instant.now()).getSeconds() <= 900)) {
                throw new IncorrectResetCodeException("Entered code has expired");
            } else {
                throw new IncorrectResetCodeException();
            }
        }

    }

    /**
     * Sends an email
     * 
     * @param email the specified email address
     * @param code  A ResetTokenDTO containing the reset code to be sent
     */
    public void sendMail(String email, ResetTokenDTO code) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        ResetToken codeinfo = resetTokenRepository.findAllById(List.of(code.getResetTokenId())).get(0);
        Person codePerson = codeinfo.getPerson();
        simpleMailMessage.setFrom(this.sender);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setText(
                "Hello " + codePerson.getFirstname() + "(" + codePerson.getUsername() + ")" + codePerson.getLastname()
                        + "\nYour code for resetting your username or password for the recruitment app is: "
                        + String.valueOf(code.getResetToken()));
        simpleMailMessage.setSubject("Recruitment App Reset Code");

        javaMailSender.send(simpleMailMessage);
    }

}
