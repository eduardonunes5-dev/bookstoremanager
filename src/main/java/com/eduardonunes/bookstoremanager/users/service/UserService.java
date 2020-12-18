package com.eduardonunes.bookstoremanager.users.service;

import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.eduardonunes.bookstoremanager.users.exception.UserNotFoundException;
import com.eduardonunes.bookstoremanager.users.mapper.UserMapper;
import com.eduardonunes.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public MessageDTO create(UserDTO userDTO){
        verifyIfExists(userDTO.getUsername(), userDTO.getEmail());

        User toSave = userMapper.toModel(userDTO);
        User savedUser = userRepository.save(toSave);
        return createMEssage(savedUser);
    }

    public void deleteById(Long id){
        verifyIfExistsAndGet(id);
        userRepository.deleteById(id);
    }

    private User verifyIfExistsAndGet(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) throw new UserNotFoundException(id);
        return user.get();
    }

    private void verifyIfExists(String username, String email) {
        userRepository.findByUsernameOrEmail(username, email)
                .ifPresent((user)-> {throw new UserAlreadyExistsException(username, email);});
    }

    private MessageDTO createMEssage(User saved) {
        Long savedUserID = saved.getId();
        MessageDTO msg = MessageDTO.builder()
                .message(String.format("User with id %s created successfully", savedUserID))
                .build();
        return msg;
    }
}
