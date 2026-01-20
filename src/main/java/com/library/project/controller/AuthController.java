package com.library.project.controller;

import com.library.project.entity.User;
import com.library.project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Map<String, Object> response = new HashMap<>();

        // 1. Tìm user trong Database (Bảng chung Users)
        User user = userRepository.findByUsername(username);

        // 2. Kiểm tra mật khẩu (Demo: So sánh chuỗi thường)
        if (user != null && user.getPassword().equals(password)) {
            // --- QUAN TRỌNG NHẤT CỦA ĐỀ TÀI: LƯU SESSION ---
            HttpSession session = request.getSession();
            session.setAttribute("USER_ID", user.getId());
            session.setAttribute("ROLE", user.getRole()); // Lưu vai trò: ADMIN, LIBRARIAN, READER
            session.setAttribute("FULLNAME", user.getFullName());
            // -----------------------------------------------

            response.put("status", "success");
            response.put("role", user.getRole());
            response.put("fullName", user.getFullName());

            // Nếu là Reader thì trả thêm readerId để frontend dùng
            if (user instanceof com.library.project.entity.Reader) {
                response.put("readerId", user.getId());
            }

            response.put("message", "Đăng nhập thành công với quyền: " + user.getRole());
        } else {
            response.put("status", "error");
            response.put("message", "Sai tên đăng nhập hoặc mật khẩu!");
        }

        return response;
    }

    // API Đăng xuất
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "Đã đăng xuất!";
    }
}