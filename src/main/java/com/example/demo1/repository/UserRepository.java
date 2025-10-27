package com.example.demo1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo1.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    
    List<User>findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    void deleteByName(String name);
    Optional<User> findByName(String name);
    List<User>findByNameOrEmail(String name,String email);
    List<User>findByNameAndEmail(String name,String email);
    List<User>findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(String name,String email);
    List<User>findByEmailContainingIgnoreCase(String email);
}

