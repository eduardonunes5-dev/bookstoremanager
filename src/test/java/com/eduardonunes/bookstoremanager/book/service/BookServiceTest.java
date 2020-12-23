package com.eduardonunes.bookstoremanager.book.service;

import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.eduardonunes.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.eduardonunes.bookstoremanager.books.mapper.BookMapper;
import com.eduardonunes.bookstoremanager.books.repository.BookRepository;
import com.eduardonunes.bookstoremanager.books.service.BookService;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import com.eduardonunes.bookstoremanager.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private AuthenticatedUser authenticationService;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();

        authenticationService = new AuthenticatedUser("eduardo", "123", "ADMIN");
    }
}
