package se.kth.iv1201.recruitment.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.Person;
import se.kth.iv1201.recruitment.model.ResetToken;

@Transactional
@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken,Long>{
    
    
    <S extends ResetToken> S findResetTokenByPerson(Person person);

    

    @Override
    <S extends ResetToken> S save(S entity);
    
}
