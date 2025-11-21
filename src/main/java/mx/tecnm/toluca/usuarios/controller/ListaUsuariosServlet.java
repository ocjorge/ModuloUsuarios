package mx.tecnm.toluca.usuarios.controller;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import mx.tecnm.toluca.usuarios.modelo.UsuarioInfo;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@WebServlet(name = "ListaUsuariosServlet", urlPatterns = {"/usuarios"})
public class ListaUsuariosServlet extends HttpServlet {

    @Inject
    private UsuarioService service;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Obtener la lista desde la base de datos
        List<UsuarioInfo> lista = service.listarUsuarios();
        
        // 2. Guardarla en la memoria del request para el JSP
        request.setAttribute("listaUsuarios", lista);
        
        // 3. Enviar al usuario a la vista (JSP)
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}