package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.kth.iv1201.recruitment.model.Competence;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long> {

@Query("FROM Competence e WHERE e.person_id = ?1")
    List<Competence> findCompetencesByPersonID(long person_id);
}
