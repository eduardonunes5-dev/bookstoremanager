package com.eduardonunes.bookstoremanager.users.utils;

import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;

public class UserDTOMessage {

    public static MessageDTO creationMessage(User saved){
        return returnMessage(saved, "created");
    }

    public static MessageDTO updationMessage(User updated){
        return returnMessage(updated, "updated");
    }

    private static MessageDTO returnMessage(User updated, String action) {
        Long savedUserID = updated.getId();
        MessageDTO msg = MessageDTO.builder()
                .message(String.format("User with id %d %s successfully", savedUserID,action))
                .build();
        return msg;
    }
}
