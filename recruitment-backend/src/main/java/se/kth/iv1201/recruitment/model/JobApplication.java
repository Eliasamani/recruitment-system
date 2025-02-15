package se.kth.iv1201.recruitment.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
 * Object keeping the competence and the availibility of 
 * (both are lists) a person with a person_id
 * 
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor


// TODO Create availibility entity
public class JobApplication {
    private long person_id;
    private List<Competence> competences;
    private List<Availability> availabilities;



    
}


