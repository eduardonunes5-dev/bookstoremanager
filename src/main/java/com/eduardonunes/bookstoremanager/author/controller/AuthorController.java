package com.eduardonunes.bookstoremanager.author.controller;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController implements AuthorControllerDocs{

    private AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDTO create(@RequestBody @Valid AuthorDTO authorDTO) {
        return this.authorService.create(authorDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDTO findById(@PathVariable Long id){
        return this.authorService.findById(id);
    }

    @GetMapping
    public List<AuthorDTO> findAll(){
        return this.authorService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable("id") Long authorId){
        this.authorService.delete(authorId);
    }
}
