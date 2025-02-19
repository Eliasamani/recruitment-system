package se.kth.iv1201.recruitment.model.resettoken;

import java.time.Instant;

import se.kth.iv1201.recruitment.model.person.Person;;

public interface ResetTokenDTO {
    public long getResetTokenId();

    public Person getPerson();

    public int getResetToken();

    public Instant getCreateTime();

    public Boolean getValid();
}
