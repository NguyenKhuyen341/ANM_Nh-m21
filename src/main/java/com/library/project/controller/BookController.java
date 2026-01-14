package com.library.project.controller;

import com.library.project.entity.Author;
import com.library.project.entity.Book;
import com.library.project.repository.AuthorRepository;
import com.library.project.repository.BookRepository;
import com.library.project.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookService bookService, BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public List<Book> getAllBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        if (category != null && !category.isEmpty()) {
            return bookRepository.findByCategory(category);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return bookService.searchBooks(keyword);
        }
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // --- HÀM THÊM SÁCH (TỰ TẠO TÁC GIẢ NẾU CHƯA CÓ) ---
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        // 1. Xử lý Tác giả theo Tên
        if (book.getAuthor() != null && book.getAuthor().getFullName() != null) {
            String authorName = book.getAuthor().getFullName();
            Author author = authorRepository.findByFullName(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setFullName(authorName);
                        return authorRepository.save(newAuthor);
                    });
            book.setAuthor(author);
        }

        // 2. Xử lý số lượng
        if (book.getAvailableQuantity() == 0) {
            book.setAvailableQuantity(book.getTotalQuantity());
        }
        return bookRepository.save(book);
    }

    // --- HÀM CẬP NHẬT SÁCH (CŨNG TỰ XỬ LÝ TÁC GIẢ THEO TÊN) ---
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách!"));

        book.setTitle(bookDetails.getTitle());
        book.setTotalQuantity(bookDetails.getTotalQuantity());
        book.setPrice(bookDetails.getPrice());
        book.setCategory(bookDetails.getCategory());

        // Cập nhật Mô tả & Ảnh (Nếu sau này bạn làm tính năng ảnh)
        book.setDescription(bookDetails.getDescription());
        book.setImage(bookDetails.getImage());

        // Xử lý cập nhật Tác giả (Logic giống hệt addBook)
        if (bookDetails.getAuthor() != null && bookDetails.getAuthor().getFullName() != null) {
            String authorName = bookDetails.getAuthor().getFullName();
            Author author = authorRepository.findByFullName(authorName)
                    .orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setFullName(authorName);
                        return authorRepository.save(newAuthor);
                    });
            book.setAuthor(author);
        }

        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}