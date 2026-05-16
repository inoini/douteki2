package Servlet;
import java.io.IOException;

import dao.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;

@WebServlet("/detail")
public class DetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String idStr = request.getParameter("id");

            if(idStr == null){
                response.getWriter().println("idなし");
                return;
            }

            int id = Integer.parseInt(idStr);

            PostDAO dao = new PostDAO();
            Post post = dao.findById(id);

            request.setAttribute("post", post);
            request.getRequestDispatcher("/detail.jsp").forward(request, response);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}