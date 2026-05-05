package Servlet;

import java.io.IOException;
import java.util.List;

import dao.PostDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;


@WebServlet("/list")
public class ListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字化け対策（必要なら）
        request.setCharacterEncoding("UTF-8");

        // ページ番号取得
        int currentPage = 1;

        String pageParam = request.getParameter("page");

        if (pageParam != null && pageParam.matches("\\d+")) {
            currentPage = Integer.parseInt(pageParam);
        }

        // ページング設定
        int limit = 24;
        int offset = (currentPage - 1) * limit;

        // DAO呼び出し
        PostDAO dao = new PostDAO();
        List<Post> posts = dao.getPostsByPage(limit, offset);

        // JSPに渡す
        request.setAttribute("posts", posts);
        request.setAttribute("currentPage", currentPage);

        // 画面遷移
        RequestDispatcher rd =
                request.getRequestDispatcher("/index.jsp");

        rd.forward(request, response);
    }
}