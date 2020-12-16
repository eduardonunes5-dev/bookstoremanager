package com.eduardonunes.bookstoremanager.publisher.service;

import com.eduardonunes.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.eduardonunes.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.eduardonunes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.eduardonunes.bookstoremanager.publishers.repository.PublisherRepository;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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


    @Test
    void whenNewPublisherIsSentThenSaveIt() {
        PublisherDTO toBeSaved = publisherDTOBuilder.buildPublisherDTO();
        Publisher toBeSavedModel = publisherMapper.toModel(toBeSaved);

        when(publisherRepository.findByCodeOrName(toBeSaved.getCode(),toBeSaved.getName())).thenReturn(Optional.empty());
        when(publisherRepository.save(toBeSavedModel)).thenReturn(toBeSavedModel);

        PublisherDTO saved = publisherService.create(toBeSaved);

        assertThat(toBeSaved, Is.is(equalTo(saved)));
    }

    @Test
    void whenExistingPublisherIsSentThenAnExceptionShouldBeThrown() {
        PublisherDTO toBeSaved = publisherDTOBuilder.buildPublisherDTO();
        Publisher toBeSavedModel = publisherMapper.toModel(toBeSaved);

        when(publisherRepository.findByCodeOrName(toBeSaved.getCode(),toBeSaved.getName()))
                .thenReturn(Optional.of(toBeSavedModel));

        assertThrows(PublisherAlreadyExistsException.class, ()-> publisherService.create(toBeSaved));
    }

    @Test
    void whenAValidIdIsPassedThenAPublisherShouldBeReturned() {
        PublisherDTO publisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher expected = publisherMapper.toModel(publisherDTO);

        when(publisherRepository.findById(publisherDTO.getId())).thenReturn(Optional.of(expected));

        PublisherDTO found = publisherService.findById(publisherDTO.getId());

        assertThat(publisherDTO, Is.is(found));
    }

    @Test
    void whenAnInvalidIdIsPassedThenAPublisherShouldBeReturned() {
        Long id = 1L;

        when(publisherRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, ()-> publisherService.findById(id));
    }
}
