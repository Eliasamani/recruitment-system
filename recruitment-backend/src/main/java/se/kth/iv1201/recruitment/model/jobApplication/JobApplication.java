package se.kth.iv1201.recruitment.model.jobApplication;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import se.kth.iv1201.recruitment.model.availability.Availability;
import se.kth.iv1201.recruitment.model.availability.AvailabilityDTO;
import se.kth.iv1201.recruitment.model.competence.Competence;
import se.kth.iv1201.recruitment.model.competence.CompetenceDTO;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.person.PersonDTO;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_application")
public class JobApplication implements JobApplicationDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private long applicationId;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // deletions of a
                                                                                        // jobapplications should also
                                                                                        // delete
    // competences & avalibilies
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private List<Competence> competences = new ArrayList();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private List<Availability> availabilities = new ArrayList();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "application_status")
    private Status status;

    public PersonDTO getPerson() {
        return this.person;
    }

    public List<CompetenceDTO> getCompetences() {
        return (List<CompetenceDTO>) (List) this.competences;
    }

    public List<AvailabilityDTO> getAvailabilities() {
        return (List<AvailabilityDTO>) (List) this.availabilities;
    }

    public void addAllAvailabilities(List<Availability> availabilities) {
        this.availabilities.addAll(availabilities);
    }

    public void addAllCompetences(List<Competence> competences) {
        this.competences.addAll(competences);
    }

}
