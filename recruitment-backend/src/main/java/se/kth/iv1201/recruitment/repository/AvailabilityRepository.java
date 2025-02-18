package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    @Query("FROM Availability e WHERE e.person_id = ?1")
    List<Availability> findAvailabilitiesByPersonId(long person_id);
}
