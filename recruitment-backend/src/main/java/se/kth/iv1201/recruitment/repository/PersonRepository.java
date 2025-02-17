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
@Transactional
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * 
     * @param username username of the Person
     * @return person with the specified username, if none null will be returned
     */
    <S extends Person> S findPersonByUsername(String username);
    <S extends Person> S findPersonByEmail(String email);

    @Override
    <S extends Person> S save(S entity);

}
