package com.library.project.controller;

import com.library.project.entity.Reader;
import com.library.project.repository.ReaderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ReaderRepository readerRepository;

    public AuthController(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    // API Đăng nhập
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Map<String, Object> response = new HashMap<>();

        // 1. Check Admin (Tạm thời để cứng)
        if ("admin".equals(username) && "123".equals(password)) {
            response.put("status", "success");
            response.put("role", "ADMIN");
            response.put("message", "Chào mừng Sếp!");
            return response;
        }

        // 2. Check Độc giả (Tìm trong Database)
        Reader reader = readerRepository.findByUsername(username);

        if (reader != null && reader.getPassword().equals(password)) {
            response.put("status", "success");
            response.put("role", "READER");
            response.put("readerId", reader.getId());
            response.put("fullName", reader.getFullName());
            response.put("message", "Đăng nhập thành công!");
        } else {
            response.put("status", "error");
            response.put("message", "Sai tên đăng nhập hoặc mật khẩu!");
        }

        return response;
    }
}