package org.vaadin.example.controller;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.vaadin.example.backend.entity.Book;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.reflections.Reflections.log;

@RestController
public class BookController {

    @GetMapping(value ="/fetchBooks/{lastBookId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Book> getBooks1(@PathVariable int lastBookId) {
        log.info("endpoint_called;");
        return Flux.range(1, 5)
                .map(i-> (long)i + lastBookId )
                .map(i -> new Book(i, "title_" + i, " author_" + i, 2000, "description of the book_" + i))
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(book -> log.info("book is emitted...{}", book));
    }

    public Flux<Book> getBooks(int lastBookId) {
        log.info("endpoint_called;");
        return Flux.range(1, 5)
                .map(i-> (long)i + lastBookId )
                .map(i -> {
                    var book = new Book();
                    book.setTitle("title_" + i);
                    book.setAuthor("author_" + i);
                    book.setDescription("\"description of the book_\" + i");
                    book.setPublicationYear(2000);
                    return book;
                })
                .delayElements(Duration.ofSeconds(2))
                .doOnNext(book -> log.info("book is emitted...{}", book));
    }
}
