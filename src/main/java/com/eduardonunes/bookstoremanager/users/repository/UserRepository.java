package com.eduardonunes.bookstoremanager.users.repository;

import com.eduardonunes.bookstoremanager.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
