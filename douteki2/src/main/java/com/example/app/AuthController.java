package com.example.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.springframework.ui.Model;
@Controller
public class AuthController {

	private final UserDAO dao = new UserDAO();

	// =========================
	// 登録画面
	// =========================
	@GetMapping("/register")
	public String registerPage(
	        HttpServletRequest request,
	        Model model) {

	    Cookie[] cookies = request.getCookies();

	    boolean registered = false;

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {

	            if (cookie.getName().equals("registered")) {
	                registered = true;
	            }
	        }
	    }

	    model.addAttribute("registered", registered);

	    return "register";
	}
	// =========================
	// 登録処理
	// =========================
	@PostMapping("/register")
	public String register(
	        @RequestParam String id,
	        @RequestParam String name,
	        @RequestParam String password,
	        HttpSession session,
	        HttpServletResponse response) {

	    id = id.trim();
	    name = name.trim();
	    password = password.trim();

	    dao.insertUser(id, name, password, "user");

	    User user = new User();
	    user.setId(id);
	    user.setName(name);
	    user.setRole("user");

	    session.setAttribute("loginUser", user);

	    // Cookie保存（再登録防止）
	    Cookie cookie = new Cookie("registered", "true");

	    // 1年間保持
	    cookie.setMaxAge(60 * 60 * 24 * 365);

	    // 全ページで有効
	    cookie.setPath("/");

	    response.addCookie(cookie);

	    return "redirect:/register?done=true";
	}

	// =========================
	// ログイン画面

	// =========================
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	// =========================
	// ログイン処理
	// =========================
	@PostMapping("/login")
	public String login(@RequestParam String id, @RequestParam String password, HttpSession session) {

		User user = dao.login(id, password);

		if (user == null) {
			return "redirect:/login?error=1";
		}

		   session.setAttribute("loginUser", user);

		return "redirect:/list";
	}

	// =========================
	// ログアウト
	// =========================
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/list";
	}
	@PostMapping("/logout")
	public String logout1(HttpSession session) {

	    session.invalidate();

	    return "redirect:/list";
	}
}