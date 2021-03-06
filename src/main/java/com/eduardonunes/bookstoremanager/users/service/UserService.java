package com.eduardonunes.bookstoremanager.users.service;

import com.eduardonunes.bookstoremanager.users.dto.MessageDTO;
import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;
import com.eduardonunes.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.eduardonunes.bookstoremanager.users.exception.UserNotFoundException;
import com.eduardonunes.bookstoremanager.users.mapper.UserMapper;
import com.eduardonunes.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.eduardonunes.bookstoremanager.users.utils.UserDTOMessage.creationMessage;
import static com.eduardonunes.bookstoremanager.users.utils.UserDTOMessage.updationMessage;

@Service
public class UserService {

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public MessageDTO create(UserDTO userDTO){
        verifyIfExists(userDTO.getUsername(), userDTO.getEmail());

        User toSave = userMapper.toModel(userDTO);
        toSave.setPassword(passwordEncoder.encode(toSave.getPassword()));
        User savedUser = userRepository.save(toSave);
        return creationMessage(savedUser);
    }

    public void deleteById(Long id){
        verifyIfExistsAndGet(id);
        userRepository.deleteById(id);
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO){
        User foundUser = verifyIfExistsAndGet(id);
        userToUpdateDTO.setId(foundUser.getId());

        User toUpdate = userMapper.toModel(userToUpdateDTO);
        toUpdate.setCreatedDate(foundUser.getCreatedDate());
        toUpdate.setPassword(passwordEncoder.encode(toUpdate.getPassword()));

        User updatedUser = userRepository.save(toUpdate);
        return updationMessage(updatedUser);
    }

    private User verifyIfExistsAndGet(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()) throw new UserNotFoundException(id);
        return user.get();
    }

    private void verifyIfExists(String username, String email) {
        userRepository.findByUsernameOrEmail(username, email)
                .ifPresent((p)-> {throw new UserAlreadyExistsException(username, email);});
    }

    public User verifyAndGetUserIfExists(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException(username));
    }
}
