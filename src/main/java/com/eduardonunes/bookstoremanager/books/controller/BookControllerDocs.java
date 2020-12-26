package com.eduardonunes.bookstoremanager.books.controller;

import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Api("Books management")
public interface BookControllerDocs {

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book created successfully"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or book already exists")
    })
    BookResponse create(AuthenticatedUser authenticatedUser, BookRequest bookRequest);
}
