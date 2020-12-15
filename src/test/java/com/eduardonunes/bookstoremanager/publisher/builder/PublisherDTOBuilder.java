package com.eduardonunes.bookstoremanager.publisher.builder;

import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.Builder;

import java.time.LocalDate;


@Builder
public class PublisherDTOBuilder {

    @Builder.Default
    private final long id = 1L;

    @Builder.Default
    private final  String name = "Nunes editora";

    @Builder.Default
    private final String code = "NUN0519";

    @Builder.Default
    private final LocalDate foundationDate = LocalDate.of(2020, 05, 19);

    public PublisherDTO buildPublisherDTO() {
        return new PublisherDTO(
                id,
                name,
                code,
                foundationDate
        );
    }
}
