package com.tamudatathon.bulletin.service;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionQueryService {

    private final SubmissionRepository submissionRepository;
    private final CommonService commonService;

    @Autowired
    public SubmissionQueryService(SubmissionRepository submissionRepository,
        CommonService commonService) {
        this.submissionRepository = submissionRepository;
        this.commonService = commonService;
    }
    
    /*
    public List<Submission> getSubmissionsByName(Long eventId, Long challengeId, String name) {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return this.submissionRepository.findBySimilarName(challengeId, name);
    } */
}
