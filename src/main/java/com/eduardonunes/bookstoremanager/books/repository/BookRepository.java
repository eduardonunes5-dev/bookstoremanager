package com.eduardonunes.bookstoremanager.books.repository;

import com.eduardonunes.bookstoremanager.books.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Long, Book> {
}
