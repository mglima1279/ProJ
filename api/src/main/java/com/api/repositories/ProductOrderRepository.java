package com.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.entities.Order;
import com.api.entities.Product;
import com.api.entities.ProductOrder;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    Page<ProductOrder> findByOrder(Order order, Pageable pageable);

    Page<ProductOrder> findByProduct(Product product, Pageable pageable);

    @Query("""
                SELECT po FROM ProductOrder po
                WHERE po.order = :order
                AND po.product = :product
            """)
    Page<ProductOrder> findByOrderAndProduct(@Param("order") Order order, @Param("product") Product product,
            Pageable pageable);
}