package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.Challenge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
}
