package com.example.app;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import dao.PostDAO;
import jakarta.servlet.http.HttpSession;
import model.Post;

@Controller
public class PostController {

    private final PostDAO dao = new PostDAO();

    // =========================
    // 一覧（ページング）
    // =========================
    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            Model model,
            HttpSession session) {

        int limit = 24;
        int offset = (page - 1) * limit;

        List<Post> posts = dao.getPostsByPage(limit, offset);

        // ⭐ログイン状態判定
        String role = (String) session.getAttribute("role");
        boolean isAdmin = "admin".equals(role);

        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("isAdmin", isAdmin); // ⭐これ追加

        return "list";
    }
    // =========================
    // 投稿
    // =========================
    @PostMapping("/post")
    public String post(
            @RequestParam String name,
            @RequestParam String message,
            @RequestParam(required = false) String snsUrl,
            @RequestParam(required = false) String snsUrl2,
            @RequestParam(required = false) String discordName,
            HttpSession session,
            Model model
    ) {

        String role = (String) session.getAttribute("role");
        boolean isAdmin = "admin".equals(role);

        try {

            // ⭐一般ユーザー制限
            if (!isAdmin) {

                int count = dao.countPostsToday(name);

                if (count >= 2) {
                    System.out.println("投稿制限");

                    return "redirect:/list?limit=1";
                }
            }

            dao.insertPost(name, message, snsUrl, snsUrl2, discordName, isAdmin);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/list";
    }

    // =========================
    // 詳細
    // =========================
    @GetMapping("/detail")
    public String detail(@RequestParam int id, Model model) {

        model.addAttribute("post", dao.findById(id));

        return "detail";
    }

    // =========================
    // 削除
    // =========================
    @PostMapping("/delete")
    public String delete(@RequestParam("id") int id,
                         HttpSession session) {

        if (!checkAdmin(session)) {
            return "redirect:/list";
        }

        if (id <= 0) {
            return "redirect:/list";
        }

        
        dao.deletePost(id);

        return "redirect:/list";
    }
    @GetMapping("/adminLogin")
    public String loginPage() {
        return "adminLogin";
    }

    @PostMapping("/adminLogin")
    public String login(
            @RequestParam String id,
            @RequestParam String pass,
            HttpSession session
    ) {

        if ("adminsekine".equals(id) && BCrypt.checkpw(pass,
                "$2a$10$R0BWUYpyMhreCQuKJvBfX.de5NRstd4d0svuOYG0ffyPAYHpAsmk2")) {

            session.setAttribute("role", "admin");
            return "redirect:/list";
        }

        return "redirect:/adminLogin?error=1";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate(); // ⭐セッション削除

        return "redirect:/list"; // 一覧へ
    }
    private boolean checkAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "admin".equals(role);
    }
}