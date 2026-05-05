package Servlet;

import java.io.IOException;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {

    private static final String ADMIN_ID = "adminsekine";

    private static final String ADMIN_HASH =
            "$2a$10$R0BWUYpyMhreCQuKJvBfX.de5NRstd4d0svuOYG0ffyPAYHpAsmk2";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        request.getRequestDispatcher("adminLogin.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
      

        if (id != null && pass != null &&
            ADMIN_ID.equals(id) &&
            BCrypt.checkpw(pass, ADMIN_HASH)) {

            HttpSession session = request.getSession(true);
            
            // ★ここを統一（重要）
            session.setAttribute("loginUser", id);
            session.setAttribute("role", "admin");

            response.sendRedirect(
                request.getContextPath() + "/AdminServlet"
            );

        } else {
        	response.sendRedirect(request.getContextPath() + "/adminLogin?error=1");
        	}
        }
    }
