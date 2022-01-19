package com.tamudatathon.bulletin.controller;

import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;

import com.tamudatathon.bulletin.data.dtos.EventDto;
import com.tamudatathon.bulletin.data.entity.Event;
import com.tamudatathon.bulletin.service.EventService;
import com.tamudatathon.bulletin.util.exception.EventInvalidException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.FileDeleteException;
import com.tamudatathon.bulletin.util.exception.FileUploadException;

@RestController
@RequestMapping("${app.api.basepath}/events")
public class EventController {

    private final EventService eventService;
    private final ModelMapper modelMapper;
    
    @Autowired
    public EventController(EventService eventService,
        ModelMapper modelMapper) {
        this.eventService = eventService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value={"", "/", "/list"})
    public List<EventDto> getEvents() {
        List<Event> events = this.eventService.getEvents();
        return events.stream()
          .map(this::convertToDto)
          .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EventDto getEventById(@PathVariable Long id) throws EventNotFoundException {
        Event event = this.eventService.getEvent(id);
        return convertToDto(event);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteEvent(@PathVariable Long id) throws EventNotFoundException {
        this.eventService.deleteEvent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value={"/save", "/save/{id}"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(
        @RequestBody EventDto eventDto, @PathVariable(required=false) Long id) throws EventInvalidException {
        try {
            Event event = convertToEntity(eventDto);
            Event newEvent = this.eventService.addEvent(id, event);
            return convertToDto(newEvent);
        } catch (Exception e) {
            e.printStackTrace();
            throw new EventInvalidException();
        }
    }

    @RequestMapping(value={"/{id}/image/save"},
        method={RequestMethod.POST, RequestMethod.PUT})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> uploadImage(@RequestPart(value = "file") MultipartFile file, @PathVariable Long id) 
        throws FileUploadException {
        try {
            URL url = this.eventService.uploadImage(file, id);
            JSONObject resp = new JSONObject();
            resp.put("url", url.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/image/delete")
    public ResponseEntity<Object> deleteImage(@PathVariable Long id) throws FileDeleteException {
        try {
            this.eventService.deleteImage(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            throw new FileDeleteException(e.getMessage());
        }
    }

    // utils

    private EventDto convertToDto(Event event) {
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setStartTime(event.getStartTime());
        eventDto.setEndTime(event.getEndTime());
        return eventDto;
    }

    private Event convertToEntity(EventDto eventDto) throws ParseException {
        Event event = modelMapper.map(eventDto, Event.class);
        event.setStartTime(eventDto.getStartTime());
        event.setEndTime(eventDto.getEndTime()); 
        if (eventDto.getId() != null) {
            Event oldEvent = this.eventService.getEvent(eventDto.getId());
            event.setEventId(oldEvent.getEventId());
        }
        return event;
    }
}
