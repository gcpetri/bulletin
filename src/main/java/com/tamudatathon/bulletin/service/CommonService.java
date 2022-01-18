package com.tamudatathon.bulletin.service;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import java.util.regex.Matcher;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Event;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.repository.EventRepository;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    private EventRepository eventRepository;

    private String HEX_COLOR_REGEX = "^([A-Fa-f0-9]{6})$";
    private Pattern colorPattern;

    @PostConstruct
    public void init() {
        this.colorPattern = Pattern.compile(HEX_COLOR_REGEX);
    }

    public Submission validEventChallengeSubmission(Long eventId, Long challengeId, 
        Long submissionId) throws EventNotFoundException, ChallengeNotFoundException, SubmissionNotFoundException {
        Challenge challenge = this.validEventAndChallenge(eventId, challengeId);
        for (Submission submission : challenge.getSubmissions()) {
            if (submission.getSubmissionId() == submissionId) return submission;
        }
        throw new SubmissionNotFoundException(submissionId);
    }

    public Challenge validEventAndChallenge (Long eventId, Long challengeId)
        throws EventNotFoundException, ChallengeNotFoundException {
        Event event = this.eventRepository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException(eventId));
        for (Challenge challenge : event.getChallenges()) {
            if (challenge.getChallengeId() == challengeId) return challenge;
        }
        throw new ChallengeNotFoundException(challengeId);
    }

    public Event validEvent(Long eventId) throws EventNotFoundException {
        return this.eventRepository.findById(eventId)
            .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    public String ordinalSuffixOf(int i) {
        int j = i % 10;
        int k = i % 100;
        if (j == 1 && k != 11) {
            return i + "st";
        }
        if (j == 2 && k != 12) {
            return i + "nd";
        }
        if (j == 3 && k != 13) {
            return i + "rd";
        }
        return i + "th";
    }

    public boolean validHexColor(String colorStr) {
        Matcher matcher = this.colorPattern.matcher(colorStr);
        return matcher.matches();
    }
}
