package se.kth.iv1201.recruitment.model.person;

/**
 * Defines the methods for accessing {@link Person} outside of model and service
 * layers
 */
public interface PersonDTO {
    /**
     * @return id of the object within the database
     */
    public long getId();

    public String getFirstname();

    public String getLastname();

    /**
     * @return the person's personnumber. This number is unique for each person in
     *         the system
     */
    public String getPersonNumber();

    public String getEmail();

    public String getPassword();

    /**
     * @return the role of the person.
     */
    public roles getRole();

    /**
     * @return username of person. This is unique for every person in the system.
     */
    public String getUsername();

    /**
     * possible roles in the system. The ordering of these
     * enums are important as the ordinals are used to fetch and persist these
     * roles from database.
     */
    public enum roles {
        NO_ROLE,
        RECRUITER,
        APPLICANT
    }
}
