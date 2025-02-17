package se.kth.iv1201.recruitment.service;

import java.security.SecureRandom;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.person.Person;
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


    public ResetService(ResetTokenRepository resetTokenRepository,PersonRepository personRepository ){
        this.resetTokenRepository = resetTokenRepository;
        this.personRepository = personRepository;

    }

    public ResetTokenDTO generateToken(String email) {
        Person emailPerson = personRepository.findPersonByEmail(email);
        if (emailPerson == null){
            return null;
        }
        else {
        SecureRandom random = new SecureRandom();
        ResetToken newToken = new ResetToken(emailPerson, random.nextInt(100000, 1000000));
        ResetTokenDTO savedToken = resetTokenRepository.save(newToken);
        return savedToken;
    
    }


    }



}
