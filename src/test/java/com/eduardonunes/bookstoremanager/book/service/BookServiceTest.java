package com.eduardonunes.bookstoremanager.book.service;

import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.eduardonunes.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.entity.Book;
import com.eduardonunes.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.eduardonunes.bookstoremanager.books.exception.BookNotFoundException;
import com.eduardonunes.bookstoremanager.books.mapper.BookMapper;
import com.eduardonunes.bookstoremanager.books.repository.BookRepository;
import com.eduardonunes.bookstoremanager.books.service.BookService;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.exception.UserNotFoundException;
import com.eduardonunes.bookstoremanager.users.service.UserService;

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
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

        assertThrows(BookAlreadyExistsException.class, () -> bookService.create(authenticatedUser, bookRequestDTO));

    }

    @Test
    void whenValidUserAndBookIdThenABookShouldBeReturned() {
        BookRequest bookRequest = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponse expectedResponse = bookResponseDTOBuilder.buildBookResponse();
        Book expectedFoundBook = bookMapper.toModel(expectedResponse);
        Long bookId = bookRequest.getId();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(bookId), any(User.class))).thenReturn(Optional.of(expectedFoundBook));

        BookResponse foundResponse = bookService.findBookByIdAndUser(authenticatedUser, bookId);
        assertThat(foundResponse, is(expectedResponse));

    }

    @Test
    void whenNotExistingBookThenAnExceptionShouldBeThrown() {
        BookRequest bookRequest = bookRequestDTOBuilder.buildBookRequestDTO();
        Long bookId = bookRequest.getId();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(bookId), any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookByIdAndUser(authenticatedUser, bookId));
    }

    @Test
    void whenValidUserIsGivenThenAllOfTheirBooksShouldBeReturned() {
        BookResponse expectedBookResponse = bookResponseDTOBuilder.buildBookResponse();
        Book expectedBook = bookMapper.toModel(expectedBookResponse);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class))).thenReturn(Collections.singletonList(expectedBook));

        List<BookResponse> foundList = bookService.findBooksByUser(authenticatedUser);

        assertThat(foundList.size(), is(1));
        assertThat(foundList.get(0), is(expectedBookResponse));

    }

    @Test
    void whenValidUserIsGivenThenEmptyListShouldBeReturned() {
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findAllByUser(any(User.class))).thenReturn(Collections.EMPTY_LIST);

        List<BookResponse> foundList = bookService.findBooksByUser(authenticatedUser);

        assertThat(foundList.size(), is(0));
    }

    @Test
    void whenInvalidUserIsGivenThenAnExceptionShouldBeThrown() {
        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> bookService.findBooksByUser(authenticatedUser));
    }

    @Test
    void whenValidBookIsInformedThenItShouldBeDeleted() {
        BookResponse expectedBookToRemoveDTO = bookResponseDTOBuilder.buildBookResponse();
        Book expectedBookToRemove = bookMapper.toModel(expectedBookToRemoveDTO);
        Long expectedBookToRemoveId = expectedBookToRemove.getId();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToRemoveDTO.getId()), any(User.class))).thenReturn(Optional.of(expectedBookToRemove));
        doNothing().when(bookRepository).deleteByIdAndUser(eq(expectedBookToRemoveId), any(User.class));

        bookService.deleteByIdAndUser(authenticatedUser, expectedBookToRemoveDTO.getId());

        verify(bookRepository, times(1)).deleteByIdAndUser(eq(expectedBookToRemoveId), any(User.class));
    }

    @Test
    void whenDeleteBookWithInvalidBookThenAnExceptionShouldBeThrown() {
        BookResponse expectedBookToRemoveDTO = bookResponseDTOBuilder.buildBookResponse();
        Book expectedBookToRemove = bookMapper.toModel(expectedBookToRemoveDTO);
        Long expectedBookToRemoveId = expectedBookToRemove.getId();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(expectedBookToRemoveDTO.getId()), any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteByIdAndUser(authenticatedUser, expectedBookToRemoveId));
    }

    @Test
    void whenExistingBookIdIsInformedThenItShouldBeUpdated() {
        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponse expectedResponse = bookResponseDTOBuilder.buildBookResponse();
        Book expectedResponseModel = bookMapper.toModel(expectedResponse);

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(bookRequestDTO.getId()),
                any(User.class))).thenReturn(Optional.of(expectedResponseModel));
        when(authorService.verifyAndGetIfExists(bookRequestDTO.getAuthorId())).thenReturn(new Author());
        when(publisherService.verifyAndGetIfExists(bookRequestDTO.getAuthorId())).thenReturn(new Publisher());
        when(bookRepository.save(any(Book.class))).thenReturn(expectedResponseModel);

        BookResponse updatedBookResponse = bookService.updateByUserAndId(authenticatedUser, bookRequestDTO, bookRequestDTO.getId());

        assertThat(updatedBookResponse, is(expectedResponse));
    }

    @Test
    void whenNotExistingBookIdIsInformedThenAnExceptionShouldBeThrown() {
        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();

        when(userService.verifyAndGetUserIfExists(authenticatedUser.getUsername())).thenReturn(new User());
        when(bookRepository.findByIdAndUser(eq(bookRequestDTO.getId()),
                any(User.class))).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateByUserAndId(authenticatedUser,bookRequestDTO, bookRequestDTO.getId()));
    }
}