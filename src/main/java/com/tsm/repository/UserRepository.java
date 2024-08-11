package com.tsm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tsm.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
}
