package se.kth.iv1201.recruitment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;
import se.kth.iv1201.recruitment.repository.JobApplicationRepository;

@Service
@AllArgsConstructor

public class JobApplicationService {

    @Autowired
    private final CompetenceRepository competenceRepository;

    @Autowired
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    private final JobApplicationRepository applicationRepository;

    public JobApplication findJobApplicationbyUsername(String username) throws Exception {

        return applicationRepository.findJobApplicationbyUsername(username);
    }

    public List<JobApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

}
