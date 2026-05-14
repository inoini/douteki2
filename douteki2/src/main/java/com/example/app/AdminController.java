package com.example.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import model.User;

@Controller
public class AdminController {

    private final UserDAO dao = new UserDAO();

    @GetMapping("/adminLogin")
    public String adminLoginPage() {
        return "adminLogin";
    }

    @PostMapping("/adminLogin")
    public String adminLogin(@RequestParam String id,
                             @RequestParam String password,
                             HttpSession session) {

        User user = dao.login(id, password);

        if (user == null) {
            return "redirect:/adminLogin?error=1";
        }

        if (!"admin".equals(user.getRole())) {
            return "redirect:/adminLogin?error=denied";
        }
        session.setAttribute("loginUser", user);

       
        return "redirect:/list";
    }

    @GetMapping("/adminLogout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/list";
    }
}