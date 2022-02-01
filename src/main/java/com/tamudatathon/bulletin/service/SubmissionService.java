package com.tamudatathon.bulletin.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.data.repository.UserRepository;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;
import com.tamudatathon.bulletin.util.exception.S3Exception;

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
    private final RestService restService;
    private final UserRepository userRepository;
 
    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService,
        AmazonService amazonService,
        RestService restService,
        UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
        this.amazonService = amazonService;
        this.restService = restService;
        this.userRepository = userRepository;
    }

    public List<Submission> getSubmissions(Long eventId, Long challengeId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return challenge.getSubmissions();
    }

    public Submission getSubmission(Long eventId, Long challengeId, Long submissionId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Submission submission : challenge.getSubmissions()) {
            if (submission.getSubmissionId() == submissionId) return submission;
        }
        throw new RecordNotFoundException("Submission", submissionId);
    }

    public Submission getSubmissionById(Long id) throws RecordNotFoundException {
        return this.submissionRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Submission", id));
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Submission addSubmission(Long eventId, Long challengeId, Long submissionId, Submission submission, User user) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        submission.setChallenge(challenge);
        if (submissionId != null) {
            Submission oldSubmission = this.submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RecordNotFoundException("Submission", submissionId));
            submission.setSubmissionId(submissionId);
            submission.setAccolades(oldSubmission.getAccolades());
            submission.setUsers(oldSubmission.getUsers());
            submission.setCreatedOn(oldSubmission.getCreatedOn());
        } else {
            if (!submission.getUsers().contains(user)) {
                submission.addUser(user);
            }
        }
        Submission newSubmission = this.submissionRepository.save(submission);
        return newSubmission;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteSubmission(Long challengeId, Long eventId, Long submissionId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Submission submission : challenge.getSubmissions()) {
            if (submission.getSubmissionId() == submissionId) {
                challenge.removeSubmission(submission);
                this.challengeRepository.save(challenge);
                this.submissionRepository.delete(submission);
                return;
            }
        }
	    throw new RecordNotFoundException("Submission", submissionId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public URL uploadIcon(MultipartFile file, Long eventId, Long challengeId, Long id) throws RecordNotFoundException, S3Exception {
        if (!file.getContentType().contains("image")) {
            throw new S3Exception("Uploading", "File is not an image");
        }
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        if (submission.getIconUrl() != null) {
            this.deleteIcon(eventId, challengeId, id);
        }
        URL fileUrl;
        try {
            fileUrl = this.amazonService.uploadFile(file);
        } catch (Exception e) {
            throw new S3Exception("Uploading", e.getMessage());
        }
        submission.setIconUrl(fileUrl);
        this.submissionRepository.save(submission);
        return fileUrl;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteIcon(Long eventId, Long challengeId, Long id) throws RecordNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        this.amazonService.deleteFileFromS3Bucket(submission.getIconUrl());
        submission.setIconUrl(null);
        this.submissionRepository.save(submission);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public URL uploadSourceCode(MultipartFile file, Long eventId, Long challengeId, Long id) throws RecordNotFoundException, S3Exception {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        if (!file.getOriginalFilename().endsWith(".zip") && !file.getOriginalFilename().endsWith(".tar")) {
            throw new S3Exception("Uploading", "source code must be a zip or tar file");
        }
        if (submission.getSourceCodeUrl() != null) {
            this.deleteSourceCode(eventId, challengeId, id);
        }
        URL fileUrl;
        try {
            fileUrl = this.amazonService.uploadFile(file);
        } catch (Exception e) {
            throw new S3Exception("Uploading", e.getMessage());
        }
        submission.setSourceCodeUrl(fileUrl);
        this.submissionRepository.save(submission);
        return fileUrl;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteSourceCode(Long eventId, Long challengeId, Long id) throws RecordNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        this.amazonService.deleteFileFromS3Bucket(submission.getSourceCodeUrl());
        submission.setSourceCodeUrl(null);
        this.submissionRepository.save(submission);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public List<User> addUsers(Long eventId, Long challengeId, Long id, List<String> discordInfoList) {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        Set<User> users = submission.getUsers();
        List<User> oldUsers = this.userRepository.findInDiscordInfoList(discordInfoList);
        oldUsers.removeAll(users);

        // remove users that are already in the submission
        for (User user : users) {
            discordInfoList.remove(user.getDiscordInfo());
        }

        // add the existing users
        List<User> newUsers = new ArrayList<>();
        for (User user : oldUsers) {
            this.userRepository.createUserSubmission(user.getUserId(), submission.getSubmissionId());
            discordInfoList.remove(user.getDiscordInfo());
            newUsers.add(user);
        }

        // create new users
        for (String discordInfo : discordInfoList) {
            User newUser = this.restService.getUserFromDiscordInfo(discordInfo);
            User savedUser = this.userRepository.save(newUser);
            this.userRepository.createUserSubmission(savedUser.getUserId(), submission.getSubmissionId());
            newUsers.add(savedUser);
        }

        return Stream.concat(users.stream(), newUsers.stream()).collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public List<User> deleteUsers(Long eventId, Long challengeId, Long id, List<String> discordInfo, User thisUser) {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, id);
        List<User> users = new ArrayList<>();
        List<User> oldUsers = new ArrayList<>();
        for (User user : submission.getUsers()) {
            if (discordInfo.contains(user.getDiscordInfo()) && 
                !user.getDiscordInfo().equals(thisUser.getDiscordInfo())) { // can't delete yourself
                oldUsers.add(user);
            } else {
                users.add(user);
            }
        }

        for (User user : oldUsers) {
            this.userRepository.deleteUserSubmission(user.getUserId(), submission.getSubmissionId());
        }
        return users;
    }
}

