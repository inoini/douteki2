package Servlet;

import java.io.IOException;
import java.util.List;

import com.example.app.Post;
import com.example.app.PostDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        
        // ★ここを統一（重要）
        String role = (session != null)
                ? (String) session.getAttribute("role")
                : null;

        if (role == null || !"admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/adminLogin");
            return;
        }

        int page = 1;
        String pageStr = request.getParameter("page");

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        int limit = 24;
        int offset = (page - 1) * limit;

        PostDAO dao = new PostDAO();
        List<Post> posts = dao.getPostsByPage(limit, offset);

        request.setAttribute("posts", posts);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("admin.jsp")
               .forward(request, response);
    }
}