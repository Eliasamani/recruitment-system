package se.kth.iv1201.recruitment.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Set<Competence> competences = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
