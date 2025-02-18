package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    @Query("FROM JobApplication e WHERE e.person.username = ?1")
    JobApplication findJobApplicationbyUsername(String username);

    @Override
    List<JobApplication> findAll();

}
