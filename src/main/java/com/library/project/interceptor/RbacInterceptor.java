package com.library.project.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RbacInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String method = request.getMethod();

        // 1. VÙNG AN TOÀN (Ai cũng được vào)
        // Login, đăng ký, file ảnh, css, js
        if (path.startsWith("/api/auth") ||
                path.equals("/") || path.equals("/index.html") ||
                path.contains("/login.html") || path.contains("/register.html") ||
                path.contains("/css/") || path.contains("/js/") || path.contains("/images/") || path.contains("/uploads/") ||
                (path.startsWith("/api/books") && method.equals("GET"))) { // Ai cũng được xem sách
            return true;
        }

        // 2. KIỂM TRA ĐĂNG NHẬP (Authentication)
        HttpSession session = request.getSession();
        String role = (String) session.getAttribute("ROLE"); // Lấy vai trò từ Session

        if (role == null) {
            System.out.println("⛔ CHẶN: Chưa đăng nhập mà đòi vào " + path);
            response.sendRedirect("/login.html"); // Đá về trang login
            return false;
        }

        System.out.println("🛡️ KIỂM TRA QUYỀN: " + role + " đang truy cập " + path);

        // --- QUYỀN ADMIN (QUYỀN LỰC NHẤT) ---
        if ("ADMIN".equals(role)) {
            return true; // Admin được đi khắp nơi
        }

        // --- QUYỀN LIBRARIAN (THỦ THƯ) ---
        if ("LIBRARIAN".equals(role)) {
            // 1. Cho phép vào admin.html để quản lý sách
            // (Không chặn admin.html nữa)

            // 2. Chặn hành động XÓA SÁCH (Sửa câu thông báo ở đây)
            if (path.startsWith("/api/books") && method.equals("DELETE")) {
                // 👇 ĐÂY LÀ CHỖ BẠN CẦN SỬA CHỮ 👇
                response.sendError(403, "Bạn không thể xóa sách được! Chỉ Admin mới có quyền này.");
                return false;
            }

            // 3. Chặn xóa User
            if (path.startsWith("/api/users") && method.equals("DELETE")) {
                response.sendError(403, "Thủ thư không được xóa người dùng!");
                return false;
            }

            return true;
        }

        // --- QUYỀN READER (SINH VIÊN) ---
        if ("READER".equals(role)) {
            // Chỉ được vào trang sinh viên và mượn sách
            if (path.contains("student-dashboard.html") || path.contains("catalog.html") ||
                    path.contains("book-details.html") || path.contains("my-loans.html") ||
                    path.contains("/api/loans")) {
                return true;
            }

            // Cấm vào trang quản lý (dashboard.html, admin.html)
            if (path.contains("dashboard.html") || path.contains("admin.html")) {
                response.sendError(403, "Sinh viên không được vào trang quản lý!");
                return false;
            }

            // Cấm Sửa/Xóa sách
            if (path.startsWith("/api/books") && (method.equals("POST") || method.equals("PUT") || method.equals("DELETE"))) {
                response.sendError(403, "Sinh viên không được sửa dữ liệu sách!");
                return false;
            }
        }

        // Mặc định chặn tất cả trường hợp lạ
        response.sendError(403, "Truy cập bị từ chối!");
        return false;
    }
}