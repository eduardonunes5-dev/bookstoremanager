package com.eduardonunes.bookstoremanager.book.builder;

import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.user.builder.UserDTOBuilder;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookRequestDTOBuilder {

    @Builder.Default
    private final Long id = 1L;

    @Builder.Default
    private final String name = "spring";

    @Builder.Default
    private final String isbn = "978-3-16-148410-0";

    @Builder.Default
    private final Integer pages = 200;

    @Builder.Default
    private final Integer chapters = 8;

    @Builder.Default
    private final Long authorId = 2L;

    @Builder.Default
    private final Long publisherId = 3L;

    private final UserDTO userDTO = UserDTOBuilder.builder().build().buildUserDTO();


    public BookRequest buildBookRequestDTO(){
        return new BookRequest(
                id,
                name,
                isbn,
                pages,
                chapters,
                authorId,
                authorId
        );
    }

}



