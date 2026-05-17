package com.example.app;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    // UserRepository ではなく AdminRepository を使う
    @Autowired
    private AdminRepository adminRepository; 

    @GetMapping("/register")
    public String showRegisterPage(
            @CookieValue(value = "already_reg", defaultValue = "false") String alreadyReg,
            HttpSession session, 
            Model model) {
        
        if (session.getAttribute("user") != null || "true".equals(alreadyReg)) {
            model.addAttribute("registered", true);
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("id") String userId,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @CookieValue(value = "already_reg", defaultValue = "false") String alreadyReg,
            HttpServletResponse response,
            HttpSession session) {

        if (session.getAttribute("user") != null || "true".equals(alreadyReg)) {
            return "redirect:/register?already";
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // ★ ここも Admin に修正
        Admin newAdmin = new Admin(); 
        newAdmin.setId(userId);
        newAdmin.setName(name);
        newAdmin.setPassword(hashedPassword);
        newAdmin.setRole("ADMIN");

        try {
            // ★ ここも adminRepository.save に修正
            adminRepository.save(newAdmin);
            
            Cookie cookie = new Cookie("already_reg", "true");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

        } catch (Exception e) {
            return "redirect:/register?error";
        }

        return "redirect:/register?done";
    }
}
