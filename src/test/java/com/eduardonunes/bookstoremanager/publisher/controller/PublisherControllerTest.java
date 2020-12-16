package com.eduardonunes.bookstoremanager.publisher.controller;

import com.eduardonunes.bookstoremanager.publisher.builder.PublisherDTOBuilder;
import com.eduardonunes.bookstoremanager.publishers.controller.PublisherController;
import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.JsonPath;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.eduardonunes.bookstoremanager.utils.JsonConversionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublisherControllerTest {

    private final static String PUBLISHERS_API_URL_TEST = "/api/v1/publishers";

    private MockMvc mockMvc;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherController publisherController;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(publisherController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s,locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTIsCalledThenCreatedStatusShouldBeReturned() throws Exception {
        PublisherDTO toBeSavedDTO = publisherDTOBuilder.buildPublisherDTO();

        Mockito.when(publisherService.create(toBeSavedDTO)).thenReturn(toBeSavedDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(PUBLISHERS_API_URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toBeSavedDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(toBeSavedDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(toBeSavedDTO.getName())))
                .andExpect(jsonPath("$.code", is(toBeSavedDTO.getCode())));


    }

    @Test
    void whenPOSTIsCalledThenBadRequestShouldBeReturned() throws Exception {
        PublisherDTO toBeSavedDTO = publisherDTOBuilder.buildPublisherDTO();
        toBeSavedDTO.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(PUBLISHERS_API_URL_TEST)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toBeSavedDTO)))
                .andExpect(status().isBadRequest());


    }

    @Test
    void whenGETValidIdIsGivenThenAPublisherShouldBeReturned() throws Exception {
        PublisherDTO expectedDTO = publisherDTOBuilder.buildPublisherDTO();

        Mockito.when(publisherService.findById(expectedDTO.getId())).thenReturn(expectedDTO);

        mockMvc.perform(MockMvcRequestBuilders.get(PUBLISHERS_API_URL_TEST + "/" + expectedDTO.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedDTO.getId().intValue())))
                .andExpect(jsonPath("$.name", is(expectedDTO.getName())))
                .andExpect(jsonPath("$.code", is(expectedDTO.getCode())));
    }
}
