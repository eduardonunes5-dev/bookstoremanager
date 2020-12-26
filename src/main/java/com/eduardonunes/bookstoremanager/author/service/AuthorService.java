package com.eduardonunes.bookstoremanager.author.service;

import com.eduardonunes.bookstoremanager.author.dto.AuthorDTO;
import com.eduardonunes.bookstoremanager.author.entity.Author;
import com.eduardonunes.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.eduardonunes.bookstoremanager.author.exception.AuthorNotFoundException;
import com.eduardonunes.bookstoremanager.author.mapper.AuthorMapper;
import com.eduardonunes.bookstoremanager.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        Author createdAuthor = this.authorRepository.save(authorToCreate);
        return authorMapper.toDto(createdAuthor);
    }

    public AuthorDTO findById(Long id){
        Author author = verifyAndGetIfExists(id);
        return authorMapper.toDto(author);
    }

    public List<AuthorDTO> findAll(){
        return authorRepository.findAll()
                .stream()
                .map(author -> authorMapper.toDto(author))
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        verifyAndGetIfExists(id);
        authorRepository.deleteById(id);
    }

    public Author verifyAndGetIfExists(Long id) {
        Author author = this.authorRepository.findById(id)
                .orElseThrow(()->new AuthorNotFoundException(id));
        return author;
    }


    private void verifyIfExists(AuthorDTO authorDTO) {
        authorRepository.findByName(authorDTO.getName())
                .ifPresent(author ->{throw new AuthorAlreadyExistsException(author.getName());});
    }

}
