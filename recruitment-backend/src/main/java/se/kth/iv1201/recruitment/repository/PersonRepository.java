package se.kth.iv1201.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.person.Person;

/*
 * Does all database access operations for Person 
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY,rollbackFor = Exception.class)
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * 
     * @param username username of the Person
     * @return person with the specified username, if none null will be returned
     */
    <S extends Person> S findPersonByUsername(String username);

    /**
     *
     * @param email email of the person to be found
     * @return the person that the email is linked to, if none null will be returned
     */
    <S extends Person> S findPersonByEmail(String email);

    @Override
    <S extends Person> S save(S entity);

}
