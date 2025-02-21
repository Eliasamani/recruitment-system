package se.kth.iv1201.recruitment.model.jobApplication;

import java.util.List;

import se.kth.iv1201.recruitment.model.availability.AvailabilityDTO;
import se.kth.iv1201.recruitment.model.competence.CompetenceDTO;
import se.kth.iv1201.recruitment.model.person.PersonDTO;

public interface JobApplicationDTO {
    public long getApplicationId();

    public PersonDTO getPerson();

    public List<CompetenceDTO> getCompetences();

    public List<AvailabilityDTO> getAvailabilities();

    public Status getStatus();

    public enum Status {
        UNDETERMINED, APPROVED, REJECTED
    }
}
