package com.tamudatathon.bulletin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionQueryService {

    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;

    @Autowired
    public SubmissionQueryService(SubmissionRepository submissionRepository,
        ChallengeRepository challengeRepository) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
    }
    
    public List<Submission> getSubmissionsByName(Long eventId, String name) {
        List<Long> challengeIds = this.challengeRepository.findChallengeIdsByEvent(eventId);
        if (challengeIds.size() == 0) {
            return new ArrayList<>();
        }
        List<Submission> submissions = this.submissionRepository.findByNameIgnoreCaseContaining(name);
        return submissions
            .stream()
            .filter(submission -> challengeIds.contains(submission.getChallenge().getChallengeId()))
            .collect(Collectors.toList());
    }

    /*
    public List<Submission> getSubmissionsByTag(Long eventId, List<String> tags) {
        List<Long> challengeIds = this.challengeRepository.findChallengeIdsByEvent(eventId);
        if (challengeIds.size() == 0) {
            throw new EventNotFoundException(eventId);
        }
        List<Submission> submissions = this.submissionRepository.findByTags(tags);
        return submissions
            .stream()
            .filter(submission -> challengeIds.contains(submission.getChallenge().getChallengeId()))
            .collect(Collectors.toList());
    } */
}
