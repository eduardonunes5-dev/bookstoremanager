package com.eduardonunes.bookstoremanager.author.builder;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public class AuthorDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "Eduardo";

    @Builder.Default
    private final int age = 21;


    public AuthorDTO buildAuthorDTO(){
        return new AuthorDTO(id, name, age);
    }
}
