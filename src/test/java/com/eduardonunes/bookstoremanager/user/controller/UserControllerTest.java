package com.eduardonunes.bookstoremanager.user.controller;

import com.eduardonunes.bookstoremanager.exception.BookStoreExceptionHandler;
import com.eduardonunes.bookstoremanager.user.builder.UserDTOBuilder;
import com.eduardonunes.bookstoremanager.users.controller.UserController;
import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.exception.UserNotFoundException;
import com.eduardonunes.bookstoremanager.users.service.UserService;
import org.hamcrest.core.Is;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String USERS_TEST_URI = "/api/v1/users";
    private UserDTOBuilder userDTOBuilder;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(BookStoreExceptionHandler.class)
                .setViewResolvers((s,v) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenPOSTValidUserThenCreatedShouldBeReturn() throws Exception {
        UserDTO expectedCreatedUserDTO = userDTOBuilder.buildUserDTO();

        Long savedUserID = expectedCreatedUserDTO.getId();
        MessageDTO expectedMessage = MessageDTO.builder()
                .message(String.format("User with id %s created successfully", savedUserID))
                .build();

        when(userService.create(expectedCreatedUserDTO)).thenReturn(expectedMessage);

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_TEST_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedCreatedUserDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenPOSTInvalidFieldThenBadRequestShouldBeReturned() throws Exception {
        UserDTO expectedInvalidUser = userDTOBuilder.buildUserDTO();
        expectedInvalidUser.setUsername(null);

        mockMvc.perform(MockMvcRequestBuilders.post(USERS_TEST_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedInvalidUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDELETEWithValidIdThenNoContentShouldBeReturned() throws Exception {
        UserDTO userDTO = userDTOBuilder.buildUserDTO();
        Long userDTOIdToRemove = userDTO.getId();

        doNothing().when(userService).deleteById(userDTOIdToRemove);

        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_TEST_URI + '/' + userDTOIdToRemove)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenDELETEWithInvalidIdThenAnExceptionShouldBeThrown() throws Exception {
        UserDTO userDTO = userDTOBuilder.buildUserDTO();
        Long userDTOIdToRemove = userDTO.getId();

        doThrow(UserNotFoundException.class).when(userService).deleteById(userDTOIdToRemove);

        mockMvc.perform(MockMvcRequestBuilders.delete(USERS_TEST_URI + '/' + userDTOIdToRemove)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPUTWithValidUserThenOkShouldBeReturned() throws Exception{
        UserDTO userToUpdate = userDTOBuilder.buildUserDTO();
        Long userToUpdateId = userToUpdate.getId();
        userToUpdate.setUsername("dudu");

        String msg = String.format("User with id %d updated successfully", userToUpdateId);
        MessageDTO  messageDTO = MessageDTO.builder().message(msg).build();
        when(userService.update(userToUpdateId, userToUpdate)).thenReturn(messageDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(USERS_TEST_URI + '/' + userToUpdateId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(equalTo(msg))));
    }
}
