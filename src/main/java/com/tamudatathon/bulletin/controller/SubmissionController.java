package com.tamudatathon.bulletin.controller;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.dtos.SubmissionDto;
import com.tamudatathon.bulletin.data.dtos.mapping.property.PropertyMaps;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.service.SubmissionService;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.FileDeleteException;
import com.tamudatathon.bulletin.util.exception.FileUploadException;
import com.tamudatathon.bulletin.util.exception.SubmissionInvalidException;
import com.tamudatathon.bulletin.util.exception.SubmissionNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("${app.api.basepath}/events/{eventId}/challenges/{challengeId}/submissions")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    private final ModelMapper modelMapper;
    private final PropertyMaps propertyMaps;
    
    @Autowired
    public SubmissionController(SubmissionService submissionService,
        ModelMapper modelMapper,
        PropertyMaps propertyMaps) {
        this.submissionService = submissionService;
        this.modelMapper = modelMapper;
        this.propertyMaps = propertyMaps;
        this.modelMapper.addMappings(this.propertyMaps.getUserToDtoMap());
        this.modelMapper.addMappings(this.propertyMaps.getUserFromDtoMap());
    }

    @GetMapping(value={"", "/", "/list"})
    public List<SubmissionDto> getSubmissions(@PathVariable Long eventId, @PathVariable Long challengeId)
        throws EventNotFoundException, ChallengeNotFoundException {
        System.out.println("getting accolades");
        List<Submission> submissions = this.submissionService.getSubmissions(eventId, challengeId);
        return submissions.stream()
          .map(this::convertToDto)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubmissionDto getSubmissionById(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws SubmissionNotFoundException, ChallengeNotFoundException, EventNotFoundException {
        Submission submission = this.submissionService.getSubmission(eventId, challengeId, id);
        return convertToDto(submission);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteSubmission(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws SubmissionNotFoundException, ChallengeNotFoundException, EventNotFoundException {
        this.submissionService.deleteSubmission(eventId, challengeId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value={"/save", "/save/{id}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public SubmissionDto createSubmission(@RequestAttribute("user") Object userAttr, @RequestBody SubmissionDto submissionDto,
        @PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable(required=false) Long id) throws ChallengeNotFoundException, EventNotFoundException,
        SubmissionNotFoundException, SubmissionInvalidException {
        try {
            User user = (User) userAttr;
            Submission submission = this.convertToEntity(submissionDto);
            Submission newSubmission = this.submissionService.addSubmission(eventId, challengeId, id, submission, user);
            return convertToDto(newSubmission);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SubmissionInvalidException(e.getMessage());
        }
    }

    @RequestMapping(value={"/{id}/icon/save"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadIcon(@RequestPart(value = "icon") MultipartFile file, @PathVariable Long eventId,
        @PathVariable Long challengeId, @PathVariable Long id) 
        throws FileUploadException {
        try {
            URL url = this.submissionService.uploadIcon(file, eventId, challengeId, id);
            JSONObject resp = new JSONObject();
            resp.put("url", url.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/icon/delete")
    public ResponseEntity<Object> deleteIcon(@PathVariable Long eventId, 
        @PathVariable Long challengeId, @PathVariable Long id) 
        throws FileDeleteException {
        try {
            this.submissionService.deleteIcon(eventId, challengeId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    @RequestMapping(value={"/{id}/source-code/save"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadSourceCode(@RequestPart(value = "source-code") MultipartFile file, @PathVariable Long eventId,
        @PathVariable Long challengeId, @PathVariable Long id) 
        throws EventNotFoundException, ChallengeNotFoundException {
        try {
            URL url = this.submissionService.uploadSourceCode(file, eventId, challengeId, id);
            JSONObject resp = new JSONObject();
            resp.put("url", url.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/source-code/delete")
    public ResponseEntity<Object> deleteSourceCode(@PathVariable Long eventId, 
        @PathVariable Long challengeId, @PathVariable Long id) 
        throws FileDeleteException {
        try {
            this.submissionService.deleteSourceCode(eventId, challengeId, id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    // utils

    private SubmissionDto convertToDto(Submission submission) {
        SubmissionDto submissionDto = modelMapper.map(submission, SubmissionDto.class);
        return submissionDto; 
    }

    private Submission convertToEntity(SubmissionDto submissionDto) throws ParseException {
        Submission submission = modelMapper.map(submissionDto, Submission.class);
        if (submissionDto.getId() != null) {
            Submission oldSubmission = this.submissionService.getSubmissionById(submissionDto.getId());
            submission.setSubmissionId(oldSubmission.getSubmissionId());
        }
        return submission;
    }
}
