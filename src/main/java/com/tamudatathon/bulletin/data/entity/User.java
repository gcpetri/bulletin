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

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="USERS")
@DynamicUpdate
public class User {

    @Id
    @Column(name="USER_ID", updatable=false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;

    @Column(name="AUTH_ID", nullable=false, unique=true)
    @NotBlank(message="authId is required")
    private String authId;

    @Column(name="DISCORD_INFO", nullable=false, unique=true)
    @NotBlank(message="discordInfo is required")
    private String discordInfo;

    @Column(name="FIRST_NAME")
    private String firstName;

    @Column(name="LAST_NAME")
    private String lastName;

    @Column(name="EMAIL")
    private String email;

    @Column(name="IS_ADMIN", nullable=false)
    private Boolean isAdmin;

    @JsonIgnore
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

    public String getAuthId() {
        return this.authId;
    }

    public void setAuthId(String userAuthId) {
        this.authId = userAuthId;
    }

    public String getDiscordInfo() {
        return this.discordInfo;
    }

    public void setDiscordInfo(String discordInfo) {
        this.discordInfo = discordInfo;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    public List<Submission> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
};
