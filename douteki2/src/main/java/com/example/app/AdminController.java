package com.example.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    // 投稿データを取得するために必要
    @Autowired
    private PostDAO dao; 

    @GetMapping("/admin")
    public String adminPage(Model model, HttpSession session) {
        // ロール（権限）チェック
        String role = (String) session.getAttribute("role");

        if (!"admin".equals(role)) {
            return "redirect:/admin/login";
        }

        // 投稿一覧を取得して画面に送る
        model.addAttribute("posts", dao.findAll());

        return "admin"; // templates/admin.html を表示
    }
}
