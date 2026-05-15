package com.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.entities.Order;
import com.api.entities.Store;
import com.api.entities.User;
import com.api.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByClient(User client, Pageable pageable);

    Page<Order> findByStore(Store store, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("""
                SELECT o FROM Order o
                WHERE o.client = :client
                AND o.store = :store
            """)
    Page<Order> findByClientStore(
            @Param("client") User client,
            @Param("store") Store store,
            Pageable pageable);
}