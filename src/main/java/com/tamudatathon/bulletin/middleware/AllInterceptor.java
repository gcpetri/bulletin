package com.tamudatathon.bulletin.middleware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.service.RestService;
import com.tamudatathon.bulletin.service.UserService;
import com.tamudatathon.bulletin.util.exception.LoginRedirectException;
import com.tamudatathon.bulletin.util.exception.ParticipantNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

import javax.servlet.http.Cookie;

@Component
public class AllInterceptor implements HandlerInterceptor {
    
    @Autowired
    UserService userService;

    @Autowired
    RestService restService;

    @Value("${app.api.auth.cookieName}")
    private String cookieName;

    @Value("${app.api.auth.redirectUrl}")
    private String redirectUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse  response, Object handler) {
        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            return true;
        }
        String accessToken = "";
        for (Cookie c : request.getCookies()) { // redirect to login
            if (c.getName().equals(this.cookieName)) {
                accessToken = c.getValue();
            }
        }
        if (accessToken == null) {
            try {
                response.sendRedirect(this.redirectUrl);
                System.out.println("post redirect");
            } catch (IOException e) {
                throw new LoginRedirectException(e.getMessage());
            }
        }
        try {
            AuthResponse authResponse = this.restService.getAuthResponse(accessToken);
            User user = this.userService.findOrCreateUser(authResponse);
            request.setAttribute("user", user);
            return true;
        } catch (Exception e) {
            throw new ParticipantNotFoundException(e.getMessage());
        }
    }
}
