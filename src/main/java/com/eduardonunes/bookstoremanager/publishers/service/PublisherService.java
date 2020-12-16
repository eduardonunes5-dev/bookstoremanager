package com.eduardonunes.bookstoremanager.publishers.service;

import com.eduardonunes.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.eduardonunes.bookstoremanager.publishers.dto.PublisherDTO;
import com.eduardonunes.bookstoremanager.publishers.entity.Publisher;
import com.eduardonunes.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.eduardonunes.bookstoremanager.publishers.mapper.PublisherMapper;
import com.eduardonunes.bookstoremanager.publishers.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final static PublisherMapper mapper = PublisherMapper.INSTANCE;

    private PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }


    public PublisherDTO create(PublisherDTO publisherDTO){
        verifyIfExists(publisherDTO.getCode(), publisherDTO.getName());
        Publisher toSave = mapper.toModel(publisherDTO);
        Publisher saved = this.publisherRepository.save(toSave);
        return mapper.toDTO(saved);

    }

    public PublisherDTO findById(Long id){
        return verifyAndGetPublisher(id);

    }

    public List<PublisherDTO> findAll(){
        return publisherRepository.findAll()
                .stream()
                .map( pub -> mapper.toDTO(pub))
                .collect(Collectors.toList());

    }

    private PublisherDTO verifyAndGetPublisher(Long id) {
        PublisherDTO publisher = publisherRepository.findById(id)
                .map( p -> mapper.toDTO(p))
                .orElseThrow(()-> new PublisherNotFoundException(id));
        return publisher;
    }

    private void verifyIfExists(String code, String name){
        publisherRepository.findByCodeOrName(code, name)
                .ifPresent( publisher -> {throw new PublisherAlreadyExistsException(publisher.getCode(),publisher.getName());});
    }
}
