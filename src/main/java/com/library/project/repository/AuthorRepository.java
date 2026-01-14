package com.library.project.repository;

import com.library.project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// --- QUAN TRỌNG: Phải có dòng này mới hết lỗi Optional ---
import java.util.Optional;
// ---------------------------------------------------------

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByFullName(String fullName);
}