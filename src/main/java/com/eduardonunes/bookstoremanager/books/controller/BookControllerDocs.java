package com.eduardonunes.bookstoremanager.books.controller;

import com.eduardonunes.bookstoremanager.books.dto.BookRequest;
import com.eduardonunes.bookstoremanager.books.dto.BookResponse;
import com.eduardonunes.bookstoremanager.users.dto.AuthenticatedUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Api("Books management")
public interface BookControllerDocs {

    @ApiOperation(value = "Book creation operation")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book created successfully"),
            @ApiResponse(code = 400, message = "Missing required fields, wrong field range value or book already exists")
    })
    BookResponse create(AuthenticatedUser authenticatedUser, BookRequest bookRequest);

    @ApiOperation(value = "Finds a book by its id and user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book found successfully"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    BookResponse findBookByIdAndUser(AuthenticatedUser authenticatedUser, Long bookId);

    @ApiOperation(value = "Finds all books by user operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Books by user found successfully")
    })
    List<BookResponse> findBooksByUser(AuthenticatedUser user);

    @ApiOperation(value = "Book deletion by id and user operation")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Book deleted successfully"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    void deleteByIdAndUser(AuthenticatedUser authenticatedUser,Long bookId);
}
