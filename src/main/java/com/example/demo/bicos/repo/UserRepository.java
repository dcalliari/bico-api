package com.example.demo.bicos.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.bicos.controller.dto.MyUserDto;
import com.example.demo.bicos.models.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "SELECT * FROM users WHERE deleted_at IS NOT NULL", nativeQuery = true)
    List<User> findDeleted();
    UserDetails findByLogin(String login);
    List<MyUserDto> queryById(UUID id);
}
