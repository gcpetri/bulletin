package com.tamudatathon.bulletin.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.tamudatathon.bulletin.data.dtos.CommentDto;
import com.tamudatathon.bulletin.data.entity.Comment;
import com.tamudatathon.bulletin.data.entity.User;
import com.tamudatathon.bulletin.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.basepath}/events/{eventId}/challenges/{challengeId}/submissions/{submissionId}")
public class SocialController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;
    
    @RequestMapping(value={"/like"},
        method={RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Object> toggleLike(@RequestAttribute("user") User user, 
        @PathVariable Long eventId, @PathVariable Long challengeId, @PathVariable Long submissionId) {
        this.userService.toggleLike(user.getUserId(), eventId, challengeId, submissionId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value={"/comment"},
        method={RequestMethod.POST, RequestMethod.PUT})
    public CommentDto addComment(@RequestAttribute("user") User user, @PathVariable Long eventId, 
        @PathVariable Long challengeId, @PathVariable Long submissionId,
        @RequestBody CommentDto commentDto) throws ParseException {
        Comment comment = this.convertCommentToEntity(commentDto);
        return this.convertCommentToDto(this.userService
            .addComment(user, eventId, challengeId, submissionId, comment));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@RequestAttribute("user") User user, @PathVariable Long eventId,
        @PathVariable Long challengeId, @PathVariable Long submissionId, @PathVariable Long commentId) {
        this.userService.deleteComment(user.getUserId(), eventId, challengeId, submissionId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // utils

    private CommentDto convertCommentToDto(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
        commentDto.setCreatedOn(comment.getCreatedOn());
        return commentDto;
    }

    private Comment convertCommentToEntity(CommentDto commentDto) throws ParseException {
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setCreatedOn(LocalDateTime.now(ZoneId.of("America/Chicago")));
        return comment;
    }
}
