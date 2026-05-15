package com.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
                SELECT p FROM Product p
                WHERE p.name = :name
                AND p.store.id = :storeId
            """)
    Page<Product> findByName(
            @Param("storeId") long storeId,
            @Param("name") String name,
            Pageable pageable);
}