package se.kth.iv1201.recruitment.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;




/**
 * Object keeping the competence and the availibility of 
 * (both are lists) a person 
 * 
 */

 // TODO Create availibility entity
public class JobApplication {
 
    private int person_id;

    private List<Competence> competences;

    




}
