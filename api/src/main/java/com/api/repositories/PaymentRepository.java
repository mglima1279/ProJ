package com.api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.Order;
import com.api.entities.Payment;
import com.api.enums.PayMethod;
import com.api.enums.PayStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);

    Page<Payment> findByMethod(PayMethod method, Pageable pageable);

    Page<Payment> findByStatus(PayStatus status, Pageable pageable);
}
