package com.ec.payment.service.repo;

import com.ec.payment.service.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    List<Payment> findByOrderIdIn(List<Long> orderIds);
}
