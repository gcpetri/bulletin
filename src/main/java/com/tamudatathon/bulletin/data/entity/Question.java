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
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="QUESTIONS")
@DynamicUpdate
public class Question {

    @Id
    @Column(name="QUESTION_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long questionId;

    @Column(name="DESCRIPTION", nullable=false)
    @NotBlank(message="Description is required")
	private String description;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_ID", referencedColumnName="CHALLENGE_ID")
    private Challenge challenge;

    public Long getQuestionId() {
        return this.questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Challenge getChallenge() {
        return this.challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
