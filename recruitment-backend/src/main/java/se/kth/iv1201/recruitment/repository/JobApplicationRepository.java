package se.kth.iv1201.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import se.kth.iv1201.recruitment.model.jobApplication.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;

import java.util.List;

@Repository
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query("FROM JobApplication e WHERE e.person.username = ?1")
    JobApplication findJobApplicationbyUsername(String username);

    JobApplication findByPerson(Person person);

    @Modifying
    void deleteByPerson(Person person);

    // Fetch only basic application data (without competences & availabilities)
    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN FETCH ja.person p")
    List<JobApplication> findAll();
}
