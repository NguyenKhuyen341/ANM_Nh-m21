package com.library.project.controller;

import com.library.project.entity.Book;
import com.library.project.entity.Loan;
import com.library.project.entity.Reader;
import com.library.project.repository.BookRepository;
import com.library.project.repository.ReaderRepository;
import com.library.project.service.LoanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    public LoanController(LoanService loanService, ReaderRepository readerRepository, BookRepository bookRepository) {
        this.loanService = loanService;
        this.readerRepository = readerRepository;
        this.bookRepository = bookRepository;
    }

    // 1. API cho ADMIN: Lấy tất cả phiếu mượn
    // GET: http://localhost:8080/api/loans
    @GetMapping
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    // 2. API cho SINH VIÊN: Lấy phiếu mượn của riêng mình
    // GET: http://localhost:8080/api/loans/my-loans?readerId=1
    @GetMapping("/my-loans")
    public List<Loan> getMyLoans(@RequestParam Long readerId) {
        return loanService.getLoansByReaderId(readerId);
    }

    // 3. API Mượn sách
    // POST: http://localhost:8080/api/loans/borrow?readerId=1&bookId=1
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long readerId, @RequestParam Long bookId) {
        Reader reader = readerRepository.findById(readerId).orElse(null);
        Book book = bookRepository.findById(bookId).orElse(null);

        if (reader == null || book == null) {
            return "Lỗi: Không tìm thấy độc giả hoặc sách!";
        }

        try {
            loanService.createLoan(reader, book);
            return "Mượn sách thành công!";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    // 4. API Trả sách
    // POST: http://localhost:8080/api/loans/return/1
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        try {
            loanService.returnBook(id);
            return "Trả sách thành công!";
        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}