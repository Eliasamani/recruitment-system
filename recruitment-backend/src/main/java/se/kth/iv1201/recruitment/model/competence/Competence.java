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

/**
 * represents the competences of the applicant
 */
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

    @Column(name = "competence_id")
    private int competenceType; // FIXME change to get either as enum or fetch directly from db

    @Column(name = "years_of_experience")
    private float experience;

    /**
     * Creates a new instance wiht specified params.
     * 
     * @param competenceType an integer 1-3 that corresponds to the type in the
     *                       database table "competences"
     * @param experience     years of experince within competence area
     */
    public Competence(int competenceType, float experience) {
        this.competenceType = competenceType;
        this.experience = experience;
    }

}
