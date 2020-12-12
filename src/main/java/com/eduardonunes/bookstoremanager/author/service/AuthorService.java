package com.eduardonunes.bookstoremanager.author.service;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.eduardonunes.bookstoremanager.author.exception.AuthorNotFoundException;
import com.eduardonunes.bookstoremanager.author.mapper.AuthorMapper;
import com.eduardonunes.bookstoremanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final static AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO create(AuthorDTO authorDTO){
        verifyIfExists(authorDTO);
        Author authorToCreate = authorMapper.toModel(authorDTO);
        System.out.println(authorToCreate);
        Author createdAuthor = this.authorRepository.save(authorToCreate);
        return authorMapper.toDto(createdAuthor);
    }

    public AuthorDTO findById(Long id){
        Author author = this.authorRepository.findById(id)
                .orElseThrow(()->new AuthorNotFoundException(id));

        return authorMapper.toDto(author);
    }

    private void verifyIfExists(AuthorDTO authorDTO) {
        authorRepository.findByName(authorDTO.getName())
                .ifPresent(author ->{throw new AuthorAlreadyExistsException(author.getName());});
    }
}
