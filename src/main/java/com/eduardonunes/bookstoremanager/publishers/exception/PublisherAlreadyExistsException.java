package com.eduardonunes.bookstoremanager.publishers.exception;

import javax.persistence.EntityExistsException;

public class PublisherAlreadyExistsException extends EntityExistsException {

    public PublisherAlreadyExistsException(String code, String name) {
        super(String.format("Publisher with code %s and/or name %s already exists", code, name));
    }
}
