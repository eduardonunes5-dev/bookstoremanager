package com.eduardonunes.bookstoremanager.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/books")
public class BookController {


    @ApiOperation(value = "An Hello World example")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfull method return")
    })
    @GetMapping
    public String hello(){
        return "Hello, book store manager... I'm running an example with pull request!";
    }

    @GetMapping(value = "1")
    public String test(){
        return "another test";
    }
}
