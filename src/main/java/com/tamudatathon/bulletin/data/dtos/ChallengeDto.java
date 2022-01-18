package com.tamudatathon.bulletin.data.dtos;

import java.net.URL;
import java.util.List;

public class ChallengeDto {

    private Long id;
    private String name;
    private String description;
    private int numPlaces;
    private String color;
    private URL imageUrl;
    private List<AccoladeDto> accolades;
    private List<SubmissionDto> submissions;
    private List<QuestionDto> questions;

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

    public List<AccoladeDto> getAccolades() {
        return this.accolades;
    }

    public void setAccolades(List<AccoladeDto> accolades) {
        this.accolades = accolades;
    }
    
    public List<SubmissionDto> getSubmissions() {
        return this.submissions;
    }

    public void setSubmissions(List<SubmissionDto> submissions) {
        this.submissions = submissions;
    }

    public List<QuestionDto> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
