package com.eduardonunes.bookstoremanager.users.controller;

import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api("User management")
public interface UserControllerDocs {


    @ApiOperation("Saves a user into database")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully"),
            @ApiResponse(code = 400, message = "Missing required fields/field validation errors")
    })
    MessageDTO create(UserDTO userDTO);
}
