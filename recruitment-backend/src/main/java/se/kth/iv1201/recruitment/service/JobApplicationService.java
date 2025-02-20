package se.kth.iv1201.recruitment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.repository.JobApplicationRepository;

import java.util.List;

@Service
@Transactional
public class JobApplicationService {

    @Autowired
    private final CompetenceRepository competenceRepository;

    @Autowired
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    private final JobApplicationRepository applicationRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
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

    public JobApplication findApplicationById(long id) {
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