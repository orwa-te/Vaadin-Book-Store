package org.vaadin.example.backend.service;

import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.Book;
import org.vaadin.example.backend.repo.BookRepository;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public void save(Book book) {
        repository.save(book);
    }
}