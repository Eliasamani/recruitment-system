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

// TODO use the DTOs here and in controller

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class JobApplicationService {

    @Autowired
    private final JobApplicationRepository applicationRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<JobApplicationDTO> getAllApplications() {
        return (List<JobApplicationDTO>) (List) applicationRepository.findAll();
    }

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

    // add method for saving an jobapplication given avalibilites & competences

    public JobApplicationDTO findApplicationById(long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }

    public void updateApplicationStatus(long applicationId, JobApplication.Status status) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        application.setStatus(status);
        applicationRepository.save(application);
    }
}