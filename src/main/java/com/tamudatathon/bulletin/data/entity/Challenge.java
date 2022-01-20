package com.tamudatathon.bulletin.data.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name="CHALLENGES")
@DynamicUpdate
public class Challenge {

    @Value("${app.api.challenges.accolades.max}")
    private static final int maxAccolades = 5;

    @Id
    @Column(name="CHALLENGE_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long challengeId;

    @Column(name="NAME", nullable=false, unique=true)
    @NotBlank(message="Name is required")
    private String name;

    @Column(name="DESCRIPTION")
	private String description;

    @Column(name="NUM_PLACES", nullable=false)
    @Min(0)
    @Max(maxAccolades)
    private int numPlaces = 3;

    @Column(name="COLOR")
    @Size(min=6, max=6)
    private String color = "632ed2";

    @Column(name="IMAGE_URL")
    private URL imageUrl;

    @OneToMany(mappedBy="challenge",
        targetEntity=Accolade.class,
        cascade=CascadeType.ALL,
        orphanRemoval=true)
    private List<Accolade> accolades = new ArrayList<>();

    @OneToMany(mappedBy="challenge",
        targetEntity=Question.class,
        cascade=CascadeType.ALL,
        orphanRemoval=true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy="challenge",
        targetEntity=Submission.class,
        cascade=CascadeType.ALL,
        orphanRemoval=false)
    private List<Submission> submissions = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="EVENT_ID", referencedColumnName="EVENT_ID")
    private Event event;

    public Long getChallengeId() {
        return this.challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
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

    public int getNumPlaces() {
        return this.numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public URL getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Accolade> getAccolades() {
        return this.accolades;
    }

    public void setAccolades(List<Accolade> accolades) {
        this.accolades = accolades;
    }

    public List<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Submission> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // utils

    public void addAccolade(Accolade accolade) {
        if (this.accolades == null) this.accolades = new ArrayList<>();
        this.accolades.add(accolade);
        accolade.setChallenge(this);
    }

    public void removeAccolade(Accolade accolade) {
        if (this.accolades == null) this.accolades = new ArrayList<>();
        this.accolades.remove(accolade);
        accolade.setChallenge(null);
    }

    public void addQuestion(Question question) {
        if (this.questions == null) this.questions = new ArrayList<>();
        this.questions.add(question);
        question.setChallenge(this);
    }

    public void removeQuestion(Question question) {
        if (this.questions == null) this.questions = new ArrayList<>();
        this.questions.remove(question);
        question.setChallenge(null);
    }

    public void addSubmission(Submission submission) {
        if (this.submissions == null) this.submissions = new ArrayList<>();
        this.submissions.add(submission);
        submission.setChallenge(this);
    }

    public void removeSubmission(Submission submission) {
        if (this.submissions == null) this.submissions = new ArrayList<>();
        this.submissions.remove(submission);
        submission.setChallenge(null);
    }
}
