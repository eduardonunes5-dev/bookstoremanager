package com.eduardonunes.bookstoremanager.book.controller;

import com.eduardonunes.bookstoremanager.book.builder.BookRequestDTOBuilder;
import com.eduardonunes.bookstoremanager.book.builder.BookResponseDTOBuilder;
import com.eduardonunes.bookstoremanager.books.controller.BookController;
import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.books.service.BookService;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.eduardonunes.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private static final String BOOK_API_URL_TEST = "/api/v1/books";

    private BookRequestDTOBuilder bookRequestDTOBuilder;

    private BookResponseDTOBuilder bookResponseDTOBuilder;


    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bookRequestDTOBuilder = BookRequestDTOBuilder.builder().build();
        bookResponseDTOBuilder = BookResponseDTOBuilder.builder().build();

        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .addFilters()
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeReturned() throws Exception{

        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        BookResponse expectedCreatedBook = bookResponseDTOBuilder.buildBookResponse();

        when(bookService.create(any(AuthenticatedUser.class), eq(bookRequestDTO))).thenReturn(expectedCreatedBook);

        mockMvc.perform(post(BOOK_API_URL_TEST)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(bookRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(bookRequestDTO.getName())))
                .andExpect(jsonPath("$.id", is(bookRequestDTO.getId().intValue())))
                .andExpect(jsonPath("$.isbn", is(bookRequestDTO.getIsbn())))
                .andExpect(jsonPath("$.chapters", is(bookRequestDTO.getChapters())));

    }

    @Test
    void whenPostWithMissingReqFieldsThenBadRequestShouldBeReturned() throws Exception{

        BookRequest bookRequestDTO = bookRequestDTOBuilder.buildBookRequestDTO();
        bookRequestDTO.setAuthorId(null);

        mockMvc.perform(post(BOOK_API_URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bookRequestDTO)))
                .andExpect(status().isBadRequest());

    }
}