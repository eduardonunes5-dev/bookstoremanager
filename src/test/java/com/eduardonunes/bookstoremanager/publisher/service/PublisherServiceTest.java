package com.eduardonunes.bookstoremanager.publisher.service;

import com.eduardonunes.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.eduardonunes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.eduardonunes.bookstoremanager.publishers.repository.PublisherRepository;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    private final PublisherMapper publisherMapper = PublisherMapper.INSTANCE;

    @Mock
    private  PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherService publisherService;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
    }
}
