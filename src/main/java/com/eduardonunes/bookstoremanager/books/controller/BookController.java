package com.eduardonunes.bookstoremanager.books.controller;


import com.eduardonunes.bookstoremanager.books.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController implements BookControllerDocs{

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }
}
