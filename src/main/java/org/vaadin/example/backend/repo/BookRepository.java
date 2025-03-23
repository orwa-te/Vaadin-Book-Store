package org.vaadin.example.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.backend.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}