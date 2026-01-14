package com.library.project.controller;

import com.library.project.entity.Reader;
import com.library.project.repository.ReaderRepository; // <--- Đã import thêm cái này
import com.library.project.service.ReaderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;
    private final ReaderRepository readerRepository; // <--- 1. Khai báo thêm biến này

    // 2. Cập nhật Constructor để nhận cả 2 cái
    public ReaderController(ReaderService readerService, ReaderRepository readerRepository) {
        this.readerService = readerService;
        this.readerRepository = readerRepository;
    }

    // Lấy danh sách
    @GetMapping
    public List<Reader> getAllReaders() {
        return readerService.getAllReaders();
    }

    // Lấy chi tiết 1 người
    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable Long id) {
        return readerService.getReaderById(id);
    }

    // Thêm mới
    @PostMapping
    public Reader addReader(@RequestBody Reader reader) {
        // Mặc định mật khẩu là 123456 nếu admin không nhập
        if (reader.getPassword() == null || reader.getPassword().isEmpty()) {
            reader.setPassword("123456");
        }
        return readerService.saveReader(reader);
    }

    // Cập nhật (Hết lỗi vì đã có readerRepository)
    @PutMapping("/{id}")
    public Reader updateReader(@PathVariable Long id, @RequestBody Reader readerDetails) {
        return readerRepository.findById(id).map(reader -> {
            // Cập nhật Tên
            if(readerDetails.getFullName() != null) {
                reader.setFullName(readerDetails.getFullName());
            }
            // Cập nhật SĐT (Hết lỗi vì đã sửa Reader.java ở bước 1)
            if(readerDetails.getPhoneNumber() != null) {
                reader.setPhoneNumber(readerDetails.getPhoneNumber());
            }

            return readerRepository.save(reader);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy độc giả có ID: " + id));
    }

    // Xóa
    @DeleteMapping("/{id}")
    public void deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
    }
}