package com.example.Nature_Wave_Safari.services;

import com.example.Nature_Wave_Safari.entities.Payment;
import com.example.Nature_Wave_Safari.interfaces.PaymentService;
import com.example.Nature_Wave_Safari.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(int bookingId) {
        Payment payment = new Payment();
        payment.setBooking_id(bookingId);
        payment.setPayment_date(LocalDate.now());
        payment.setStatus("Pending");
        return paymentRepository.save(payment);
    }


    @Override
    public ResponseEntity<Void> verifyPayment(int paymentId) {
        Payment pay= paymentRepository.findById(paymentId);
        if(pay!=null) {
            pay.setStatus("Success");
            paymentRepository.save(pay);
        }
        return ResponseEntity.noContent().build();
    };
    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deletePayment(int paymentId) {
        paymentRepository.deleteById(paymentId);
    }




    ;


}

