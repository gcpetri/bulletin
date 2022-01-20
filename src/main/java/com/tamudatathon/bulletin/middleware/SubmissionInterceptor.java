package com.tamudatathon.bulletin.middleware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.service.RestService;
import com.tamudatathon.bulletin.util.exception.EditingForbiddenException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;

@Component
public class SubmissionInterceptor implements HandlerInterceptor {

    @Autowired
    RestService restService;

    @Autowired
    SubmissionRepository submissionRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse  response, Object handler) {
        if (request.getMethod().equals("GET")) { // we don't care who GETs stuff
            return true;
        }
        if (request.getParameter("submissionId") == null) {
            return true;
        }
        User user = (User) request.getAttribute("user");
        Long submissionId = Long.valueOf(request.getParameter("submissionId")).longValue();
        Submission submission = this.submissionRepository.findById(submissionId)
            .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
        if (!submission.getUsers().contains(user)) {
            throw new EditingForbiddenException("You not allowed to edit this submission");
        }
        return true;
    }
}
