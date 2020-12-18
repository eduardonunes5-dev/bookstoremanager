package com.eduardonunes.bookstoremanager.users.exception;

import javax.persistence.EntityExistsException;

public class UserAlreadyExistsException extends EntityExistsException {
    public UserAlreadyExistsException(String username, String email) {
        super(String.format("User with username %s or email %s already exists", username, email));
    }
}
