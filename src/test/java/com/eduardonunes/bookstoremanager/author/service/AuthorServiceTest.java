package com.eduardonunes.bookstoremanager.author.service;

import com.eduardonunes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.eduardonunes.bookstoremanager.author.exception.AuthorNotFoundException;
import com.eduardonunes.bookstoremanager.author.mapper.AuthorMapper;
import com.eduardonunes.bookstoremanager.author.repository.AuthorRepository;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp(){
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShouldBeCreated(){
        AuthorDTO expectedAuthorToCreate = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreate);
        //
        when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        when(authorRepository.findByName(expectedCreatedAuthor.getName())).thenReturn(Optional.empty());
        AuthorDTO createdAuthorDTO = authorService.create(expectedAuthorToCreate);

        assertThat(createdAuthorDTO, is(equalTo(expectedAuthorToCreate)));
    }

    @Test
    void whenExistingAuthorIsInformedThenAnExceptionShouldBeThrown(){
        AuthorDTO expectedAuthorToCreate = authorDTOBuilder.buildAuthorDTO();
        Author expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreate);

        when(authorRepository.findByName(expectedCreatedAuthor.getName()))
                .thenReturn(Optional.of(expectedCreatedAuthor));

        assertThrows(AuthorAlreadyExistsException.class, ()-> authorService.create(expectedAuthorToCreate));
    }

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        Author expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        when(authorRepository.findById(expectedFoundAuthorDTO.getId()))
                .thenReturn(Optional.of(expectedFoundAuthor));

        AuthorDTO foundAuthorDTO = authorService.findById(expectedFoundAuthorDTO.getId());

        assertThat(foundAuthorDTO, is(equalTo(expectedFoundAuthorDTO)));

    }

    @Test
    void whenNotValidIdIsGivenThenAnExceptionShouldBeThrown() {
        AuthorDTO expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorRepository.findById(expectedFoundAuthorDTO.getId()))
                .thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, ()-> this.authorService.findById(expectedFoundAuthorDTO.getId()));

    }
}
