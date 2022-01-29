package com.tamudatathon.bulletin.data.entity;

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
@Table(name="SUBMISSION_QUESTIONS")
@DynamicUpdate
public class SubmissionQuestion {
    
    @Id
    @Column(name="SUBMISSION_QUESTION_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long submissionQuestionId;

    @Column(name="ANSWER")
    private String answer;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SUBMISSION_ID", referencedColumnName="SUBMISSION_ID")
    private Submission submission;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="QUESTION_ID", referencedColumnName="QUESTION_ID")
    private Question question;
}
