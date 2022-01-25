package com.tamudatathon.bulletin.data.repository;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubmissionRepository extends JpaRepository<Submission, Long> { 

    // @Query(value = "SELECT * FROM SUBMISSIONS WHERE NAME LIKE '%?1%'", nativeQuery = true)
    // List<Submission> findBySimilarName(Long challengeId, String name);
}