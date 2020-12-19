package com.eduardonunes.bookstoremanager.users.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ADMIN("Admin"),
    USER("User");

    private final String description;
}
