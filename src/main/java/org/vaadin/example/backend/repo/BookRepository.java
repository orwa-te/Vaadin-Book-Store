package org.vaadin.example.backend.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vaadin.example.backend.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "CAST(b.publicationYear AS string) LIKE CONCAT('%', :searchTerm, '%') OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Book> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

//    @Query("SELECT b FROM Book b WHERE " +
//            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "CAST(b.publicationYear AS string) LIKE CONCAT('%', :searchTerm, '%') OR " +
//            "LOWER(b.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    List<Book> findBySearchTerm(@Param("searchTerm") String searchTerm);

//    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    // Update repository method
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "CAST(b.publicationYear AS string) LIKE CONCAT('%', :keyword, '%')")
    List<Book> search(@Param("keyword") String keyword);

}