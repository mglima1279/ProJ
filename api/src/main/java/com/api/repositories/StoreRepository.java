package com.api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.entities.Store;
import com.api.entities.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

        @Query("""
                                SELECT s FROM Store s
                                WHERE s.owner = :owner
                        """)
        Optional<Store> findByOwner(@Param("owner") User owner);

        @Query("""
                                SELECT s FROM Store s
                                WHERE s.owner.id = :ownerId
                        """)
        Optional<Store> findByOwnerId(@Param("ownerId") long ownerId);

        @Query("""
                                SELECT s FROM Store s
                                WHERE s.owner = :owner
                        """)
        Page<Store> findAllByOwner(@Param("owner") User owner, Pageable pageable);
}