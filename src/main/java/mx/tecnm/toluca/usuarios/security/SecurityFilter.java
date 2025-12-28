package mx.tecnm.toluca.usuarios.security;

// Importaciones para inyección de dependencias
import jakarta.inject.Inject;

// Importaciones para filtros de Servlet
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Importación para manejo de excepciones de E/S
import java.io.IOException;

// Importación del manejador de sesión del sistema
import mx.tecnm.toluca.usuarios.web.SessionManager;

/**
 * Filtro de seguridad que intercepta todas las peticiones a páginas XHTML.
 * Se encarga de verificar si el usuario está autenticado
 * antes de permitir el acceso a las vistas protegidas.
 */
@WebFilter("*.xhtml")
public class SecurityFilter implements Filter {

    /**
     * Inyección del SessionManager.
     * Se utiliza para verificar el estado de autenticación del usuario.
     */
    @Inject
    private SessionManager sessionManager;

    /**
     * Método de inicialización del filtro.
     * No se realiza ninguna configuración adicional.
     */
    @Override
    public void init(FilterConfig filterConfig) {}

    /**
     * Método principal del filtro.
     * Se ejecuta en cada solicitud a un recurso XHTML.
     *
     * @param request  solicitud recibida
     * @param response respuesta enviada
     * @param chain    cadena de filtros
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Conversión de la solicitud y respuesta a tipos HTTP
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Obtiene la URL solicitada
        String url = req.getRequestURI();

        // Verifica si la solicitud corresponde a la página de login
        // o a recursos internos de JSF (CSS, JS, imágenes, etc.)
        boolean isLoginPage = url.contains("login.xhtml") 
                              || url.contains("/javax.faces.resource/");

        // Verifica si el usuario está autenticado en la sesión
        boolean loggedIn = sessionManager != null && sessionManager.isAutenticado();

        // Si el usuario no está autenticado y no intenta acceder al login,
        // se redirige a la página de inicio de sesión
        if (!loggedIn && !isLoginPage) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        // Continúa con la cadena de filtros si la validación es correcta
        chain.doFilter(request, response);
    }

    /**
     * Método de destrucción del filtro.
     * No se realiza ninguna acción al finalizar.
     */
    @Override
    public void destroy() {}
}
