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

/*
 * Contains all database access operations for JobApplication 
 */

@Repository
@Transactional(propagation = Propagation.MANDATORY, rollbackFor = Exception.class)
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    /**
     * Returns the job application belonging to the user with the username, or null
     * if no such application exists
     * 
     * @deprecated use {@link #findByPerson()} instead
     * @param username username of the person job application belongs to
     * @return the job application with the Person that has the specified username
     *         or null if such a job application is not found
     */
    @Query("FROM JobApplication e WHERE e.person.username = ?1")
    JobApplication findJobApplicationbyUsername(String username);

    /**
     * Returns the job application belonging to the specified person, or null if no
     * such job application exists
     * 
     * @param person the person job application belongs to
     * @return the job application that has the specified Person
     *         or null if such a job application is not found
     */
    JobApplication findByPerson(Person person);

    @Modifying
    void deleteByPerson(Person person);

    /**
     * Fetches all job application data in the database without the competences and
     * availabilities included
     */
    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN FETCH ja.person p")
    List<JobApplication> findAll();
}
