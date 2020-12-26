package com.eduardonunes.bookstoremanager.books.controller;


import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.service.BookService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/books")
public class BookController implements BookControllerDocs{

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestBody @Valid BookRequest bookRequest){
        return bookService.create(authenticatedUser, bookRequest);
    }
}
