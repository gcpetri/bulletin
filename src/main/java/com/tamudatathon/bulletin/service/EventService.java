package com.tamudatathon.bulletin.service;

import java.net.URL;
import java.util.List;

import com.tamudatathon.bulletin.data.entity.Event;
import com.tamudatathon.bulletin.data.repository.EventRepository;
import com.tamudatathon.bulletin.util.exception.RecordFormatInvalidException;
import com.tamudatathon.bulletin.util.exception.RecordNotFoundException;
import com.tamudatathon.bulletin.util.exception.S3Exception;

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

    public Event getEvent(Long id) throws RecordNotFoundException {
        return this.eventRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Event", id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public Event addEvent(Long id, Event event) {
        if (id != null) {
            this.eventRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Event", id));
            event.setEventId(id);
        }
        if (!this.commonService.validHexColor(event.getColor())) {
            throw new RecordFormatInvalidException("Invalid 6-digit hex color");
        }
        Event newEvent = this.eventRepository.save(event);
        return newEvent;
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW,
        rollbackFor = Exception.class)
    public void deleteEvent(Long id) throws RecordNotFoundException {
        try {
            this.eventRepository.deleteById(id);
        } catch (Exception e) {
            throw new RecordNotFoundException("Event", id);
        }
    }

    public URL uploadImage(MultipartFile file, Long id) throws S3Exception, RecordNotFoundException {
        if (!file.getContentType().contains("image")) {
            throw new S3Exception("Uploading", "File is not an image");
        }
        Event event = this.eventRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Event", id));
        if (event.getImageUrl() != null) {
            this.deleteImage(id);
        }
        URL fileUrl;
        try {
            fileUrl = this.amazonService.uploadFile(file);
        } catch (Exception e) {
            throw new S3Exception("Uploading", e.getMessage());
        }
        event.setImageUrl(fileUrl);
        this.eventRepository.save(event);
        return fileUrl;
    }

    public void deleteImage(Long id) throws RecordNotFoundException {
        Event event = this.eventRepository.findById(id)
            .orElseThrow(() -> new RecordNotFoundException("Event", id));
        this.amazonService.deleteFileFromS3Bucket(event.getImageUrl());
        event.setImageUrl(null);
        this.eventRepository.save(event);
    }
}
