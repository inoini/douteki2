package Servlet;

import java.io.IOException;

import com.example.app.PostDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/post")
public class PostServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		// ★ログイン必須
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("loginUser") == null) {

			response.sendRedirect(request.getContextPath() + "/adminLogin");

			return;
		}

		// ★セッション取得
		String userId = (String) session.getAttribute("loginUser");

		String role = (String) session.getAttribute("role");

		boolean isAdmin = "admin".equals(role);
		System.out.println("role=" + role);
		System.out.println("isAdmin=" + isAdmin);

		PostDAO dao = new PostDAO();
		// ★入力取得
		String message = request.getParameter("message");

		String snsUrl = request.getParameter("snsUrl");

		String sns2 = request.getParameter("snsUrl2");

		String discordName = request.getParameter("discordName");

		// ★文字数制限
		if (message != null && message.length() > 200) {

			message = message.substring(0, 200);
		}

		// ★一般ユーザーだけ1日2回制限
		if (!isAdmin) {

			int count = dao.countPostsToday(userId);

			if (count >= 2) {

				request.setAttribute("error", "本日の投稿上限（2回）に達しました");

				request.getRequestDispatcher("/index.jsp").forward(request, response);

				return;
			}
		}

		// ★表示名
		String displayName = userId;

		if (isAdmin) {
			displayName = "管理者いの";
		}

		// ★保存
		dao.insertPost(displayName, message, snsUrl, sns2, discordName, isAdmin);

		response.sendRedirect(request.getContextPath() + "/list?page=1");
	}
}