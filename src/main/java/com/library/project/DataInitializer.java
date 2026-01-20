package com.library.project;

import com.library.project.entity.*;
import com.library.project.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LibrarianRepository librarianRepository;
    private final ReaderRepository readerRepository;

    public DataInitializer(UserRepository userRepository, LibrarianRepository librarianRepository, ReaderRepository readerRepository) {
        this.userRepository = userRepository;
        this.librarianRepository = librarianRepository;
        this.readerRepository = readerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin") == null) {
            System.out.println("---- ĐANG KHỞI TẠO ADMIN VÀ DỮ LIỆU MẪU ----");

            // 1. Tạo ADMIN
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123");
            admin.setFullName("Quản Trị Viên");
            admin.setRole("ADMIN");
            admin.setStatus("ACTIVE");
            userRepository.save(admin);

            // 2. Tạo THỦ THƯ (Nhân viên)
            Librarian lib = new Librarian();
            lib.setUsername("thuthu");
            lib.setPassword("123");
            lib.setFullName("Cô Thủ Thư");
            lib.setRole("LIBRARIAN");
            lib.setStaffCode("NV001");
            lib.setStatus("ACTIVE");
            librarianRepository.save(lib); // Lưu vào bảng Librarian (tự động lưu vào User)

            // 3. Tạo ĐỘC GIẢ (Sinh viên)
            Reader reader = new Reader();
            reader.setUsername("sinhvien");
            reader.setPassword("123");
            reader.setFullName("Nguyễn Văn Sinh Viên");
            reader.setRole("READER");
            reader.setReaderCode("SV2024");
            reader.setStatus("ACTIVE");
            readerRepository.save(reader);

            System.out.println("---- ĐÃ TẠO XONG: admin/123 ----");
        } else {
            System.out.println("---- Dữ liệu đã có sẵn, không tạo lại nữa ----");
        }
    }
}