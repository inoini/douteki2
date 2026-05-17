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

    @Autowired
    private AdminRepository adminRepository;

    /**
     * 登録画面を表示する
     */
    @GetMapping("/register")
    public String showRegisterPage(
            @CookieValue(value = "already_reg", defaultValue = "false") String alreadyReg,
            HttpSession session, 
            Model model) {
        
        // 判定1: すでにログインしているか？
        boolean isLoggedIn = (session.getAttribute("user") != null);
        // 判定2: ブラウザに「登録済みクッキー」があるか？
        boolean hasCookie = "true".equals(alreadyReg);
        
        // ★最重要★ trueでもfalseでも必ず「registered」という名前で画面に送る
        // これで HTML側の Exception evaluating SpringEL expression エラーが消えます
        model.addAttribute("registered", isLoggedIn || hasCookie);
        
        return "register";
    }

    /**
     * ユーザー登録を実行する
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("id") String id, 
            @RequestParam("name") String name, 
            @RequestParam("password") String password, 
            HttpServletResponse response) {

        try {
            // パスワードを暗号化
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            
            admin newAdmin = new admin();
            newAdmin.setId(id);
            newAdmin.setName(name);
            newAdmin.setPassword(hashedPassword);
            newAdmin.setRole("ADMIN");

            // データベースへ保存
            adminRepository.save(newAdmin);
            
            // ★登録成功時、ブラウザに「登録済み」というクッキーを刻む（10年間有効）
            Cookie cookie = new Cookie("already_reg", "true");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "redirect:/register?done";
            
        } catch (Exception e) {
            // ID重複などのエラー時
            return "redirect:/register?error";
        }
    }
}
