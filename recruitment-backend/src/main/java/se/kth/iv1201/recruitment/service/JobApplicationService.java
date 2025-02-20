package se.kth.iv1201.recruitment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;

import se.kth.iv1201.recruitment.repository.JobApplicationRepository;

@Service
@AllArgsConstructor

public class JobApplicationService {

    @Autowired
    private final JobApplicationRepository applicationRepository;

    public JobApplication findJobApplicationbyUsername(String username) throws Exception {

        return applicationRepository.findJobApplicationbyUsername(username);
    }

    public List<JobApplication> getAllApplications() {
        return applicationRepository.findAll();
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

}
