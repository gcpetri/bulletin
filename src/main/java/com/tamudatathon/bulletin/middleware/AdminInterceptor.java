package com.tamudatathon.bulletin.middleware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.util.exception.EditingForbiddenException;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse  response, Object handler) {
        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            return true;
        }
        User user = (User) request.getAttribute("user");
        if (user.getIsAdmin()) return true;
        throw new EditingForbiddenException("user is not an admin");
    }
}
