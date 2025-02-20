package se.kth.iv1201.recruitment.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

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
import se.kth.iv1201.recruitment.model.person.Person;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_application")
public class JobApplication {

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
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Set<Availability> availabilities = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private Status status;

    public enum Status {
        UNDETERMINED, APPROVED, REJECTED
    }

    public void addAllAvailabilities(Set<Availability> availabilities) {
        this.availabilities.addAll(availabilities);
    }

    public void addAllCompetences(Set<Competence> competences) {
        this.competences.addAll(competences);
    }
}
