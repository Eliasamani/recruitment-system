package se.kth.iv1201.recruitment.service;

import org.springframework.beans.factory.annotation.Autowired;

import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.PersonDTO;
import se.kth.iv1201.recruitment.repository.PersonRepository;

/**
 * implement login/register related bussiness logic here
 */
public class LoginRegisterService {
    
    @Autowired
    private PersonRepository repository;
    // we may want to divide the person into two: user + personal info to simplify these interfaces?
    public PersonDTO createPerson(String firstname, String lastname,  String personNum, String email, String username, String password) throws Exception{
        if(repository.findPersonByUsername(username) != null){
            throw new Exception("User already Exists"); //TODO Create Specific exception for this
        }
        return repository.save(new Person(firstname,lastname, personNum, email, username, password));
    }
    

   /**
    * Searches for person with the specified username
    * @param username persons username
    * @return person with specified username, is null if not found
    */
    public PersonDTO findPerson(String username){
       return repository.findPersonByUsername(username); 
    }

}
