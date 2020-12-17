package com.eduardonunes.bookstoremanager.users.mapper;

import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toModel(UserDTO userDTO);
    UserDTO toDTO(User user);
}
