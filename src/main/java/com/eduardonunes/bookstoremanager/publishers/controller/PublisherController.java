package com.eduardonunes.bookstoremanager.publishers.controller;

import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/publishers")
public class PublisherController implements  PublisherControllerDocs{

    private PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDTO create(@RequestBody @Valid PublisherDTO publisherDTO){
        return publisherService.create(publisherDTO);
    }
}