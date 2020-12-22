package com.eduardonunes.bookstoremanager.user.builder;

import com.eduardonunes.bookstoremanager.users.dto.JwtRequest;
import lombok.Builder;

@Builder
public class JwtRequestBuilder {

    @Builder.Default
    private String username = "eduardo";

    @Builder.Default
    private String password = "123";

    public JwtRequest buildJwtRequest(){
        return new JwtRequest(
                username,
                password
        );
    }
}
