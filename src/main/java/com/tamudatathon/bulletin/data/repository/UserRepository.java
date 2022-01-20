package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> { 

    @Query(value = "SELECT * FROM USERS WHERE AUTH_ID = ?1", nativeQuery = true)
    User findByAuthId(String authId);

}