package com.library.project.controller;

import com.library.project.repository.BookRepository;
import com.library.project.repository.LoanRepository;
import com.library.project.repository.ReaderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanRepository loanRepository;

    public DashboardController(BookRepository bookRepository, ReaderRepository readerRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.loanRepository = loanRepository;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // 1. Thống kê Sách
        long totalBooks = bookRepository.count();
        // Tính tổng số lượng sách 'available_quantity' của tất cả các đầu sách
        // (Ở đây ta tạm tính số đầu sách còn hàng, nếu muốn chính xác từng cuốn phải viết Query SUM)
        long availableBooks = bookRepository.findAll().stream().mapToInt(b -> b.getAvailableQuantity()).sum();

        // 2. Thống kê Độc giả
        long totalReaders = readerRepository.count();

        // 3. Thống kê Mượn trả
        long borrowingCount = loanRepository.findAll().stream().filter(l -> "BORROWING".equals(l.getStatus())).count();
        long returnedCount = loanRepository.findAll().stream().filter(l -> "RETURNED".equals(l.getStatus())).count();

        // Đóng gói dữ liệu gửi về
        stats.put("totalBooks", totalBooks);       // Tổng đầu sách
        stats.put("totalStock", availableBooks);   // Tổng sách trong kho
        stats.put("totalReaders", totalReaders);   // Tổng thành viên
        stats.put("borrowing", borrowingCount);    // Đang mượn
        stats.put("returned", returnedCount);      // Đã trả

        // Tính số sách đang được cầm bởi sinh viên (Tổng sách - Sách trong kho)
        // Lưu ý: Logic này chỉ đúng tương đối nếu totalStock được tính đúng
        long booksInUse = 0; // Tạm thời để 0, Frontend sẽ tự tính hoặc dùng borrowingCount

        return stats;
    }
}