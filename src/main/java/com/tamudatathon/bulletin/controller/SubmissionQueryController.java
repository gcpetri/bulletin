package com.tamudatathon.bulletin.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.dtos.SubmissionDto;
import com.tamudatathon.bulletin.data.entity.Submission;
import com.tamudatathon.bulletin.service.SubmissionQueryService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("${app.api.basepath}/events/{eventId}/submissions")
public class SubmissionQueryController {
    
    private final SubmissionQueryService submissionQueryService;
    private final ModelMapper modelMapper;

    @Autowired
    public SubmissionQueryController(ModelMapper modelMapper,
        SubmissionQueryService submissionQueryService) {
        this.submissionQueryService = submissionQueryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public List<SubmissionDto> getSubmissionsByName(@PathVariable Long eventId, @RequestParam String name) {
        return this.submissionQueryService.getSubmissionsByName(eventId, name)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // utils

    private SubmissionDto convertToDto(Submission submission) {
        SubmissionDto submissionDto = modelMapper.map(submission, SubmissionDto.class);
        submissionDto.setCreatedOn(submission.getCreatedOn());
        submissionDto.setUpdatedOn(submission.getUpdatedOn());
        return submissionDto;
    }
}
