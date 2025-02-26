package se.kth.iv1201.recruitment.model.availability;

import java.sql.Date;

/**
 * Defines the methods for accessing {@link Availability} outside of model and
 * service layers
 */
public interface AvailabilityDTO {
    /**
     * @return id of the object within the database
     */
    public long getId();

    /**
     * @return the starting date for the applicants availibilty period
     */
    public Date getFromDate();

    /**
     * @return the end date for the applicants availibilty period
     */
    public Date getToDate();

}
