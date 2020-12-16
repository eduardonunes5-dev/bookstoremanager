package com.eduardonunes.bookstoremanager.publishers.controller;


import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;


@Api("Publisher management")
public interface PublisherControllerDocs {

    @ApiOperation(value = "Creates a publisher in the database")
    @ApiResponses(value ={
            @ApiResponse(code = 201, message = "Publisher created successfully"),
            @ApiResponse(code = 400, message = "Publisher missing fields, wrong field range or already exists")
    })
    PublisherDTO create(PublisherDTO publisherDTO);

    @ApiOperation(value = "Retrieves a publisher in the database")
    @ApiResponses(value ={
            @ApiResponse(code = 200, message = "Publisher found successfully"),
            @ApiResponse(code = 404, message = "Publisher not found")
    })
    PublisherDTO findById(Long id);

    @ApiOperation(value = "Retrieves all publishers in the database")
    @ApiResponse(code = 200, message = "Returns all publishers")
    List<PublisherDTO> findAll();
}
