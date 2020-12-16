package com.eduardonunes.bookstoremanager.publishers.controller;


import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api("Publisher management")
public interface PublisherControllerDocs {

    @ApiOperation(value = "Creates a publisher in the database")
    @ApiResponses(value ={
            @ApiResponse(code = 201, message = "Publisher created successfully"),
            @ApiResponse(code = 400, message = "Publisher missing fields, wrong field range or already exists")
    })
    PublisherDTO create(PublisherDTO publisherDTO);
}
