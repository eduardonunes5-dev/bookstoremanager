package com.eduardonunes.bookstoremanager.book.builder;

import com.eduardonunes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import com.eduardonunes.bookstoremanager.user.builder.UserDTOBuilder;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import lombok.Builder;

@Builder
public class BookResponseDTOBuilder {


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
    private final AuthorDTO author = AuthorDTOBuilder.builder().build().buildAuthorDTO();

    @Builder.Default
    private final PublisherDTO publisher = PublisherDTOBuilder.builder().build().buildPublisherDTO();

    @Builder.Default
    private final UserDTO userDTO = UserDTOBuilder.builder().build().buildUserDTO();


    public BookResponse buildBookResponse(){
        return new BookResponse(id,
                name,
                isbn,
                pages,
                chapters,
                author,
                publisher
        );
    }
}
