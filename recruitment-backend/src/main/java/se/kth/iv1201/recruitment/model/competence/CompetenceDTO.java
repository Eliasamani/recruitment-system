package se.kth.iv1201.recruitment.model.competence;

/**
 * Defines the methods for accessing {@link Competence} outside of model and
 * service layers
 */
public interface CompetenceDTO {
    public long getId();

    /**
     * 
     * @return an integer 1-3 that corresponds to the type in the
     *         database table "competences"
     */
    public int getCompetenceType();

    /**
     * 
     * @return the years of experince within this competence
     */
    public float getExperience();
}
