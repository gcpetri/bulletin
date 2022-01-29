package com.tamudatathon.bulletin.data.repository;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> { 

    List<Submission> findByNameIgnoreCaseContaining(String name);
}