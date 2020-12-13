package com.eduardonunes.bookstoremanager.author.controller;

import com.eduardonunes.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import com.eduardonunes.bookstoremanager.utils.JsonConversionUtils;
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

import java.util.Collections;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthorControllerTest {

    private static final String AUTHOR_API_URL_PATH = "/api/v1/authors";

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(authorController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenStatusCreatedShouldBeReturned() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorService.create(expectedCreatedAuthorDTO))
                .thenReturn(expectedCreatedAuthorDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedCreatedAuthorDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedCreatedAuthorDTO.getName())))
                .andExpect(jsonPath("$.age", is(expectedCreatedAuthorDTO.getAge())));

    }

    @Test
    void whenPOSTIsCalledWithoutRequiredFieldsThenBadRequestStatusCreatedShouldBeReturned() throws Exception {
        AuthorDTO expectedCreatedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        expectedCreatedAuthorDTO.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConversionUtils.asJsonString(expectedCreatedAuthorDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void whenGETWithValidIdIsCalledThenAnAuthorShouldBeReturned() throws Exception {
        AuthorDTO expectedFoundAuthorDto = authorDTOBuilder.buildAuthorDTO();
        Long expectedFoundAuthorId = expectedFoundAuthorDto.getId();

        when(authorService.findById(expectedFoundAuthorId))
            .thenReturn(expectedFoundAuthorDto);

        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH + "/" + expectedFoundAuthorId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(expectedFoundAuthorId.intValue())))
                .andExpect(jsonPath("$.name", is(expectedFoundAuthorDto.getName())))
                .andExpect(jsonPath("$.age", is(expectedFoundAuthorDto.getAge())));

    }

    @Test
    void whenGETListIsCalledThenAnListOfAuthorsShouldBeReturned() throws Exception {
        AuthorDTO expectedFoundAuthorDto = authorDTOBuilder.buildAuthorDTO();

        when(authorService.findAll())
                .thenReturn(Collections.singletonList(expectedFoundAuthorDto));

        mockMvc.perform(MockMvcRequestBuilders.get(AUTHOR_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id", is(expectedFoundAuthorDto.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(expectedFoundAuthorDto.getName())))
                .andExpect(jsonPath("$[0].age", is(expectedFoundAuthorDto.getAge())));

    }

    @Test
    void whenDELETEWithValidIdThenNoContentIsReturned() throws Exception{
        AuthorDTO deletedAuthor = authorDTOBuilder.buildAuthorDTO();
        Long authorId = deletedAuthor.getId();

        doNothing().when(authorService).delete(authorId);

        mockMvc.perform(MockMvcRequestBuilders.delete(AUTHOR_API_URL_PATH + '/' + authorId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


}
