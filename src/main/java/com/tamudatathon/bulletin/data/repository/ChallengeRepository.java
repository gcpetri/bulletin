package com.tamudatathon.bulletin.data.repository;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query(value = "SELECT CHALLENGE_ID FROM CHALLENGES WHERE EVENT_ID = ?1", nativeQuery = true)
    List<Long> findChallengeIdsByEvent(Long eventId);
}
