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

    <S extends ResetToken> S findResetTokenByPerson(Person person);

    <S extends ResetToken> List<S> findResetTokenByPersonAndValidTrue(Person person);

    <S extends ResetToken> S findResetTokenByPersonAndResetToken(Person person, int resetToken);

    @Override
    <S extends ResetToken> S save(S entity);

}
