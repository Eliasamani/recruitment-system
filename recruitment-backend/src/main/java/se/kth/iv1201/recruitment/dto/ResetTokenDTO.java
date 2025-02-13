package se.kth.iv1201.recruitment.dto;



import java.util.Date;

import se.kth.iv1201.recruitment.model.Person;

public interface ResetTokenDTO {
    public long getResetTokenId();
    public Person getPerson();
    public int getResetToken();
    public Date getCreateTime();
}
