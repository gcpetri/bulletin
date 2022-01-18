package com.tamudatathon.bulletin.data.entity;

// import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="SUBMISSION_QUESTION")
@DynamicUpdate
public class SubmissionQuestion {

    @Id
    @Column(name="SUBMISSION_QUESTION_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long submissionQuestionId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SUBMISSION_ID", referencedColumnName="SUBMISSION_ID")
    private Submission submission;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="QUESTION_ID", referencedColumnName="QUESTION_ID")
    private Question question;

    @Column(name="ANSWER", nullable=false)
    @NotBlank(message="Answer is required")
    private String answer;

    public Long getSubmissionQuestionId() {
        return this.submissionQuestionId;
    }

    public void setSubmissionQuestionId(Long submissionQuestionId) {
        this.submissionQuestionId = submissionQuestionId;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

