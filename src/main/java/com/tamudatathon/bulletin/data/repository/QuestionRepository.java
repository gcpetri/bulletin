package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> { 
}
