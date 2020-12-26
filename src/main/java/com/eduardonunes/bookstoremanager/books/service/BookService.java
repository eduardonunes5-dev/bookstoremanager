package com.eduardonunes.bookstoremanager.books.service;

import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.entity.Book;
import com.eduardonunes.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.eduardonunes.bookstoremanager.books.mapper.BookMapper;
import com.eduardonunes.bookstoremanager.books.repository.BookRepository;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BookService {

    private final BookMapper bookMapper = BookMapper.INSTANCE;

    private BookRepository bookRepository;

    private AuthorService authorService;

    private UserService userService;

    private PublisherService publisherService;

    public BookResponse create(AuthenticatedUser authenticatedUser, BookRequest bookRequestDTO){
        User authUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        verifyIfBookExistsByUser(bookRequestDTO, authUser);

        Author bookAuthor = authorService.verifyAndGetIfExists(bookRequestDTO.getAuthorId());
        Publisher bookPublisher = publisherService.verifyAndGetIfExists(bookRequestDTO.getPublisherId());

        Book bookToSave = bookMapper.toModel(bookRequestDTO);
        bookToSave.setUser(authUser);
        bookToSave.setAuthor(bookAuthor);
        bookToSave.setPublisher(bookPublisher);

        Book savedBook = bookRepository.save(bookToSave);

        return bookMapper.toDTO(savedBook);
    }

    private void verifyIfBookExistsByUser(BookRequest bookRequestDTO, User authUser) {
        bookRepository.findByNameAndIsbnAndUser(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), authUser)
                .ifPresent(duplicateBook -> {
                    throw new BookAlreadyExistsException(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), authUser.getUsername());
                });
    }

}
