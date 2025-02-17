package se.kth.iv1201.recruitment.model.person;


/**
 * Defines the methods for accessing Person outside of model and service layers
 */
public interface PersonDTO {
    public long getId();
    public String getFirstname();
    public String getLastname();
    public String getPersonNum();
    public String getEmail();
    public String getPassword();
    public int getRole();
    public roles getRoleType();
    public String getUsername();


    public enum roles {
        NO_ROLE,
        RECRUITER,
        APPLICANT
      }
}
