package com.eduardonunes.bookstoremanager.users.controller;

import com.eduardonunes.bookstoremanager.users.dto.JwtRequest;
import com.eduardonunes.bookstoremanager.users.dto.JwtResponse;
import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Api("User management")
public interface UserControllerDocs {


    @ApiOperation("Saves a user into database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Missing required fields/field validation errors")
    })
    MessageDTO create(UserDTO userDTO);

    @ApiOperation(value ="User deletion by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "User deleted successfully"),
            @ApiResponse(code = 404, message = "User not found")
    })
    void deleteById(Long id);

    @ApiOperation(value = "Updates user by id operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated successfully"),
            @ApiResponse(code = 400, message = "Missing required fields or malformed data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    MessageDTO update(Long id, UserDTO userDTO);

    @ApiOperation("User authentication")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User logged in successfully"),
            @ApiResponse(code = 404, message = "User not found")
    })
    JwtResponse createAuthenticationToken(JwtRequest jwtRequest);
}
