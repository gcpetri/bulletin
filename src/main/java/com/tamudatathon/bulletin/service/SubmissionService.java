package com.tamudatathon.bulletin.service;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmissionService {
 
    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;
    private final CommonService commonService;
 
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
    }

    public List<Submission> getSubmissions(Long eventId, Long challengeId) throws EventNotFoundException,
        ChallengeNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return challenge.getSubmissions();
    }

    public Submission getSubmission(Long eventId, Long challengeId, Long submissionId) throws EventNotFoundException,
        ChallengeNotFoundException, SubmissionNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Submission submission : challenge.getSubmissions()) {
            if (submission.getSubmissionId() == submissionId) return submission;
        }
        throw new SubmissionNotFoundException(submissionId);
    }

    public Submission getSubmissionById(Long id) throws SubmissionNotFoundException {
        return this.submissionRepository.findById(id)
            .orElseThrow(() -> new SubmissionNotFoundException(id));
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Submission addSubmission(Long eventId, Long challengeId, Long submissionId, Submission submission) throws EventNotFoundException,
        ChallengeNotFoundException, SubmissionNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        submission.setChallenge(challenge);
        if (submissionId != null) {
            this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
            submission.setSubmissionId(submissionId);
        }
        Submission newSubmission = this.submissionRepository.save(submission);
        return newSubmission;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteSubmission(Long challengeId, Long eventId, Long submissionId) throws EventNotFoundException,
        ChallengeNotFoundException, SubmissionNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Submission submission : challenge.getSubmissions()) {
            if (submission.getSubmissionId() == submissionId) {
                challenge.removeSubmission(submission);
                this.challengeRepository.save(challenge);
                this.submissionRepository.delete(submission);
                return;
            }
        }
	    throw new SubmissionNotFoundException(submissionId);
    }
}

