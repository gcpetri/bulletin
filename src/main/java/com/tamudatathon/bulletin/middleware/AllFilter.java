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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AllFilter extends OncePerRequestFilter {
    
    @Autowired
    UserService userService;

    @Autowired
    RestService restService;

    @Value("${app.api.auth.cookieName}")
    private String cookieName;

    @Value("${app.api.auth.redirectUrl}")
    private String redirectUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        
        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Filter: All Filter");

        // if there are not cookies
        String accessToken = "";
        if (request.getCookies() == null) {
            try {
                System.out.println("redirecting");
                response.sendRedirect(this.redirectUrl);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                throw new LoginRedirectException(e.getMessage());
            }
        }

        // if the cookie is not present
        for (Cookie c : request.getCookies()) { // redirect to login
            if (c.getName().equals(this.cookieName)) {
                accessToken = c.getValue();
            }
        }
        if (accessToken == null) {
            try {
                response.sendRedirect(this.redirectUrl);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                throw new LoginRedirectException(e.getMessage());
            }
        }

        // if the cookie is valid
        try {
            User user = this.restService.getUserFromAccessToken(accessToken);
            User oldUser = this.userService.findUser(user);
            if (oldUser == null) {
                User newUser = this.userService.createUser(user);
                request.setAttribute("user", newUser);
                filterChain.doFilter(request, response);
            }
            request.setAttribute("user", oldUser);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            throw new ParticipantNotFoundException(e.getMessage());
        }
        
    }
}
