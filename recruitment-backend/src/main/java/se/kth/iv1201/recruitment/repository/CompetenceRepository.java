package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import se.kth.iv1201.recruitment.model.Competence;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long> {

    
    List<Competence> findCompetencesByPersonID(long person_id);
}
