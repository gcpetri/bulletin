package com.tamudatathon.bulletin.middleware;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.service.RestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthMiddleware extends OncePerRequestFilter {

    @Value("${app.api.auth.cookieName}")
    private String cookieName;

    @Value("${app.api.auth.authUrl}")
    private String authUrl;

    @Value("${app.api.auth.redirectUrl}")
    private String redirectUrl;

    private final RestService restService;

    @Autowired
    public AuthMiddleware(RestService restService) {
        this.restService = restService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
        throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String accessToken = "";
        System.out.println(this.cookieName);
        for (Cookie c : cookies) {
            System.out.println(c.getName());
            if (c.getName() == this.cookieName) {
                accessToken = c.getValue();
                System.out.println(c.getValue());
            }
        }
        // System.out.println(accessToken);
        if (accessToken.length() > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(this.cookieName, accessToken);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<Object> req = new HttpEntity<>(headers);

            ResponseEntity<AuthResponse> resp = this.restService.getRestTemplate()
                .exchange(this.authUrl, HttpMethod.GET, req, AuthResponse.class, 1);
            if (resp.getStatusCode() == HttpStatus.OK) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        // redirect to login
        System.out.println("Redirecting");
        // response.setHeader("Location", this.redirectUrl);
        // response.setStatus(302);
        filterChain.doFilter(request, response);
    }
};
