package mx.tecnm.toluca.usuarios;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Ejemplo básico de validación (puedes cambiarlo a consulta SQL)
        if ("admin".equals(username) && "1234".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", username);
            response.sendRedirect("editarUsuario.jsp"); // página principal tras login
        } else {
            response.sendRedirect("login.jsp?error=true");
        }
    }
}
