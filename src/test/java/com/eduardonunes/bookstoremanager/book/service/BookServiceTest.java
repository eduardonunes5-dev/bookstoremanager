package com.eduardonunes.bookstoremanager.book.service;

import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.eduardonunes.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.entity.Book;
import com.eduardonunes.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.eduardonunes.bookstoremanager.books.mapper.BookMapper;
import com.eduardonunes.bookstoremanager.books.repository.BookRepository;
import com.eduardonunes.bookstoremanager.books.service.BookService;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private BookService bookService;

    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();

        authenticatedUser = new AuthenticatedUser("eduardo", "123", "ADMIN");
    }

    @Test
    void whenNewBookThenResponseDTOShouldBeReturned() {
        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponse expectedBookResponse = bookResponseDTOBuilder.buildBookResponse();
        Book expectedCreatedBook = bookMapper.toModel(expectedBookResponse);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(eq(bookRequestDTO.getName()),
                eq(bookRequestDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.empty());

        when(authorService.verifyAndGetIfExists(bookRequestDTO.getAuthorId())).thenReturn(new Author());

        when(publisherService.verifyAndGetIfExists(bookRequestDTO.getAuthorId())).thenReturn(new Publisher());

        when(bookRepository.save(any(Book.class))).thenReturn(expectedCreatedBook);

        BookResponse foundResponse = bookService.create(authenticatedUser, bookRequestDTO);

        assertThat(foundResponse, is(expectedBookResponse));

    }

    @Test
    void whenCreatingAnExistingBookThenAnExceptionShouldBeThrown() {
        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponse expectedBookResponse = bookResponseDTOBuilder.buildBookResponse();
        Book expectedDuplicatedBook = bookMapper.toModel(expectedBookResponse);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByNameAndIsbnAndUser(
                eq(bookRequestDTO.getName()),
                eq(bookRequestDTO.getIsbn()),
                any(User.class))).thenReturn(Optional.of(expectedDuplicatedBook));

        assertThrows(BookAlreadyExistsException.class, ()-> bookService.create(authenticatedUser,bookRequestDTO));

    }
}
