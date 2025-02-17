package se.kth.iv1201.recruitment.model.resettoken;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import se.kth.iv1201.recruitment.model.person.Person;

@Entity
@Table(name = "reset_token")
public class ResetToken implements ResetTokenDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_token_id")
    private long resetTokenId;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "reset_token")
    private int resetToken;

    @CreationTimestamp
    @Column(name = "create_time")
    private Date createTime = new Date();

    public long getResetTokenId() {
        return resetTokenId;
    }

    public void setResetTokenId(long resetTokenId) {
        this.resetTokenId = resetTokenId;
    }

    public ResetToken(Person person, int resetToken) {
        this.person = person;
        this.resetToken = resetToken;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getResetToken() {
        return resetToken;
    }

    public void setResetToken(int resetToken) {
        this.resetToken = resetToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public String toString() {
        return "Reset Token [id=" + resetTokenId + ", person=" + person.getId() + ", resetToken=" + resetTokenId
                + ", createTime=" + createTime + "]";
    }

}
