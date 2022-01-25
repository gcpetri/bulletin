package com.tamudatathon.bulletin.service;

import java.net.URL;
import java.util.List;

import com.tamudatathon.bulletin.data.entity.Event;
import com.tamudatathon.bulletin.data.repository.EventRepository;
import com.tamudatathon.bulletin.util.exception.EventInvalidException;
import com.tamudatathon.bulletin.util.exception.EventNotFoundException;
import com.tamudatathon.bulletin.util.exception.FileUploadException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventService {
 
    private final EventRepository eventRepository;
    private final CommonService commonService;
    private final AmazonService amazonService;
 
    @Autowired
    public EventService(EventRepository eventRepository,
        CommonService commonService,
        AmazonService amazonService) {
        this.eventRepository = eventRepository;
        this.commonService = commonService;
        this.amazonService = amazonService;
    }

    public List<Event> getEvents() {
        return this.eventRepository.findAll();
    }

    public Event getEvent(Long id) throws EventNotFoundException {
        return this.eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Event addEvent(Long id, Event event) {
        if (id != null) {
            this.eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
            event.setEventId(id);
        }
        if (!this.commonService.validHexColor(event.getColor())) {
            throw new EventInvalidException();
        }
        Event newEvent = this.eventRepository.save(event);
        return newEvent;
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteEvent(Long id) throws EventNotFoundException {
        try {
            this.eventRepository.deleteById(id);
        } catch (Exception e) {
            throw new EventNotFoundException(id);
        }
    }

    public URL uploadImage(MultipartFile file, Long id) throws Exception, EventNotFoundException {
        if (!file.getContentType().contains("image")) {
            throw new FileUploadException("File is not an image");
        }
        Event event = this.eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
        if (event.getImageUrl() != null) {
            this.deleteImage(id);
        }
        URL fileUrl = this.amazonService.uploadFile(file);
        event.setImageUrl(fileUrl);
        this.eventRepository.save(event);
        return fileUrl;
    }

    public void deleteImage(Long id) throws Exception, EventNotFoundException {
        Event event = this.eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException(id));
        this.amazonService.deleteFileFromS3Bucket(event.getImageUrl());
        event.setImageUrl(null);
        this.eventRepository.save(event);
    }
}
