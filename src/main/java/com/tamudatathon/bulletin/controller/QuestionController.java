package com.tamudatathon.bulletin.controller;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.tamudatathon.bulletin.data.dtos.QuestionDto;
import com.tamudatathon.bulletin.data.entity.Question;
import com.tamudatathon.bulletin.service.QuestionService;
import com.tamudatathon.bulletin.util.exception.ChallengeNotFoundException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.QuestionInvalidException;
import com.tamudatathon.bulletin.util.exception.QuestionNotFoundException;

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
@RequestMapping("${app.api.basepath}/events/{eventId}/challenges/{challengeId}/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public QuestionController(QuestionService questionService,
        ModelMapper modelMapper) {
        this.questionService = questionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value={"", "/", "/list"})
    public List<QuestionDto> getQuestions(@PathVariable Long eventId, @PathVariable Long challengeId)
        throws EventNotFoundException, ChallengeNotFoundException {
        List<Question> questions = this.questionService.getQuestions(eventId, challengeId);
        return questions.stream()
          .map(this::convertToDto)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public QuestionDto getQuestionById(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws QuestionNotFoundException, ChallengeNotFoundException, EventNotFoundException {
        Question question = this.questionService.getQuestion(eventId, challengeId, id);
        return convertToDto(question);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteQuestion(@PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable Long id) throws QuestionNotFoundException, ChallengeNotFoundException, EventNotFoundException {
        this.questionService.deleteQuestion(eventId, challengeId, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value={"/save", "/save/{id}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionDto createQuestion(@RequestBody QuestionDto questionDto,
        @PathVariable Long eventId, @PathVariable Long challengeId,
        @PathVariable(required=false) Long id) throws ChallengeNotFoundException, EventNotFoundException,
        QuestionNotFoundException, QuestionInvalidException {
        try {
            Question question = this.convertToEntity(questionDto);
            Question newQuestion = this.questionService.addQuestion(eventId, challengeId, id, question);
            return convertToDto(newQuestion);
        } catch (Exception e) {
            e.printStackTrace();
            throw new QuestionInvalidException();
        }
    }

    // utils

    private QuestionDto convertToDto(Question question) {
        QuestionDto questionDto = modelMapper.map(question, QuestionDto.class);
        return questionDto;
    }

    private Question convertToEntity(QuestionDto questionDto) throws ParseException {
        Question question = modelMapper.map(questionDto, Question.class);
        return question;
    }    
}
