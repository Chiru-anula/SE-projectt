package com.example.Nature_Wave_Safari.interfaces;

import com.example.Nature_Wave_Safari.entities.Payment;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PaymentService {
    Payment addPayment(int bookingId);

   ResponseEntity<Void> verifyPayment(int paymentId);

    List<Payment> findAll();

    void deletePayment(int paymentId);


}
