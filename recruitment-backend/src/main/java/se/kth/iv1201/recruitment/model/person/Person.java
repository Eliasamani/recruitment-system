package se.kth.iv1201.recruitment.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "person")
public class Person implements PersonDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private long id;

    @Column(name = "name")
    private String firstname;

    @Column(name = "surname")
    private String lastname;

    @Column(name = "pnr", unique = true)
    private String personNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "role_id")
    private roles role;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    /**
     * Creates a new instance wiht specified params. Note that any
     * new instance created will have role APPLICANT.
     * 
     * Person number and username must be unique for
     * each person persisted the database.
     * 
     * @param firstname
     * @param lastname
     * @param personNum
     * @param email
     * @param username
     * @param password
     */
    public Person(String firstname, String lastname, String personNum, String email, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.personNumber = personNum;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = roles.APPLICANT; // set role to applicant
    }

}
