package se.kth.iv1201.recruitment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.kth.iv1201.recruitment.model.Person;



@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    
  <S extends Person> S findPersonByUsername(String username);

  @Override
  <S extends Person> S save(S entity);

} 
