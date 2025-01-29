package se.kth.iv1201.recruitment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import se.kth.iv1201.recruitment.model.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    
   Optional<Account> findUserByUsername(String username);
} 
