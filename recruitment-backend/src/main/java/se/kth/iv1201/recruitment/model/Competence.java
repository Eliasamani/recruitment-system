package se.kth.iv1201.recruitment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@Entity
@Table(name="competence_profile")
public class Competence {
    @Id
    @Column(name="competence_profile_id")
    private long id;

    @Column(name="person_id")
    private long person_id;


    @Column(name="competence_id")
    private int competence_type;

    @Column(name="years_of_experience")
    private float experience;
}
