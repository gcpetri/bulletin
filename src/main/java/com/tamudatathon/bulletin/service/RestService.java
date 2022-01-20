package com.tamudatathon.bulletin.service;

import java.net.URISyntaxException;

import com.tamudatathon.bulletin.middleware.AuthResponse;
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
import org.json.JSONException;
import org.json.JSONObject;

@Service
public class RestService {

    private final RestTemplate restTemplate;

    @Value("${app.api.auth.authUrl}")
    private String authUrl;

    @Value("${app.api.auth.harmoniaUrl}")
    private String harmoniaUrl;

    @Autowired
    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public RestTemplate getRestTemplate() {
        return this.restTemplate;
    }

    public AuthResponse getAuthResponse(String accessToken) throws URISyntaxException, JSONException {
        AuthResponse authResponse = new AuthResponse();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "accessToken=" + accessToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<Object> response = this.getRestTemplate().exchange(this.authUrl, HttpMethod.GET, requestEntity, Object.class);
        
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ParticipantNotFoundException("Valid accessToken missing");
        }

        JSONObject obj = new JSONObject(response.getBody().toString());
        authResponse.setAuthId(obj.getString("authId"));
        authResponse.setEmail(obj.getString("email"));
        authResponse.setFirstName(obj.getString("firstName"));
        authResponse.setLastName(obj.getString("lastName"));
        authResponse.setIsAdmin(obj.getBoolean("isAdmin"));

        String discordInfo = this.getDiscordInfo(requestEntity, authResponse.getAuthId());
        authResponse.setDiscordInfo(discordInfo);
        return authResponse;
    }

    private String getDiscordInfo(HttpEntity<Object> requestEntity, String authId) throws JSONException {
        ResponseEntity<Object> response = this.getRestTemplate().exchange(this.harmoniaUrl + "?userAuthId=" + authId,
            HttpMethod.GET, requestEntity, Object.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ParticipantNotFoundException("User is not in the discord guild");
        }
        String body = response.getBody().toString();
        body = body.replace('#', '\u00A3');
        JSONObject obj = new JSONObject(body);
        String discordInfo = obj.getString("discordInfo");
        return discordInfo.replace('\u00A3', '#');
    }

};
