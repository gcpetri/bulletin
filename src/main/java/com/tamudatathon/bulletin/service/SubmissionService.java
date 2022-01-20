package com.tamudatathon.bulletin.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SubmissionService {
 
    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;
    private final CommonService commonService;
    private final AmazonService amazonService;
 
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService,
        AmazonService amazonService) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
        this.amazonService = amazonService;
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
    public Submission addSubmission(Long eventId, Long challengeId, Long submissionId, Submission submission, User user) throws EventNotFoundException,
        ChallengeNotFoundException, SubmissionNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        submission.setChallenge(challenge);
        if (submissionId != null) {
            Submission oldSubmission = this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionNotFoundException(submissionId));
            submission.setSubmissionId(submissionId);
            submission.setAccolades(oldSubmission.getAccolades());
            submission.setUsers(oldSubmission.getUsers());
        } else {
            List<User> newUsers = new ArrayList<User>();
            newUsers.add(user);
            submission.setUsers(newUsers);
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

    public URL uploadIcon(MultipartFile file, Long eventId, Long challengeId, Long id) throws Exception, 
        EventNotFoundException, ChallengeNotFoundException, SubmissionNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        if (submission.getIconUrl() != null) {
            this.deleteIcon(eventId, challengeId, id);
        }
        URL fileUrl = this.amazonService.uploadFile(file);
        submission.setIconUrl(fileUrl);
        this.submissionRepository.save(submission);
        return fileUrl;
    }

    public void deleteIcon(Long eventId, Long challengeId, Long id) throws Exception, EventNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        this.amazonService.deleteFileFromS3Bucket(submission.getIconUrl());
        submission.setIconUrl(null);
        this.submissionRepository.save(submission);
    }

    public URL uploadSourceCode(MultipartFile file, Long eventId, Long challengeId, Long id) throws Exception, 
        EventNotFoundException, ChallengeNotFoundException, SubmissionNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        if (submission.getSourceCodeUrl() != null) {
            this.deleteSourceCode(eventId, challengeId, id);
        }
        URL fileUrl = this.amazonService.uploadFile(file);
        submission.setSourceCodeUrl(fileUrl);
        this.submissionRepository.save(submission);
        return fileUrl;
    }

    public void deleteSourceCode(Long eventId, Long challengeId, Long id) throws Exception, EventNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        this.amazonService.deleteFileFromS3Bucket(submission.getSourceCodeUrl());
        submission.setSourceCodeUrl(null);
        this.submissionRepository.save(submission);
    }
}

