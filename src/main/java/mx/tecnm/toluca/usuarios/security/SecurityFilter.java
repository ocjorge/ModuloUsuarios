package mx.tecnm.toluca.usuarios.security;

import jakarta.inject.Inject;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import mx.tecnm.toluca.usuarios.web.SessionManager;

@WebFilter("*.xhtml")
public class SecurityFilter implements Filter {

    @Inject
    private SessionManager sessionManager;

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String url = req.getRequestURI();
        boolean isLoginPage = url.contains("login.xhtml") || url.contains("/javax.faces.resource/");

        boolean loggedIn = sessionManager != null && sessionManager.isAutenticado();

        if (!loggedIn && !isLoginPage) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
