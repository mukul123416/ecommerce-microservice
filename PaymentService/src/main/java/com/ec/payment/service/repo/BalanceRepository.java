package com.ec.payment.service.repo;

import com.ec.payment.service.entities.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<UserBalance,Long> {
}
