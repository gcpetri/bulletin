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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="ACCOLADES")
@DynamicUpdate
public class Accolade {

    @Id
    @Column(name="ACCOLADE_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long accoladeId;

    @Column(name="NAME", nullable=false, unique=true)
    @NotBlank(message="Name is required")
    private String name;

    @Column(name="DESCRIPTION")
	private String description;

    @Column(name="COLOR")
    @Size(min=6, max=6)
    private String color = "632ed2";

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_ID", referencedColumnName="CHALLENGE_ID")
    private Challenge challenge;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition="integer", name="SUBMISSION_ID", referencedColumnName="SUBMISSION_ID", nullable=true)
    private Submission submission;

    public Long getAccoladeId() {
        return this.accoladeId;
    }

    public void setAccoladeId(Long accoladeId) {
        this.accoladeId = accoladeId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Challenge getChallenge() {
        return this.challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Submission getSubmission() {
        return this.submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    // utils

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Accolade )) return false;
        return this.accoladeId != null && this.accoladeId.equals(((Accolade) o).getAccoladeId());
    }
 
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}

