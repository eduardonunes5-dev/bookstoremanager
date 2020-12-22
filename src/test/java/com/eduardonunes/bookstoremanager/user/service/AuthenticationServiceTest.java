package com.eduardonunes.bookstoremanager.user.service;

import com.eduardonunes.bookstoremanager.user.builder.JwtRequestBuilder;
import com.eduardonunes.bookstoremanager.user.builder.UserDTOBuilder;
import com.eduardonunes.bookstoremanager.users.dto.JwtRequest;
import com.eduardonunes.bookstoremanager.users.dto.JwtResponse;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.mapper.UserMapper;
import com.eduardonunes.bookstoremanager.users.repository.UserRepository;
import com.eduardonunes.bookstoremanager.users.service.AuthenticationService;
import com.eduardonunes.bookstoremanager.users.service.JwtTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    private UserDTOBuilder userDTOBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenManager jwtTokenManager;

    private JwtRequestBuilder jwtRequestBuilder;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
        jwtRequestBuilder = JwtRequestBuilder.builder().build();
    }

    @Test
    void whenUsernameAndPasswordIsInformedThenATokenShouldBeGenerated() {
        JwtRequest jwtRequest = jwtRequestBuilder.buildJwtRequest();
        UserDTO expectedUserDTO = userDTOBuilder.buildUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);
        String expectedToken = "fakeToken";

        when(userRepository.findByUsername(jwtRequest.getUsername())).thenReturn(Optional.of(expectedUser));
        when(jwtTokenManager.generateToken(any((UserDetails.class)))).thenReturn(expectedToken);

        JwtResponse jwtResponse = authenticationService.createAuthenticationToken(jwtRequest);

        assertThat(jwtResponse.getJwtToken(), is(expectedToken));
    }

    @Test
    void whenValidUsernameThenUserShouldBeReturned() {
        UserDTO expectedUserDTO = userDTOBuilder.buildUserDTO();
        User expectedUser = userMapper.toModel(expectedUserDTO);
        SimpleGrantedAuthority expectedUserRole = new SimpleGrantedAuthority("ROLE_" + expectedUserDTO.getRole().getDescription().toUpperCase());

        String expectedUsername = expectedUserDTO.getUsername();
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.of(expectedUser));

        UserDetails userDetails = authenticationService.loadUserByUsername(expectedUsername);

        assertThat(userDetails.getUsername(), is(expectedUser.getUsername()));
        assertThat(userDetails.getPassword(), is(expectedUser.getPassword()));
        assertTrue(userDetails.getAuthorities().contains(expectedUserRole));

    }

    @Test
    void whenInvalidUsernameThenAnExceptionShouldBeThrown() {
        UserDTO expectedUserDTO = userDTOBuilder.buildUserDTO();

        String expectedUsername = expectedUserDTO.getUsername();
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, ()-> authenticationService.loadUserByUsername(expectedUsername));

    }

}
