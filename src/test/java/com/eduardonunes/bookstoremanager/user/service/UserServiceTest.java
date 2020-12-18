package com.eduardonunes.bookstoremanager.user.service;

import com.eduardonunes.bookstoremanager.user.builder.UserDTOBuilder;
import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.eduardonunes.bookstoremanager.users.exception.UserNotFoundException;
import com.eduardonunes.bookstoremanager.users.mapper.UserMapper;
import com.eduardonunes.bookstoremanager.users.repository.UserRepository;
import com.eduardonunes.bookstoremanager.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenNewValidUserDTOThenSaveIt() {
        UserDTO userDTOToBeSaved = userDTOBuilder.buildUserDTO();
        User userToBeSaved = userMapper.toModel(userDTOToBeSaved);

        String toBeSavedUsername = userToBeSaved.getUsername();
        String toBeSavedEmail = userToBeSaved.getEmail();
        when(userRepository.findByUsernameOrEmail(toBeSavedUsername, toBeSavedEmail)).thenReturn(Optional.empty());

        when(userRepository.save(userToBeSaved)).thenReturn(userToBeSaved);

        MessageDTO returnMessage = userService.create(userDTOToBeSaved);

        String expectedMessageText = String.format("User with id %s created successfully", userDTOToBeSaved.getId());

        assertThat(expectedMessageText, is(returnMessage.getMessage()));
    }

    @Test
    void whenExistingUserThenAnExceptionShouldBeThrown() {
        UserDTO userDTOToBeSaved = userDTOBuilder.buildUserDTO();
        User expectedDuplicatedUser = userMapper.toModel(userDTOToBeSaved);

        String toBeSavedUsername = expectedDuplicatedUser.getUsername();
        String toBeSavedEmail = expectedDuplicatedUser.getEmail();
        when(userRepository.findByUsernameOrEmail(toBeSavedUsername, toBeSavedEmail))
                .thenReturn(Optional.of(expectedDuplicatedUser));

        assertThrows(UserAlreadyExistsException.class, ()-> userService.create(userDTOToBeSaved));
    }

    @Test
    void whenExistingUserThenDeleteIt(){
        UserDTO userDTOToRemove = userDTOBuilder.buildUserDTO();
        User userToRemove = userMapper.toModel(userDTOToRemove);
        Long userToRemoveId = userDTOToRemove.getId();

        when(userRepository.findById(userToRemoveId)).thenReturn(Optional.of(userToRemove));
        doNothing().when(userRepository).deleteById(userToRemoveId);
        userService.deleteById(userToRemoveId);

        verify(userRepository, times(1)).deleteById(userToRemoveId);
        verify(userRepository, times(1)).findById(userToRemoveId);

    }

    @Test
    void whenNotExistingUserThenThrowAnException(){
        Long userToRemoveId = 2L;

        when(userRepository.findById(userToRemoveId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(userToRemoveId));
    }
}
