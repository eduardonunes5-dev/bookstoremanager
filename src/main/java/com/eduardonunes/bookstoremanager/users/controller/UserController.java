package com.eduardonunes.bookstoremanager.users.controller;

import com.eduardonunes.bookstoremanager.users.dto.JwtRequest;
import com.eduardonunes.bookstoremanager.users.dto.JwtResponse;
import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.service.AuthenticationService;
import com.eduardonunes.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs{

    private UserService userService;

    private AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@RequestBody @Valid UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageDTO update(@PathVariable Long id, @RequestBody @Valid UserDTO userToUpdateDTO) {
        return userService.update(id, userToUpdateDTO);
    }

    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponse createAuthenticationToken(@RequestBody @Valid JwtRequest jwtRequest) {
        return authenticationService.createAuthenticationToken(jwtRequest);
    }
}
