package com.example.app;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue; // 追加
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.Cookie; // 追加
import jakarta.servlet.http.HttpServletResponse; // 追加
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 登録画面の表示
     */
    @GetMapping("/register")
    public String showRegisterPage(
            @CookieValue(value = "already_reg", defaultValue = "false") String alreadyReg,
            HttpSession session, 
            Model model) {
        
        // 1. セッション（ログイン中）または クッキー（過去に登録済み）をチェック
        if (session.getAttribute("user") != null || "true".equals(alreadyReg)) {
            model.addAttribute("registered", true); // これでHTML側で判定する
        }
        return "register";
    }

    /**
     * ユーザー登録処理
     */
    @PostMapping("/register")
    public String registerUser(
            @RequestParam("id") String userId,
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @CookieValue(value = "already_reg", defaultValue = "false") String alreadyReg,
            HttpServletResponse response, // クッキー書き込み用に追加
            HttpSession session) {

        // 1. クッキーまたはセッションによる二重登録チェック
        if (session.getAttribute("user") != null || "true".equals(alreadyReg)) {
            return "redirect:/register?already";
        }

        // 2. パスワード暗号化
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User();
        newUser.setId(userId);
        newUser.setName(name);
        newUser.setPassword(hashedPassword);
        newUser.setRole("USER");

        try {
            userRepository.save(newUser);
            
            // ★登録成功時、ブラウザに「登録済み」クッキーを保存する
            Cookie cookie = new Cookie("already_reg", "true");
            cookie.setMaxAge(60 * 60 * 24 * 365 * 10); // 有効期限：10年（秒単位）
            cookie.setPath("/"); // サイト全体で有効
            cookie.setHttpOnly(true); // セキュリティ向上
            response.addCookie(cookie);

        } catch (Exception e) {
            return "redirect:/register?error";
        }

        return "redirect:/register?done";
    }
}
