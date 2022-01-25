package com.tamudatathon.bulletin.data.entity;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

    @Column(name="ICON_URL")
    private URL iconUrl;

    @OneToMany(mappedBy="submission",
        targetEntity=Accolade.class,
        cascade=CascadeType.ALL,
        orphanRemoval=false)
    private List<Accolade> accolades = new ArrayList<>();

    @ManyToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
    @JoinTable(name="USER_SUBMISSIONS",
        joinColumns=@JoinColumn(name="SUBMISSION_ID", referencedColumnName="SUBMISSION_ID"),
        inverseJoinColumns=@JoinColumn(name="USER_ID", referencedColumnName="USER_ID"))
    private Set<User> users = new HashSet<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="CHALLENGE_ID", referencedColumnName="CHALLENGE_ID")
    private Challenge challenge;

    @Column(name="CREATED_ON", updatable=false)
    private LocalDateTime createdOn;
 
    @Column(name="UPDATED_ON")
    private LocalDateTime updatedOn;

    // getters and setters

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

    public URL getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(URL iconUrl) {
        this.iconUrl = iconUrl;
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

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setAccolades(List<Accolade> accolades) {
        this.accolades = accolades;
    }

    public Challenge getChallenge() {
        return this.challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public void addAccolade(Accolade accolade) {
        if (this.accolades == null) this.accolades = new ArrayList<>();
        this.accolades.add(accolade);
        accolade.setSubmission(this);
    }

    public void removeAccolade(Accolade accolade) {
        if (this.accolades == null) this.accolades = new ArrayList<>();
        this.accolades.remove(accolade);
        accolade.setSubmission(null);
    }

    public void addUser(User user) {
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);
        user.getSubmissions().add(this);
    }

    public void removeUser(User user) {
        if (this.users == null) this.users = new HashSet<>();
        this.users.remove(user);
        user.getSubmissions().remove(this);
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
 
    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
 
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
 
    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
