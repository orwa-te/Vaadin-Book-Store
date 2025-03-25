package org.vaadin.example.backend.service;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vaadin.example.backend.entity.Book;
import org.vaadin.example.backend.repo.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }


    public Page<Book> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return repository.findAll(pageable);
        }
        return repository.findBySearchTerm(keyword, pageable);
    }

//    public List<Book> search(String filter) {
//        if(filter==null || filter.isEmpty()){
//            return repository.findAll();
//        }
//        return repository.findBySearchTerm(filter);
//    }
    public Page<Book> search(String filter, int page, int size, List<QuerySortOrder> sortOrders) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(getSpringSort(sortOrders)));
        return repository.findBySearchTerm(filter, pageable);
    }

    private List<Sort.Order> getSpringSort(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream()
                .map(order -> new Sort.Order(
                        order.getDirection() == SortDirection.ASCENDING ?
                                Sort.Direction.ASC : Sort.Direction.DESC,
                        order.getSorted()))
                .collect(Collectors.toList());
    }

    public Book findItemWithLargestId() {
        return repository.findAll().stream()
                .max(Comparator.comparingLong(Book::getId)) // Compare long IDs
                .orElse(null); // Handle empty stream (return null if no items)
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