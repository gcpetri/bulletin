package com.tamudatathon.bulletin.middleware;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.service.RestService;
import com.tamudatathon.bulletin.util.exception.EditingForbiddenException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;

@Component
@Order(3)
public class SubmissionFilter extends OncePerRequestFilter {

    @Autowired
    RestService restService;

    @Autowired
    SubmissionRepository submissionRepository;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
        throws ServletException {
        String path = request.getRequestURI();
        return !(path.contains("/submissions") || path.contains("/comment") || path.contains("/like"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            filterChain.doFilter(request, response);
            return;
        }
        
        System.out.println("Filter: SubmissionFilter");

        if (request.getParameter("submissionId") == null) {
            filterChain.doFilter(request, response);
            return;
        }
        User user = (User) request.getAttribute("user");
        Long submissionId = Long.valueOf(request.getParameter("submissionId")).longValue();
        Submission submission = this.submissionRepository.findById(submissionId)
            .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
        if (!submission.getUsers().contains(user)) {
            throw new EditingForbiddenException("You not allowed to edit this submission");
        }
        filterChain.doFilter(request, response);
    }
}
