package com.eduardonunes.bookstoremanager.books.service;

import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.books.mapper.BookMapper;
import com.eduardonunes.bookstoremanager.books.repository.BookRepository;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
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

}
