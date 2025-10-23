package com.example.Nature_Wave_Safari.controller;

import com.example.Nature_Wave_Safari.DTO.PaymentDTO;
import com.example.Nature_Wave_Safari.entities.Payment;
import com.example.Nature_Wave_Safari.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping("addPayment")
    public ResponseEntity<Payment> addpayment(@RequestParam int booking_id){
        Payment added_payment=paymentService.addPayment(booking_id);
        return ResponseEntity.ok(added_payment);
    }
    @GetMapping("verifyPayment")
    public ResponseEntity<Void> verifyPayment(@RequestParam int payment_id){
        return paymentService.verifyPayment(payment_id);
   }
    @GetMapping("findAll")
    public List<Payment> getAllPayments() {
        return paymentService.findAll();
    }
    @DeleteMapping("deletePayment")
    public ResponseEntity<Void> deletePayment(@RequestParam int payment_id){
        paymentService.deletePayment(payment_id);
        return ResponseEntity.noContent().build();
    }

}
