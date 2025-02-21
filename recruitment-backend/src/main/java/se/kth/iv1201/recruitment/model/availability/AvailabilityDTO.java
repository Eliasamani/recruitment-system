package se.kth.iv1201.recruitment.model.availability;

import java.sql.Date;

public interface AvailabilityDTO {
    public long getId();

    public Date getFromDate();

    public Date getToDate();

}
