package Servlet;

import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);

        Boolean admin = null;

        if (session != null) {
            admin = (Boolean) session.getAttribute("admin");
        }

        // ❌ 管理者じゃなければ完全ブロック
        if (admin == null || !admin) {

            response.sendRedirect(
                request.getContextPath() + "/adminLogin"
            );
            return;
        }

        // ⭕ OKなら通す
        chain.doFilter(req, res);
    }
}