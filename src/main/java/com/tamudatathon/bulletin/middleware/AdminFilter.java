package com.tamudatathon.bulletin.middleware;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.util.exception.EditingForbiddenException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AdminFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
        throws ServletException {
        String path = request.getRequestURI();
        return path.contains("/submissions");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Filter: Admin Filter");

        User user = (User) request.getAttribute("user");
        if (user.getIsAdmin()) {
            filterChain.doFilter(request, response);
            return;
        }
        throw new EditingForbiddenException("user is not an admin");
    }
}
