package se.kth.iv1201.recruitment.model.resettoken;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import se.kth.iv1201.recruitment.model.person.Person;

@Getter
@Setter
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
    private Instant createTime;

    @Column(name = "valid")
    private Boolean valid;

    /**
     * Needed by JPA, never actually used
     */

    protected ResetToken() {
    }

    /**
     * Creates a new reset token where the token is automatically valid, and its
     * time is set to the current time.
     * 
     * @param person     The person the reset token should be linked with
     * @param resetToken An actual token which the person gets sent to their mail
     */
    public ResetToken(Person person, int resetToken) {
        this.person = person;
        this.resetToken = resetToken;
        this.createTime = Instant.now();
        this.valid = true;
    }

    @Override
    public String toString() {
        return "ResetToken [resetTokenId=" + resetTokenId + ", person=" + person + ", resetToken=" + resetToken
                + ", createTime=" + createTime + ", valid=" + valid + "]";
    }

}
