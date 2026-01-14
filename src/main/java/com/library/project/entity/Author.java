package com.library.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    // Các trường này của bạn rất tốt, hãy giữ lại
    private String penName;
    private LocalDate dob;
    private String nationality;

    // --- QUAN TRỌNG: @JsonIgnore ở đây giúp chặn lỗi Server ---
    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Book> books;

    // Constructor mặc định
    public Author() {}

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPenName() { return penName; }
    public void setPenName(String penName) { this.penName = penName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
}