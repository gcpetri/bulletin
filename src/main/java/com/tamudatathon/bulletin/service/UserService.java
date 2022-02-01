package com.tamudatathon.bulletin.service;

import java.time.LocalDateTime;

import com.tamudatathon.bulletin.data.entity.Comment;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.data.repository.CommentRepository;
import com.tamudatathon.bulletin.data.repository.UserRepository;
import com.tamudatathon.bulletin.util.exception.EditingForbiddenException;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    @Autowired
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommonService commonService;

    @Autowired
    public UserService(UserRepository userRepository,
        CommentRepository commentRepository,
        CommonService commonService) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commonService = commonService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public User createUser(User user) {
        return this.userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public User findUser(User user) {
        if (user == null) return null;
        return this.userRepository.findByAuthId(user.getAuthId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void toggleLike(Long userId, Long eventId, Long challengeId, Long submissionId) {
        this.commonService.validEventChallengeSubmission(eventId, challengeId, submissionId);
        int count = this.userRepository.getLike(userId, submissionId);
        if (count == 0) {
            this.userRepository.addLike(userId, submissionId);
        } else {
            this.userRepository.deleteLike(userId, submissionId);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Comment addComment(User user, Long eventId, Long challengeId, Long submissionId, Comment comment) {
        Submission submission = this.commonService
            .validEventChallengeSubmission(eventId, challengeId, submissionId);
        comment.setUser(user);
        comment.setSubmission(submission);
        comment.setCreatedOn(LocalDateTime.now());
        return this.commentRepository.save(comment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteComment(Long userId, Long eventId, Long challengeId, Long submissionId, Long commentId) {
        Submission submission = this.commonService
            .validEventChallengeSubmission(eventId, challengeId, submissionId);
        for (Comment comment : submission.getComments()) {
            if (comment.getCommentId() == commentId) {
                if (comment.getUser().getUserId() != userId) {
                    throw new EditingForbiddenException("This comment does not belong to you");
                }
                this.commentRepository.delete(comment);
                submission.removeComment(comment);
                return;
            }
        }
        throw new RecordNotFoundException("Comment", commentId);
    }
}
