package com.tamudatathon.bulletin.controller;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.dtos.ChallengeDto;
import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.service.ChallengeService;
import com.tamudatathon.bulletin.util.exception.ChallengeInvalidException;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("${app.api.basepath}/events/{eventId}/challenges")
public class ChallengeController {
    
    private final ChallengeService challengeService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public ChallengeController(ChallengeService challengeService,
        ModelMapper modelMapper) {
        this.challengeService = challengeService;
        this.modelMapper = modelMapper;
    } 

    @GetMapping(value={"", "/", "/list"})
    public List<ChallengeDto> getChallenges(@PathVariable Long eventId) throws EventNotFoundException {
        List<Challenge> challenges =this.challengeService.getChallenges(eventId);
        return challenges.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ChallengeDto getChallengeById(@PathVariable Long eventId, @PathVariable Long id) 
        throws EventNotFoundException, ChallengeNotFoundException {
        Challenge challenge = this.challengeService.getChallenge(eventId, id);
        return this.convertToDto(challenge);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteChallenge(@PathVariable Long eventId, @PathVariable Long id)
        throws EventNotFoundException, ChallengeNotFoundException {
        this.challengeService.deleteChallenge(eventId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value={"/save", "/save/{id}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public ChallengeDto createChallenge(@RequestBody ChallengeDto challengeDto,
        @PathVariable Long eventId, @PathVariable(required=false) Long id) throws ChallengeNotFoundException, EventNotFoundException {
        try {
            Challenge challenge = convertToEntity(challengeDto);
            Challenge newChallenge = this.challengeService.addChallenge(eventId, id, challenge);
            return convertToDto(newChallenge);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ChallengeInvalidException(e.getMessage());
        }
    }

    @RequestMapping(value={"/{id}/image/save"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadImage(@RequestPart(value = "file") MultipartFile file, @PathVariable Long eventId,
        @PathVariable Long id) 
        throws EventNotFoundException, ChallengeNotFoundException {
        try {
            URL url = this.challengeService.uploadImage(file, eventId, id);
            JSONObject resp = new JSONObject();
            resp.put("url", url.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            throw new ChallengeNotFoundException(id);
        }
    }

    @DeleteMapping("/{id}/image/delete")
    public ResponseEntity<Object> deleteImage(@PathVariable Long eventId, @PathVariable Long id) 
        throws EventNotFoundException, ChallengeNotFoundException {
        try {
            this.challengeService.deleteImage(eventId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            throw new ChallengeNotFoundException(id);
        }
    }

    // utils

    private ChallengeDto convertToDto(Challenge challenge) {
        ChallengeDto challengeDto = modelMapper.map(challenge, ChallengeDto.class);
        return challengeDto;
    }

    private Challenge convertToEntity(ChallengeDto challengeDto) throws ParseException {
        Challenge challenge = modelMapper.map(challengeDto, Challenge.class);
        if (challengeDto.getId() != null) {
            Challenge oldChallenge = this.challengeService.getChallengeById(challengeDto.getId());
            challenge.setChallengeId(oldChallenge.getChallengeId());
        }
        return challenge;
    }

}
