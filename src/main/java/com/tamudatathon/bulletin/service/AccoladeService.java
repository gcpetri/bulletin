package com.tamudatathon.bulletin.service;

import java.util.List;

import com.tamudatathon.bulletin.data.entity.Accolade;
import com.tamudatathon.bulletin.data.entity.Challenge;
import com.tamudatathon.bulletin.data.repository.AccoladeRepository;
import com.tamudatathon.bulletin.data.repository.ChallengeRepository;
import com.tamudatathon.bulletin.util.exception.AccoladeInvalidException;
import com.tamudatathon.bulletin.util.exception.AccoladeNotFoundException;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccoladeService {
 
    private final AccoladeRepository accoladeRepository;
    private final ChallengeRepository challengeRepository;
    private final CommonService commonService;
 
    @Autowired
    public AccoladeService(AccoladeRepository accoladeRepository,
        ChallengeRepository challengeRepository,
        CommonService commonService) {
        this.accoladeRepository = accoladeRepository;
        this.challengeRepository = challengeRepository;
        this.commonService = commonService;
    }

    public List<Accolade> getAccolades(Long eventId, Long challengeId) throws EventNotFoundException,
        ChallengeNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        return challenge.getAccolades();
    }

    public Accolade getAccolade(Long eventId, Long challengeId, Long accoladeId) throws EventNotFoundException,
        ChallengeNotFoundException, AccoladeNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Accolade accolade : challenge.getAccolades()) {
            if (accolade.getAccoladeId() == accoladeId) return accolade;
        }
        throw new AccoladeNotFoundException(accoladeId);
    }

    public Accolade getAccoladeById(Long id) throws AccoladeNotFoundException {
        return this.accoladeRepository.findById(id)
            .orElseThrow(() -> new AccoladeNotFoundException(id));
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Accolade addAccolade(Long eventId, Long challengeId, Long accoladeId, Accolade accolade) throws EventNotFoundException,
        ChallengeNotFoundException, AccoladeNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        accolade.setChallenge(challenge);
        if (accoladeId != null) {
            this.accoladeRepository.findById(accoladeId)
                .orElseThrow(() -> new AccoladeNotFoundException(accoladeId));
            accolade.setAccoladeId(accoladeId);
        }
        if (!this.commonService.validHexColor(accolade.getColor())) {
            throw new AccoladeInvalidException("Invalid 6-digit hex color");
        }
        Accolade newAccolde = this.accoladeRepository.save(accolade);
        return newAccolde;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteAccolade(Long challengeId, Long eventId, Long accoladeId) throws EventNotFoundException,
        ChallengeNotFoundException, AccoladeNotFoundException {
        Challenge challenge = this.commonService.validEventAndChallenge(eventId, challengeId);
        for (Accolade accolade : challenge.getAccolades()) {
            if (accolade.getAccoladeId() == accoladeId) {
                challenge.removeAccolade(accolade);
                this.challengeRepository.save(challenge);
                this.accoladeRepository.delete(accolade);
                return;
            }
        }
	    throw new AccoladeNotFoundException(accoladeId);
    }
}
