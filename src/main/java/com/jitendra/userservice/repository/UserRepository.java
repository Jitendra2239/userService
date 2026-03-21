package com.jitendra.userservice.repository;

import com.jitendra.userservice.model.UserStatus;
import com.jitendra.userservice.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<Users> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Users> findAll(Pageable pageable);

    List<Users> findByStatus(UserStatus status);

    @Query("SELECT DISTINCT u FROM Users u " +
            "LEFT JOIN FETCH u.roles " +
            "LEFT JOIN FETCH u.addresses " +
            "WHERE u.id = :id")
    Optional<Users> findUserWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM Users u " +
            "LEFT JOIN FETCH u.roles " +
            "LEFT JOIN FETCH u.addresses")
    List<Users> findAllWithDetails();
}
