package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query("FROM JobApplication e WHERE e.person.username = ?1")
    JobApplication findJobApplicationbyUsername(String username);

    JobApplication findByPerson(Person person);

    @Modifying
    void deleteByPerson(Person person);

    @Override
    List<JobApplication> findAll();

}
