package mx.tecnm.toluca.usuarios.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import mx.tecnm.toluca.usuarios.modelo.UsuarioInfo;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Inject
    private UsuarioService service;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        // 1. Recibimos el 'username' del formulario HTML
        String user = request.getParameter("username"); 
        String pass = request.getParameter("password");
        
        // 2. El servicio busca en la BD por username
        UsuarioInfo usuario = service.login(user, pass);
        
        if (usuario != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);
            
            // Respondemos con JSON de éxito y el nombre real para el saludo
            out.print("{\"status\":\"ok\", \"nombre\":\"" + usuario.getNombre() + "\"}");
        } else {
            out.print("{\"status\":\"error\", \"message\":\"Usuario o contraseña incorrectos\"}");
        }
    }
}