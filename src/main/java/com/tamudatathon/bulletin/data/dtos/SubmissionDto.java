package com.tamudatathon.bulletin.data.dtos;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDto {

    private static final DateTimeFormatter dateFormatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Long id;
    private String name;
	private String description;
    private String color;
    private Boolean show;
    private URL videoLink;
    private List<URL> links;
    private List<String> tags;
    private URL sourceCodeUrl;
    private URL iconUrl;
    private List<AccoladeDto> accolades = new ArrayList<>();
    private List<UserDto> users = new ArrayList<>();
    private String createdOn;
    private String updatedOn;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<URL> getLinks() {
        return this.links;
    }

    public void setLinks(List<URL> links) {
        this.links = links;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<AccoladeDto> getAccolades() {
        return this.accolades;
    }

    public void setAccolades(List<AccoladeDto> accolades) {
        this.accolades = accolades;
    }

    public List<UserDto> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public String getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = dateFormatter.format(createdOn);
    }

    public String getUpdatedOn() {
        return this.updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = dateFormatter.format(updatedOn);
    }
}
