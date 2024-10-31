package com.mt.jwtstarter.repository;

import com.mt.jwtstarter.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:query% OR u.firstName LIKE %:query% OR u.lastName LIKE %:query% OR u.email LIKE %:query%")
    Page<UserEntity> findByUsernameOrFirstNameOrLastNameContaining(@Param("query") String query, Pageable pageable);
}
