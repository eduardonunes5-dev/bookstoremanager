package com.eduardonunes.bookstoremanager.author.controller;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Api("Authors management")
public interface AuthorControllerDocs {

    @ApiOperation(value = "Author creation operation")
    @ApiResponses(value ={
            @ApiResponse(code = 202, message = "Author created successfully"),
            @ApiResponse(code = 400, message = "Missing requid fields/wrong field range value or author already registered")
    })
    AuthorDTO create(@RequestBody @Valid AuthorDTO authorDTO);


    @ApiOperation(value = "Author searching operation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Author found successfully"),
            @ApiResponse(code = 404, message = "Author not found")
    })
    AuthorDTO findById(@PathVariable Long id);

    @ApiOperation(value = "List all registered authors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return a list of authors in the database"),
    })
    List<AuthorDTO> findAll();


    @ApiOperation(value = "Author deletion by their id")
    @ApiResponses(value ={
            @ApiResponse(code = 204, message = "Author deleted successfully!"),
            @ApiResponse(code = 404, message = "Author not found!")
    })
    void deleteById(@PathVariable("id") Long id);
}
