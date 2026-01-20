package com.library.project.repository;

import com.library.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Hàm này giúp tìm User bất kể họ là Admin, Librarian hay Reader
    User findByUsername(String username);
}