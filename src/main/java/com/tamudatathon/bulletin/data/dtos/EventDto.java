package com.tamudatathon.bulletin.data.dtos;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventDto {

    private static final DateTimeFormatter dateFormatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Long id;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private String color;
    private Boolean show;
    private URL imageUrl;
    private List<ChallengeDto> challenges;

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

    public URL getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getStartTime() throws ParseException {
        return LocalDateTime.parse(this.startTime, dateFormatter);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = dateFormatter.format(startTime);
    }

    public LocalDateTime getEndTime() throws ParseException {
        return LocalDateTime.parse(this.endTime, dateFormatter);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = dateFormatter.format(endTime);
    }

    public List<ChallengeDto> getChallenges() {
        return this.challenges;
    }

    public void setChallenges(List<ChallengeDto> challenges) {
        this.challenges = challenges;
    }
    
}
