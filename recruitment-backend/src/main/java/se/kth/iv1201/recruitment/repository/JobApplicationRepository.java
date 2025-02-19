package se.kth.iv1201.recruitment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.kth.iv1201.recruitment.model.JobApplication;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    // Fetch only basic application data (without competences & availabilities)
    @Query("SELECT ja FROM JobApplication ja " +
            "JOIN FETCH ja.person p")
    List<JobApplication> findAll();
}
