package se.kth.iv1201.recruitment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name ="person")
public class Person implements PersonDTO{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="person_id")
    private long id;

    @Column(name="name")
    private String firstname;

    @Column(name="surname")
    private String lastname;

    @Column(name="pnr",unique = true)
    private String personNum;

    @Column(name="email")
    private String email;

    @Column(name="role_id")
    private int role;

    @Column(name="username", unique = true)
    private String username;

    @Column(name="password")
    private String password;


    /**
     * JPA needs this, not for use  
     */
    protected Person(){

    }


    public Person(String firstname, String lastname, String personNum, String email, String password, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.personNum = personNum;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = 1; // set role to applicant
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getFirstname() {
        return firstname;
    }


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }


    public String getLastname() {
        return lastname;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getPersonNum() {
        return personNum;
    }


    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public int getRole() {
        return role;
    }


    public void setRole(int role) {
        this.role = role;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String toString() {
        return "User [id=" + id + ", firstname=" + firstname + ", lastname=" + lastname + ", personNum=" + personNum
                + ", email=" + email + ", password=" + password + ", role=" + role + ", username=" + username + "]";
    }
    
    
    

}
