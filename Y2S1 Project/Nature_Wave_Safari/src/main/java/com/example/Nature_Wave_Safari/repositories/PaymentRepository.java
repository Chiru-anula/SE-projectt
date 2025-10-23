package com.example.Nature_Wave_Safari.repositories;

import com.example.Nature_Wave_Safari.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findById(int paymentId);
}
