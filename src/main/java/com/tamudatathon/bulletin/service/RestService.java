package com.tamudatathon.bulletin.service;

import java.net.URISyntaxException;

import com.tamudatathon.bulletin.data.dtos.AuthDto;
import com.tamudatathon.bulletin.data.dtos.HarmoniaDto;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.util.exception.ParticipantNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    @Value("${app.api.auth.authUrl}")
    private String authUrl;

    @Value("${app.api.auth.harmoniaUrl}")
    private String harmoniaUrl;

    @Value("${app.api.auth.adminAccessToken}")
    private String adminAccessToken;

    @Autowired
    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public User getUserFromAccessToken(String accessToken) throws URISyntaxException {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "accessToken=" + accessToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<AuthDto> response = this.getRestTemplate().exchange(this.authUrl, HttpMethod.GET,
            requestEntity, AuthDto.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ParticipantNotFoundException("Valid accessToken missing");
        }

        AuthDto authDto = response.getBody();

        User user = new User();
        user.setAuthId(authDto.getAuthId());
        user.setFirstName(authDto.getFirstName());
        user.setLastName(authDto.getLastName());
        user.setEmail(authDto.getEmail());
        user.setIsAdmin(authDto.getIsAdmin());

        String discordInfo = this.getDiscordInfo(requestEntity, authDto.getAuthId());
        user.setDiscordInfo(discordInfo);

        return user;
    }

    private String getDiscordInfo(HttpEntity<Object> requestEntity, String authId) {
        ResponseEntity<HarmoniaDto> response = this.getRestTemplate().exchange(this.harmoniaUrl + "?userAuthId=" + authId,
            HttpMethod.GET, requestEntity, HarmoniaDto.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ParticipantNotFoundException("User is not in the discord guild");
        }

        HarmoniaDto harmoniaDto = response.getBody();
        return harmoniaDto.getDiscordInfo();
    }

    private String getAuthId(HttpEntity<Object> requestEntity, String discordInfo) {
        ResponseEntity<HarmoniaDto> response = this.getRestTemplate().exchange(this.harmoniaUrl + "?discordInfo=" + discordInfo,
            HttpMethod.GET, requestEntity, HarmoniaDto.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ParticipantNotFoundException(discordInfo + " is not in the discord guild");
        }
        
        HarmoniaDto harmoniaDto = response.getBody();
        return harmoniaDto.getUserAuthId();
    }

    public User getUserFromDiscordInfo(String discordInfo) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "accessToken=" + this.adminAccessToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, requestHeaders);
        String authId = this.getAuthId(requestEntity, discordInfo);

        ResponseEntity<AuthDto> response = this.getRestTemplate().exchange(this.authUrl + "?authId=" + authId, 
            HttpMethod.GET, requestEntity, AuthDto.class);
        
        AuthDto authDto = response.getBody();

        if (!authId.equals(authDto.getAuthId())) { // sanity check
            throw new ParticipantNotFoundException("user authId mismatch");
        }

        User user = new User();
        user.setAuthId(authDto.getAuthId());
        user.setFirstName(authDto.getFirstName());
        user.setLastName(authDto.getLastName());
        user.setEmail(authDto.getEmail());
        user.setIsAdmin(authDto.getIsAdmin());
        user.setDiscordInfo(discordInfo);

        return user;
    }
 
};
