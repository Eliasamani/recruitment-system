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

@Entity
@Table(name = "reset_token")
public class ResetToken implements ResetTokenDTO {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reset_token_id")
    private long resetTokenId;

    @Getter
    @Setter
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Getter
    @Setter
    @Column(name = "reset_token")
    private int resetToken;

    @Getter
    @CreationTimestamp
    @Column(name = "create_time")
    private Instant createTime = Instant.now();

    @Getter
    @Setter
    @Column(name = "valid")
    private Boolean valid = true;

    protected ResetToken() {
    }

    public ResetToken(Person person, int resetToken) {
        this.person = person;
        this.resetToken = resetToken;
    }

    @Override
    public String toString() {
        return "ResetToken [resetTokenId=" + resetTokenId + ", person=" + person + ", resetToken=" + resetToken
                + ", createTime=" + createTime + ", valid=" + valid + "]";
    }

}
