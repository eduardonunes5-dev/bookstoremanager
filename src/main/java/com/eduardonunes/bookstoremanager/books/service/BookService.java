package com.eduardonunes.bookstoremanager.books.service;

import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.entity.Book;
import com.eduardonunes.bookstoremanager.books.exception.BookAlreadyExistsException;
import com.eduardonunes.bookstoremanager.books.exception.BookNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public BookResponse findBookByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId){
        User authUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        return bookRepository.findByIdAndUser(bookId, authUser)
                .map(book -> bookMapper.toDTO(book))
                .orElseThrow(()-> new BookNotFoundException(bookId));

    }

    public List<BookResponse> findBooksByUser(AuthenticatedUser user){
        User authUser = userService.verifyAndGetUserIfExists(user.getUsername());
        return bookRepository.findAllByUser(authUser)
                .stream()
                .map(book -> bookMapper.toDTO(book))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId){
        User authUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Book expectedToDeleteBook = verifyAndGetIfExists(bookId, authUser);
        bookRepository.deleteByIdAndUser(expectedToDeleteBook.getId(), authUser);
    }

    public BookResponse updateByUserAndId(AuthenticatedUser authenticatedUser, BookRequest bookRequest, Long id){
        User authUser = userService.verifyAndGetUserIfExists(authenticatedUser.getUsername());
        Book foundBookToUpdate = this.verifyAndGetIfExists(id, authUser);
        Author foundAuthor = authorService.verifyAndGetIfExists(bookRequest.getAuthorId());
        Publisher foundPublisher = publisherService.verifyAndGetIfExists(bookRequest.getPublisherId());

        Book bookToUpdate = bookMapper.toModel(bookRequest);
        bookToUpdate.setCreatedDate(foundBookToUpdate.getCreatedDate());
        bookToUpdate.setId(foundBookToUpdate.getId());
        bookToUpdate.setUser(authUser);
        bookToUpdate.setAuthor(foundAuthor);
        bookToUpdate.setPublisher(foundPublisher);

        Book updatedBook = bookRepository.save(bookToUpdate);
        return bookMapper.toDTO(updatedBook);
    }

    private Book verifyAndGetIfExists(Long bookId, User authUser) {
        Book foundBookToDelete = bookRepository.findByIdAndUser(bookId, authUser)
                .orElseThrow(()-> new BookNotFoundException(bookId));
        return foundBookToDelete;
    }

    private void verifyIfBookExistsByUser(BookRequest bookRequestDTO, User authUser) {
        bookRepository.findByNameAndIsbnAndUser(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), authUser)
                .ifPresent(duplicateBook -> {
                    throw new BookAlreadyExistsException(bookRequestDTO.getName(), bookRequestDTO.getIsbn(), authUser.getUsername());
                });
    }

}
