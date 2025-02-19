package se.kth.iv1201.recruitment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.model.resettoken.ResetToken;

@Transactional
@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    /**
     * 
     * @param person The person whose reset token should be found
     * @return The reset token of the person, if none null will be returned
     */
    <S extends ResetToken> S findResetTokenByPerson(Person person);

    /**
     * 
     * @param person The person whose valid token should be looked up
     * @return a list of tokens marked as valid linked to the person, if none null
     *         will be returned
     */
    <S extends ResetToken> List<S> findResetTokenByPersonAndValidTrue(Person person);

    /**
     * 
     * @param person     The person who should have the token
     * @param resetToken The actual token number of the token
     * @return a token which is linked to the person with the specified number, if
     *         none null will be returned
     */
    <S extends ResetToken> S findResetTokenByPersonAndResetToken(Person person, int resetToken);

    @Override
    <S extends ResetToken> S save(S entity);

}
