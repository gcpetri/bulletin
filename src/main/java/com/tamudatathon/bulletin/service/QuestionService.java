package com.tamudatathon.bulletin.service;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Question;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.QuestionRepository;
import com.tamudatathon.bulletin.util.exception.RecordFormatInvalidException;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {
 
    private final QuestionRepository questionRepository;
    private final ChallengeRepository challengeRepository;
    private final CommonService commonService;
 
    @Autowired
    public QuestionService(QuestionRepository questionRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService) {
        this.questionRepository = questionRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
    }

    public List<Question> getQuestions(Long eventId, Long challengeId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return challenge.getQuestions();
    }

    public Question getQuestion(Long eventId, Long challengeId, Long questionId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Question question : challenge.getQuestions()) {
            if (question.getQuestionId() == questionId) return question;
        }
        throw new RecordNotFoundException("Question", questionId);
    }

    public Question getQuestionById(Long id) throws RecordNotFoundException {
        return this.questionRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Question", id));
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Question addQuestion(Long eventId, Long challengeId, Long questionId, Question question) throws RecordNotFoundException,
        RecordFormatInvalidException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        question.setChallenge(challenge);
        if (questionId != null) {
            this.questionRepository.findById(questionId)
                .orElseThrow(() -> new RecordNotFoundException("Question", questionId));
            question.setQuestionId(questionId);
        }
        Question newQuestion = this.questionRepository.save(question);
        return newQuestion;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteQuestion(Long challengeId, Long eventId, Long questionId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Question question : challenge.getQuestions()) {
            if (question.getQuestionId() == questionId) {
                challenge.removeQuestion(question);
                this.challengeRepository.save(challenge);
                this.questionRepository.delete(question);
                return;
            }
        }
	    throw new RecordNotFoundException("Question", questionId);
    }
}

