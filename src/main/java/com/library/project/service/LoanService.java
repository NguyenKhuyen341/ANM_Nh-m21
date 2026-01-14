package com.library.project.service;

import com.library.project.entity.Book;
import com.library.project.entity.Loan;
import com.library.project.entity.Reader;
import com.library.project.repository.BookRepository;
import com.library.project.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List; // Thêm thư viện này để dùng List ngắn gọn

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    // 1. Nghiệp vụ Mượn sách
    @Transactional
    public Loan createLoan(Reader reader, Book book) {
        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Sách đã hết trong kho!");
        }

        // Trừ số lượng sách còn lại
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        // Tạo phiếu mượn
        Loan loan = new Loan();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14)); // Mặc định cho mượn 14 ngày
        loan.setStatus("BORROWING");

        return loanRepository.save(loan);
    }

    // 2. Nghiệp vụ Trả sách
    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu mượn!"));

        if ("RETURNED".equals(loan.getStatus())) {
            throw new RuntimeException("Sách này đã được trả trước đó rồi!");
        }

        // Cập nhật ngày trả và trạng thái
        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");

        // Cộng lại số lượng sách vào kho
        Book book = loan.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    // 3. Lấy tất cả phiếu mượn (Cho Admin)
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    // 4. Lấy phiếu mượn theo ID độc giả (Cho Sinh viên) -> HÀM NÀY PHẢI NẰM TRONG CLASS
    public List<Loan> getLoansByReaderId(Long readerId) {
        return loanRepository.findByReaderId(readerId);
    }

}