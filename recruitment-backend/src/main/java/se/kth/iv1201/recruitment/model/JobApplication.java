package se.kth.iv1201.recruitment.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Object keeping the competence and the availibility of
 * (both are lists) a person with a person_id
 * 
 */

enum applicationStatus {
    UNDETERMINED,
    APPROVED,
    REJECTED
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
// TODO DTOs for this class and each class within this to allow better
// encapsulation

@Entity
@Table(name = "application_status")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_status_id")
    private long status_id;

    // @Column(name = "person_id")
    // private long applicant_person_id;

    @OneToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;

    @OneToMany()
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private List<Competence> competences = new ArrayList();

    @OneToMany()
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private List<Availability> availabilities = new ArrayList();
    // private applicationStatus status = applicationStatus.UNDETERMINED;

    public void addAllAvailabilities(Set<Availability> availabilities) {
        this.availabilities.addAll(availabilities);
    }

    public void addAllCompetences(Set<Competence> competences) {
        this.competences.addAll(competences);
    }
}

// TODO CREATE JOB_APPLICATION_STATUS TABLE
// -> Use it as the table for JobApplication entity
// -> join person, competences and availibilites on this..
// check existing db on rwpassword branch line 102 -122
