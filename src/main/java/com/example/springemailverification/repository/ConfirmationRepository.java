package com.example.springemailverification.repository;

import com.example.springemailverification.model.Confirmation;
import com.example.springemailverification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, String> {
    Confirmation findByToken(String token);
    Confirmation findByUser(User user);
}