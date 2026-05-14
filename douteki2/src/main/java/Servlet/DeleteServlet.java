package Servlet;

import java.io.IOException;

import com.example.app.PostDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // 管理者チェック
        HttpSession session = request.getSession(false);

        String role = (String)session.getAttribute("role");

        if(role == null || !role.equals("admin")){
            response.sendRedirect("adminLogin.jsp");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        PostDAO dao = new PostDAO();
        dao.deletePost(id);

        response.sendRedirect(request.getContextPath() + "/AdminServlet");
    }
}