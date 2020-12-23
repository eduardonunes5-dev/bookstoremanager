package com.eduardonunes.bookstoremanager.books.dto;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long id;

    private String name;

    private String isbn;

    private Integer pages;

    private Integer chapters;

    private AuthorDTO author;

    private PublisherDTO publisher;


}
