package com.ec.inventory.service.repo;

import com.ec.inventory.service.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findByProductIdIn(List<Long> productIds);
    Inventory findByProductId(Long productId);
}
