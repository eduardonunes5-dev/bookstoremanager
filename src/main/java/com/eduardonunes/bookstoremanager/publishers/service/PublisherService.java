package com.eduardonunes.bookstoremanager.publishers.service;

import com.eduardonunes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.eduardonunes.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

    private final static PublisherMapper mapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }
}
