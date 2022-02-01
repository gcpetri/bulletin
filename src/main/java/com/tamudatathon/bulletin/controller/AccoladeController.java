package com.tamudatathon.bulletin.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.dtos.AccoladeDto;
import com.tamudatathon.bulletin.data.entity.Accolade;
import com.tamudatathon.bulletin.service.AccoladeService;
import com.tamudatathon.bulletin.util.exception.RecordFormatInvalidException;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.basepath}/events/{eventId}/challenges/{challengeId}/accolades")
public class AccoladeController {

    private final AccoladeService accoladeService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public AccoladeController(AccoladeService accoladeService,
        ModelMapper modelMapper) {
        this.accoladeService = accoladeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value={"", "/", "/list"})
    public List<AccoladeDto> getAccolades(@PathVariable Long eventId, @PathVariable Long challengeId)
        throws RecordNotFoundException {
        List<Accolade> accolades = this.accoladeService.getAccolades(eventId, challengeId);
        return accolades.stream()
          .map(this::convertToDto)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccoladeDto getAccoladeById(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws RecordNotFoundException {
        Accolade accolade = this.accoladeService.getAccolade(eventId, challengeId, id);
        return convertToDto(accolade);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteAccolade(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws RecordNotFoundException {
        this.accoladeService.deleteAccolade(eventId, challengeId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value={"/save", "/save/{id}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public AccoladeDto createAccolade(@RequestBody AccoladeDto accoladeDto,
        @PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable(required=false) Long id) throws RecordNotFoundException, RecordFormatInvalidException {
        try {
            Accolade accolade = this.convertToEntity(accoladeDto);
            Accolade newAccolade = this.accoladeService.addAccolade(eventId, challengeId, id, accolade);
            return convertToDto(newAccolade);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RecordFormatInvalidException(e.getMessage());
        }
    }

    @RequestMapping(value={"/{id}/submissions/{submissionId}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> toggleAccoladeToSubmission(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id, @PathVariable Long submissionId) throws RecordNotFoundException {
        HttpStatus httpCode = this.accoladeService.toggleAccoladeToSubmission(eventId, challengeId, id, submissionId);
        return ResponseEntity.status(httpCode).build();
    }

    // utils

    private AccoladeDto convertToDto(Accolade accolade) {
        AccoladeDto accoladeDto = modelMapper.map(accolade, AccoladeDto.class);
        return accoladeDto;
    }

    private Accolade convertToEntity(AccoladeDto accoladeDto) throws ParseException {
        Accolade accolade = modelMapper.map(accoladeDto, Accolade.class);
        return accolade;
    }
}
