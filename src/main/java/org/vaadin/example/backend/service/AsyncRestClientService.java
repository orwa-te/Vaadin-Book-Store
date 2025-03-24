package org.vaadin.example.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.vaadin.example.backend.entity.Book;

import java.io.Serializable;
import java.util.List;

import static org.reflections.Reflections.log;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.vaadin.example.controller.BookController;
import reactor.core.publisher.Flux;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncRestClientService {

    @Autowired
    BookController bookController;//mimic remote controller

    public interface AsyncRestCallback<T> {
        void operationFinished(T book);
    }

    public void getBooksAsync(AsyncRestCallback<Book> callback) {
        log.info("Fetching books through REST...");

        bookController.getBooks(1)
                .subscribe(
                        book -> {
                            log.info("book_received={}", book);
                            callback.operationFinished(book);
                        },
                        error -> log.error("Error fetching books", error) // Error handling
                );
    }
}