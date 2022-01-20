package com.tamudatathon.bulletin.data.entity;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="EVENTS")
@DynamicUpdate
public class Event {

    @Id
    @Column(name="EVENT_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long eventId;

    @Column(name="NAME", nullable=false, unique=true)
    @NotBlank(message="Name is required")
    private String name;

    @Column(name="DESCRIPTION")
	private String description;

    @Column(name="START_TIME", nullable=false)
	private LocalDateTime startTime;

    @Column(name="END_TIME", nullable=false)
    private LocalDateTime endTime;

    @Column(name="COLOR")
    @Size(min=6, max=6)
    private String color = "632ed2";

    @Column(name="SHOW", nullable=false)
    @NotNull(message="Show is required")
    private Boolean show;

    @Column(name="IMAGE_URL")
    private URL imageUrl;

    @OneToMany(mappedBy="event",
        targetEntity=Challenge.class,
        cascade=CascadeType.ALL,
        orphanRemoval=true,
        fetch=FetchType.EAGER)
    private List<Challenge> challenges = new ArrayList<>();

    public Long getEventId() {
        return this.eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    public URL getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Challenge> getChallenges() {
        return this.challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    // utils

    public void addChallenge(Challenge challenge) {
        if (this.challenges == null) this.challenges = new ArrayList<>();
        this.challenges.add(challenge);
        challenge.setEvent(this);
    }

    public void removeChallenge(Challenge challenge) {
        if (this.challenges == null) this.challenges = new ArrayList<>();
        this.challenges.remove(challenge);
        challenge.setEvent(null);
    }
}
