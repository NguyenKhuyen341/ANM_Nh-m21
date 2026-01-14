package com.library.project.repository;

import com.library.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCaseOrAuthorFullNameContainingIgnoreCase(String title, String authorName);
    List<Book> findByCategory(String category);
}