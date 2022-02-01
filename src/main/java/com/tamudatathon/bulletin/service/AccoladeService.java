package com.tamudatathon.bulletin.service;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Accolade;
import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.repository.AccoladeRepository;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.data.repository.SubmissionRepository;
import com.tamudatathon.bulletin.util.exception.RecordFormatInvalidException;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccoladeService {
 
    private final AccoladeRepository accoladeRepository;
    private final ChallengeRepository challengeRepository;
    private final SubmissionRepository submissionRepository;
    private final CommonService commonService;
 
    @Autowired
    public AccoladeService(AccoladeRepository accoladeRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService,
        SubmissionRepository submissionRepository) {
        this.accoladeRepository = accoladeRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
        this.submissionRepository = submissionRepository;
    }

    public List<Accolade> getAccolades(Long eventId, Long challengeId) throws RecordNotFoundException,
        RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return challenge.getAccolades();
    }

    public Accolade getAccolade(Long eventId, Long challengeId, Long accoladeId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Accolade accolade : challenge.getAccolades()) {
            if (accolade.getAccoladeId() == accoladeId) return accolade;
        }
        throw new RecordNotFoundException("Accolade", accoladeId);
    }

    public Accolade getAccoladeById(Long id) throws RecordNotFoundException {
        return this.accoladeRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Accolade", id));
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Accolade addAccolade(Long eventId, Long challengeId, Long accoladeId, Accolade accolade) throws RecordNotFoundException,
        RecordFormatInvalidException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        accolade.setChallenge(challenge);
        if (accoladeId != null) {
            this.accoladeRepository.findById(accoladeId)
                .orElseThrow(() -> new RecordNotFoundException("Accolade", accoladeId));
            accolade.setAccoladeId(accoladeId);
        }
        if (!this.commonService.validHexColor(accolade.getColor())) {
            throw new RecordFormatInvalidException("Invalid 6-digit hex color");
        }
        Accolade newAccolde = this.accoladeRepository.save(accolade);
        return newAccolde;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteAccolade(Long eventId, Long challengeId, Long accoladeId) throws RecordNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Accolade accolade : challenge.getAccolades()) {
            if (accolade.getAccoladeId() == accoladeId) {
                challenge.removeAccolade(accolade);
                this.challengeRepository.save(challenge);
                this.accoladeRepository.delete(accolade);
                return;
            }
        }
	    throw new RecordNotFoundException("Accolade", accoladeId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public HttpStatus toggleAccoladeToSubmission(Long eventId, Long challengeId, Long accoladeId, Long submissionId)
        throws RecordNotFoundException {
        Submission submission = this.commonService.validEventChallengeSubmission(eventId, challengeId, submissionId);
        Accolade accolade = this.accoladeRepository.findById(accoladeId)
            .orElseThrow(() -> new RecordNotFoundException("Accolade", accoladeId));
        if (accolade.getChallenge().getChallengeId() != challengeId) {
            throw new RecordNotFoundException("Challenge", challengeId);
        }
        if (submission.getAccolades().contains(accolade)) {
            submission.removeAccolade(accolade);
            this.submissionRepository.save(submission);
            return HttpStatus.NO_CONTENT;
        }
        submission.addAccolade(accolade);
        this.submissionRepository.save(submission);
        return HttpStatus.OK;
    }
}
