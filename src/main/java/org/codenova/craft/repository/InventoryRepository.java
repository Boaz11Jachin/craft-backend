package org.codenova.craft.repository;

import org.codenova.craft.entity.Inventory;
import org.codenova.craft.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProduct(Product product);
}
