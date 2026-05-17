package com.example.app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import model.User;

@Controller
public class PostController {

    // ★DAOは1つだけ
    private final PostDAO dao = new PostDAO();

    // =========================
    // トップ
    // =========================
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("posts", dao.findAll());
        return "list";
    }

    // =========================
    // 管理者画面
    // =========================
    
    // =========================
    // 一覧
    // =========================
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(required = false) String keyword,
                       @CookieValue(value = "registered", required = false) String registered,
                       Model model,
                       HttpSession session) {

        List<Post> posts;

        if (keyword != null && !keyword.isBlank()) {
            posts = dao.searchPosts(keyword, 24, (page - 1) * 24);
        } else {
            posts = dao.getPostsByPage(24, (page - 1) * 24);
        }

        // ★ログイン状態
        String userId = (String) session.getAttribute("loginUserId");
        boolean isLogin = (userId != null);

        // ★管理者判定
        String role = (String) session.getAttribute("role");
        boolean isAdmin = "admin".equals(role);

        model.addAttribute("posts", posts);
        model.addAttribute("isLogin", isLogin);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);

        // ★Cookie追加
        model.addAttribute("registered", registered);

        return "list";
    }
 
    // =========================
    // 投稿
    // =========================
    @PostMapping("/post")
    public String post(@RequestParam String message,
                       @RequestParam(required = false) String snsUrl,
                       @RequestParam(required = false) String snsUrl2,
                       @RequestParam(required = false) String discordName,
                       HttpSession session) {

        // ログインチェック
        User user = (User) session.getAttribute("loginUser");

        if (user == null) {
            return "redirect:/login";
        }

        // ログインユーザー情報取得
        String name = user.getName();
        String role = user.getRole();

        boolean isAdmin = "admin".equals(role);

        // SNS整形
        if (snsUrl != null) {
            snsUrl = snsUrl.replace("@", "")
                    .replace("https://twitter.com/", "")
                    .replace("http://twitter.com/", "")
                    .replace("twitter.com/", "");
        }

        if (snsUrl2 != null) {
            snsUrl2 = snsUrl2.replace("@", "")
                    .replace("https://instagram.com/", "")
                    .replace("http://instagram.com/", "")
                    .replace("instagram.com/", "");
        }

        // 投稿制限（一般ユーザーのみ）
        if (!isAdmin) {

            Integer count = (Integer) session.getAttribute("postCount");

            if (count == null) {
                count = 0;
            }

            if (count >= 2) {
                return "redirect:/list?limit=1";
            }

            session.setAttribute("postCount", count + 1);
        }

        // 投稿保存
        dao.insertPost(name, message, snsUrl, snsUrl2, discordName, isAdmin);

        // 古い投稿削除
        dao.deleteOldestPostIfOver500();

        return "redirect:/list";
    }
    // =========================
    // 詳細
    // =========================
    @GetMapping("/detail")
    public String detail(@RequestParam int id,
                          HttpSession session,
                          Model model) {

        // ★ここ重要（統一）
    	if (session.getAttribute("loginUser") == null) {
            return "redirect:/list?error=login";
        }

        Post post = dao.findById(id);
        model.addAttribute("post", post);

        return "detail";
    }

    // =========================
    // 削除（管理者のみ）
    // =========================
    @PostMapping("/delete")
    public String deletePost(@RequestParam int id,
                             HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        // 未ログイン
        if (loginUser == null) {
            return "redirect:/login";
        }

        // 管理者以外拒否
        if (!"admin".equals(loginUser.getRole())) {
            return "redirect:/list";
        }

        dao.delete(id);

        return "redirect:/list";
    }

    // =========================
    // 管理者チェック（未使用なら削除OK）
    // =========================
    private boolean checkAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "admin".equals(role);
    }
}
