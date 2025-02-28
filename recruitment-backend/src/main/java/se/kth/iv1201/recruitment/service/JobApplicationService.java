package se.kth.iv1201.recruitment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.availability.Availability;
import se.kth.iv1201.recruitment.model.competence.Competence;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplication;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplicationDTO;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.repository.JobApplicationRepository;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class JobApplicationService {

    @Autowired
    private final JobApplicationRepository applicationRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * method for getting all the job applications in the system's database
     * 
     * @return a list of all existing job applications
     */
    public List<JobApplicationDTO> getAllApplications() {
        return (List<JobApplicationDTO>) (List) applicationRepository.findAll();
    }

    /**
     * saves a new job application in to the database
     * 
     * @param person         the person that the job application belongs to
     * @param competences    a list of {@link Competence} competences of the
     *                       applicant
     * @param availabilities a list of {@link Availability} availibilites of the
     *                       applicant
     * @return
     * @throws Exception thrown when the provided person already has a job
     *                   application in the system
     */
    public JobApplication saveApplication(Person person, List<Competence> competences,
            List<Availability> availabilities) throws Exception {
        if (applicationRepository.findByPerson(person) != null) {
            throw new Exception("Application Already Exist");
        }
        JobApplication application = new JobApplication();
        application.setPerson(person);
        application.setAvailabilities(availabilities);
        application.setCompetences(competences);
        application.setStatus(JobApplication.Status.UNDETERMINED);

        return applicationRepository.save(application);
    }

    /**
     * fetches the job application of the given id
     * 
     * @param id the id of the job applicaiton in the database
     * @return {@link JobApplicationDTO} with the given id
     */
    public JobApplicationDTO findApplicationById(long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    /**
     * Updates the {@link JobApplication.Status} of the job application with the
     * given id
     * 
     * @param applicationId id of the job application to be updated
     * @param status        status to be set by the update
     */
    public void updateApplicationStatus(long applicationId, JobApplication.Status status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setStatus(status);
        applicationRepository.save(application);
    }
}