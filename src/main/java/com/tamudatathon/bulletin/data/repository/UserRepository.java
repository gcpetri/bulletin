package com.tamudatathon.bulletin.data.repository;

import java.util.List;

import javax.transaction.Transactional;

import com.tamudatathon.bulletin.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> { 

    @Query(value = "SELECT * FROM USERS WHERE AUTH_ID = ?1", nativeQuery = true)
    User findByAuthId(String authId);

    @Query(value = "SELECT * FROM USERS WHERE DISCORD_INFO in ?1", nativeQuery = true)
    List<User> findInDiscordInfoList(List<String> discordInfoList);

    @Modifying
    @Query(value = "INSERT INTO USER_SUBMISSIONS (USER_ID, SUBMISSION_ID) VALUES (?1, ?2)", nativeQuery = true)
    @Transactional
    void createUserSubmission(Long userId, Long submissionId);

    @Modifying
    @Query(value = "DELETE FROM USER_SUBMISSIONS WHERE USER_ID = ?1 AND SUBMISSION_ID = ?2", nativeQuery = true)
    @Transactional
    void deleteUserSubmission(Long userId, Long submissionId);
}