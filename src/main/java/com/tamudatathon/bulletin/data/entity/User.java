package com.tamudatathon.bulletin.data.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="USERS")
@DynamicUpdate
public class User {

    @Id
    @Column(name="USER_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    @Column(name="USER_AUTH_ID", nullable=false, unique=true)
    @NotBlank(message="userAuthId is required")
    private String userAuthId;

    @Column(name="DISCORD_INFO", nullable=false, unique=true)
    @NotBlank(message="discordInfo is required")
    private String discordInfo;

    @JoinTable(name="USER_SUBMISSIONS",
        joinColumns=@JoinColumn(name="USER_ID"),
        inverseJoinColumns=@JoinColumn(name="SUBMISSION_ID"))
    @OneToMany
    private List<Submission> submissions;

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAuthId() {
        return this.userAuthId;
    }

    public void setUserAuthId(String userAuthId) {
        this.userAuthId = userAuthId;
    }

    public String getDiscordInfo() {
        return this.discordInfo;
    }

    public void setDiscordInfo(String discordInfo) {
        this.discordInfo = discordInfo;
    }

    public List<Submission> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
};
