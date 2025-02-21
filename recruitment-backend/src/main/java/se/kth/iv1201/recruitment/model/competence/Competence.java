package se.kth.iv1201.recruitment.model.competence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "competence_profile")
public class Competence implements CompetenceDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_profile_id")
    private long id;

    // @Column(name = "person_id")
    // private long person_id;

    @Column(name = "competence_id")
    private int competenceType;

    @Column(name = "years_of_experience")
    private float experience;

    public Competence(int competenceType, float experience) {
        this.competenceType = competenceType;
        this.experience = experience;
    }

}
