package se.kth.iv1201.recruitment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import se.kth.iv1201.recruitment.model.Availability;
import se.kth.iv1201.recruitment.model.Competence;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.repository.AvailabilityRepository;
import se.kth.iv1201.recruitment.repository.CompetenceRepository;

@Service
@AllArgsConstructor

public class JobApplicationService {
    
    @Autowired
    private final CompetenceRepository competenceRepository;

    @Autowired
    private final AvailabilityRepository availabilityRepository;


    public JobApplication findJobApplicationByPersonId(long id) throws Exception{
        List<Competence> competences = competenceRepository.findCompetencesByPersonId(id); 
        List<Availability> availabilities = availabilityRepository.findAvailabilitiesByPersonId(id);
        if(competences.isEmpty() || availabilities.isEmpty()){
            throw new Exception("Either competence or availibilites is empty");
        }

        return new JobApplication(id,competences,availabilities);
    }

    public void saveApplication(JobApplication application) throws Exception{
        List<Competence> competences = competenceRepository.findCompetencesByPersonId(application.getPerson_id()); 
        List<Availability> availabilities = availabilityRepository.findAvailabilitiesByPersonId(application.getPerson_id());
        if(competences.isEmpty() || availabilities.isEmpty()){
            competenceRepository.saveAll(application.getCompetences());
            availabilityRepository.saveAll(application.getAvailabilities());
        }
        else{
            throw new Exception("Either competence oravailibilites is empty");
        }
    }

}
