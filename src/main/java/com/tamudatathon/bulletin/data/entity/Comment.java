package com.tamudatathon.bulletin.data.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="COMMENTS")
@DynamicUpdate
public class Comment {
    
    @Id
    @Column(name="COMMENT_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long commentId;

    @Column(name="MESSAGE", updatable=false)
    private String message;

    @Column(name="CREATED_ON", updatable=false)
    private LocalDateTime createdOn;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SUBMISSION_ID", referencedColumnName="SUBMISSION_ID")
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID", referencedColumnName="USER_ID")
    private User user;

    public Long getCommentId() {
        return this.commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
 
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
