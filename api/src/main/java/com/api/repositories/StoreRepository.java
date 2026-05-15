package com.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.entities.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
