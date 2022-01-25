package com.tamudatathon.bulletin.service;

import java.net.URL;
import java.util.List;

import com.tamudatathon.bulletin.data.entity.Accolade;
import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Event;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.EventRepository;
import com.tamudatathon.bulletin.util.exception.ChallengeInvalidException;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.FileUploadException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChallengeService {
 
    private final ChallengeRepository challengeRepository;
    private final EventRepository eventRepository;
    private final CommonService commonService;
    private final AmazonService amazonService;

    @Value("${app.api.accolades.defaultColors}")
    private List<String> colorPlaces;

    @Value("${app.api.challenges.accolades.max}")
    private int maxPlaces;
 
    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository,
        EventRepository eventRepository,
        CommonService commonService, 
        AmazonService amazonService) {
        this.challengeRepository = challengeRepository;
        this.eventRepository = eventRepository;
        this.commonService = commonService;
        this.amazonService = amazonService;
    }

    public List<Challenge> getChallenges(Long eventId) throws EventNotFoundException {
        Event event = this.commonService.validEvent(eventId);
        return event.getChallenges();
    }

    public Challenge getChallenge(Long eventId, Long challengeId) throws EventNotFoundException,
        ChallengeNotFoundException {
        Event event = this.commonService.validEvent(eventId);
        for (Challenge challenge : event.getChallenges()) {
            if (challenge.getChallengeId() == challengeId) return challenge;
        }
        throw new ChallengeNotFoundException(challengeId);
    }

    public Challenge getChallengeById(Long challengeId) throws ChallengeNotFoundException {
        return this.challengeRepository.findById(challengeId)
            .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Challenge addChallenge(Long eventId, Long challengeId, Challenge challenge) throws EventNotFoundException,
        ChallengeNotFoundException {
        Event event = this.commonService.validEvent(eventId);
        challenge.setEvent(event);
        int oldNumPlaces = 0;
        if (!this.commonService.validHexColor(challenge.getColor())) {
            throw new ChallengeInvalidException("color is not a 6-digit hex code");
        }
        if (challenge.getNumPlaces() > this.maxPlaces) {
            throw new ChallengeInvalidException("The max number of places is " + this.maxPlaces);
        }
        if (challengeId != null) {
            Challenge oldChallenge = this.challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeNotFoundException(challengeId));
            challenge.setChallengeId(challengeId);
            oldNumPlaces = oldChallenge.getNumPlaces();
            challenge.setAccolades(oldChallenge.getAccolades());
            challenge.setQuestions(oldChallenge.getQuestions());
            challenge.setSubmissions(oldChallenge.getSubmissions());
        }
        if (oldNumPlaces < challenge.getNumPlaces()) {
            for (int i = oldNumPlaces; i < challenge.getNumPlaces(); i++) {
                Accolade accolade = new Accolade();
                accolade.setDescription("This declares that you received " + this.commonService.ordinalSuffixOf(i + 1) + " place in " + challenge.getName());
                accolade.setName(this.commonService.ordinalSuffixOf(i + 1) + " in the " + challenge.getName());
                if (i >= this.colorPlaces.size()) {
                    accolade.setColor(this.colorPlaces.get(this.colorPlaces.size() - 1));
                }
                else accolade.setColor(this.colorPlaces.get(i));
                challenge.addAccolade(accolade);
            }
        } else if (oldNumPlaces > challenge.getNumPlaces()) {
            for (int i = oldNumPlaces - 1; i >= challenge.getNumPlaces(); i--) {
                challenge.removeAccolade(challenge.getAccolades().get(i));
            }
        }
        Challenge newChallenge = this.challengeRepository.save(challenge);
        return newChallenge;
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteChallenge(Long eventId, Long challengeId) {
        Event event = this.commonService.validEvent(eventId);
        for (Challenge challenge : event.getChallenges()) {
            if (challenge.getChallengeId() == challengeId) {
                event.removeChallenge(challenge);
                this.eventRepository.save(event);
                this.challengeRepository.delete(challenge);
                return;
            }
        }
        throw new ChallengeNotFoundException(challengeId);
    }

    public URL uploadImage(MultipartFile file, Long eventId, Long id) throws Exception, EventNotFoundException {
        if (!file.getContentType().contains("image")) {
            throw new FileUploadException("File is not an image");
        }
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, id);
        if (challenge.getImageUrl() != null) {
            this.deleteImage(eventId, id);
        }
        URL fileUrl = this.amazonService.uploadFile(file);
        challenge.setImageUrl(fileUrl);
        this.challengeRepository.save(challenge);
        return fileUrl;
    }

    public void deleteImage(Long eventId, Long id) throws Exception, EventNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, id);
        this.amazonService.deleteFileFromS3Bucket(challenge.getImageUrl());
        challenge.setImageUrl(null);
        this.challengeRepository.save(challenge);
    }
}
