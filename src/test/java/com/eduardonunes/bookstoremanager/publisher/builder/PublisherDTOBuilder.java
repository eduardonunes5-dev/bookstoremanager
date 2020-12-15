package com.eduardonunes.bookstoremanager.publisher.builder;

import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.Builder;

import java.time.LocalDate;


@Builder
public class PublisherDTOBuilder {

    @Builder.Default
    private final long id = 1L;

    private final  String name = "Nunes editora";

    private final String code = "NUN0519";

    private LocalDate foundationDate = LocalDate.of(2020, 05, 19);

    public PublisherDTO buildPublisherDTO(LocalDate foundationDate) {
        return new PublisherDTO(
                id,
                name,
                code,
                foundationDate
        );
    }
}
