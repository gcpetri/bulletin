package com.tamudatathon.bulletin.data.entity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="SUBMISSIONS")
@DynamicUpdate
public class Submission {

    @Id
    @Column(name="SUBMISSION_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long submissionId;

    @Column(name="NAME", nullable=false, unique=true)
    @NotBlank(message="Name is required")
    private String name;

    @Column(name="DESCRIPTION")
	private String description = "Describe your submission here!";

    @Column(name="COLOR")
    private String color = "632ed2";

    @Column(name="SHOW", nullable=false)
    @NotNull(message="Show is required")
    private Boolean show;

    @Column(name="VIDEO_LINK", nullable=false)
    @NotNull(message="Video Link is required")
    private URL videoLink;

    @ElementCollection
    @CollectionTable(name="LINKS")
    private Collection<URL> links;

    @ElementCollection
    @CollectionTable(name="TAGS")
    private Collection<String> tags;

    @Column(name="SOURCE_CODE_URL")
    private URL sourceCodeUrl;

    @Column(name="SOURCE_CODE_KEY")
    private String sourceCodeKey;

    @Column(name="ICON_URL")
    private URL iconUrl;

    @Column(name="ICON_KEY")
    private String iconKey;

    @OneToMany(mappedBy="submission",
        targetEntity=Accolade.class,
        cascade=CascadeType.ALL,
        orphanRemoval=false)
    private List<Accolade> accolades = new ArrayList<>();

    @OneToMany(mappedBy="submission",
        targetEntity=SubmissionQuestion.class,
        cascade=CascadeType.ALL,
        orphanRemoval=true)
    private List<SubmissionQuestion> submissionQuestions = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_ID", referencedColumnName="CHALLENGE_ID")
    private Challenge challenge;

    public Long getSubmissionId() {
        return this.submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
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

    public Boolean getShow() {
        return this.show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public URL getVideoLink() {
        return this.videoLink;
    }

    public void setVideoLink(URL videoLink) {
        this.videoLink = videoLink;
    }

    public URL getSourceCodeUrl() {
        return this.sourceCodeUrl;
    }

    public void setSourceCodeUrl(URL sourceCodeUrl) {
        this.sourceCodeUrl = sourceCodeUrl;
    }

    public String getSourceCodeKey() {
        return this.sourceCodeKey;
    }

    public void setSourceCodeKey(String sourceCodeKey) {
        this.sourceCodeKey = sourceCodeKey;
    }

    public URL getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconKey() {
        return this.iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public Collection<URL> getLinks() {
        return this.links;
    }

    public void setLinks(Collection<URL> links) {
        this.links = links;
    }

    public Collection<String> getTags() {
        return this.tags;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public List<Accolade> getAccolades() {
        return this.accolades;
    }

    public void setAccolades(List<Accolade> accolades) {
        this.accolades = accolades;
    }

    public List<SubmissionQuestion> getSubmissionQuestions() {
        return this.submissionQuestions;
    }

    public void setSubmissionQuestions(List<SubmissionQuestion> submissionQuestions) {
        this.submissionQuestions = submissionQuestions;
    }

    public Challenge getChallenge() {
        return this.challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void addSubmissionQuestion(SubmissionQuestion submissionQuestion) {
        if (this.submissionQuestions == null) this.submissionQuestions = new ArrayList<>();
        this.submissionQuestions.add(submissionQuestion);
        submissionQuestion.setSubmission(this);
    }

    public void removeSubmissionQuestion(SubmissionQuestion submissionQuestion) {
        if (this.submissionQuestions == null) this.submissionQuestions = new ArrayList<>();
        this.submissionQuestions.remove(submissionQuestion);
        submissionQuestion.setSubmission(null);
    }
}
