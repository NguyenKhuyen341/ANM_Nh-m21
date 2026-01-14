package com.library.project.service;

import com.library.project.entity.Reader;
import com.library.project.repository.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    // 1. Lấy danh sách tất cả độc giả
    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    // 2. Lưu (Thêm mới hoặc Cập nhật)
    public Reader saveReader(Reader reader) {
        return readerRepository.save(reader);
    }

    // 3. Tìm độc giả theo ID (Dùng khi muốn sửa thông tin)
    public Reader getReaderById(Long id) {
        Optional<Reader> reader = readerRepository.findById(id);
        return reader.orElse(null); // Nếu tìm thấy thì trả về, không thì trả về null
    }

    // 4. Xóa độc giả
    public void deleteReader(Long id) {
        readerRepository.deleteById(id);
    }
}