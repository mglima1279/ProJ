package com.api.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.entities.Product;
import com.api.entities.Rate;
import com.api.entities.User;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    Page<Rate> findByUser(User user, Pageable pageable);

    Page<Rate> findByProduct(Product product, Pageable pageable);

    Page<Rate> findByRate(int rate, Pageable pageable);
}
