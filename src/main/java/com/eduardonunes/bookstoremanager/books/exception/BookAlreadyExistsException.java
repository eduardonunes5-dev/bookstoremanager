package com.eduardonunes.bookstoremanager.books.exception;

import javax.persistence.EntityExistsException;


public class BookAlreadyExistsException extends EntityExistsException {
    public BookAlreadyExistsException(String name, String isbn, String authUsername) {
        super(String.format("Book with name: %s and isbn: %s for user %s already exists", name, isbn, authUsername));
    }
}
