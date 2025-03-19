package com.example.demo.Repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByName(String username);
    
}
