package com.tamudatathon.bulletin.data.repository;

import com.tamudatathon.bulletin.data.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
