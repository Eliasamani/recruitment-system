package se.kth.iv1201.recruitment.model.jobApplication;

import java.util.List;

import se.kth.iv1201.recruitment.model.availability.AvailabilityDTO;
import se.kth.iv1201.recruitment.model.competence.Competence;
import se.kth.iv1201.recruitment.model.competence.CompetenceDTO;
import se.kth.iv1201.recruitment.model.person.PersonDTO;

/**
 * Defines the methods for accessing {@link Competence} outside of model and
 * service layers
 */
public interface JobApplicationDTO {
    public long getApplicationId();

    /**
     * @return the {@link PersonDTO} associated with this job application
     */
    public PersonDTO getPerson();

    /**
     * @return the {@link CompetenceDTO} competences associated with this job
     *         application
     */
    public List<CompetenceDTO> getCompetences();

    /**
     * @return the {@link AvailabilityDTO} availabilities associated with this job
     *         application
     */
    public List<AvailabilityDTO> getAvailabilities();

    /**
     * 
     * @return the {@link Status} specifying the decision taken by the recruiter
     *         related to this jobApplication.
     */
    public Status getStatus();

    public enum Status {
        UNDETERMINED, APPROVED, REJECTED
    }
}
