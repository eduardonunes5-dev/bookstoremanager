package com.eduardonunes.bookstoremanager.user.builder;

import com.eduardonunes.bookstoremanager.users.dto.UserDTO;
import com.eduardonunes.bookstoremanager.users.entity.Gender;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class UserDTOBuilder {

    @Builder.Default
    private final long id = 1L;

    @Builder.Default
    private final String name = "user mock";

    @Builder.Default
    private final int age = 20;

    @Builder.Default
    private final Gender gender = Gender.MALE;

    @Builder.Default
    private final String email = "email@test.com";

    @Builder.Default
    private final String username = "edu";

    @Builder.Default
    private final String password = "123";

    @Builder.Default
    private final LocalDate birthDate = LocalDate.of(2020, 12, 17);

    public UserDTO buildUserDTO(){
        return new UserDTO(
                id,
                name,
                age,
                gender,
                email,
                username,
                password,
                birthDate
        );
    }
}
