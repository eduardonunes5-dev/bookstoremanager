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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Test
    void whenFindAllThenReturnAllPublisherRecords() {
        PublisherDTO publisherDTO = publisherDTOBuilder.buildPublisherDTO();
        Publisher publisher = publisherMapper.toModel(publisherDTO);

        when(publisherRepository.findAll()).thenReturn(Collections.singletonList(publisher));

        List<PublisherDTO> foundList = publisherService.findAll();

        assertThat(foundList.size(), is(1));
        assertThat(foundList.get(0), is(publisherDTO));
    }

    @Test
    void whenFindAllThenReturnEmptyList() {
        when(publisherRepository.findAll()).thenReturn(Collections.emptyList());

        List<PublisherDTO> foundList = publisherService.findAll();

        assertThat(foundList.size(), is(0));
    }

    @Test
    void whenRemoveByIdThenRemoveIt() {
        PublisherDTO publisherDTOToRemove = publisherDTOBuilder.buildPublisherDTO();
        Publisher toRemove = publisherMapper.toModel(publisherDTOToRemove);
        Long publisherToRemoveId = toRemove.getId();

        doNothing().when(publisherRepository).deleteById(publisherToRemoveId);
        when(publisherRepository.findById(publisherToRemoveId)).thenReturn(Optional.of(toRemove));
        publisherService.removeById(publisherToRemoveId);

        verify(publisherRepository, times(1)).deleteById(publisherToRemoveId);
        verify(publisherRepository, times(1)).findById(publisherToRemoveId);
    }

    @Test
    void whenRemoveByIdWithInvalidIdThenThrowAnException() {
        Long publisherToRemoveId = 1L;

        when(publisherRepository.findById(publisherToRemoveId)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> publisherService.removeById(publisherToRemoveId));
    }
}
