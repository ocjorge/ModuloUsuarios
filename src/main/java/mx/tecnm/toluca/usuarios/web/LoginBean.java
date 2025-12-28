package mx.tecnm.toluca.usuarios.web;

// Alcance del bean a nivel request (una petición HTTP)
import jakarta.enterprise.context.RequestScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para manejo de mensajes JSF
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// Importación para manejo de excepciones de redirección
import java.io.IOException;

// Importaciones del modelo y servicio
import mx.tecnm.toluca.usuarios.model.Usuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

/**
 * Bean de respaldo (Managed Bean) para el proceso de login.
 * Maneja la captura de credenciales y la autenticación del usuario.
 */
@Named
@RequestScoped
public class LoginBean {

    /**
     * Nombre de usuario ingresado en el formulario.
     */
    private String username;

    /**
     * Contraseña ingresada en el formulario.
     */
    private String password;

    /**
     * Servicio de usuarios.
     * Se utiliza para autenticar al usuario.
     */
    @Inject
    private UsuarioService usuarioService;

    /**
     * Administrador de sesión.
     * Se utiliza para establecer la sesión del usuario autenticado.
     */
    @Inject
    private SessionManager sessionManager;

    /**
     * Obtiene el nombre de usuario.
     * @return username
     */
    public String getUsername() { 
        return username; 
    }

    /**
     * Establece el nombre de usuario.
     * @param username nombre de usuario
     */
    public void setUsername(String username) { 
        this.username = username; 
    }

    /**
     * Obtiene la contraseña.
     * @return password
     */
    public String getPassword() { 
        return password; 
    }

    /**
     * Establece la contraseña.
     * @param password contraseña
     */
    public void setPassword(String password) { 
        this.password = password; 
    }

    /**
     * Método que ejecuta el proceso de autenticación.
     * Llama al servicio de usuarios y, si es exitoso,
     * establece la sesión y redirige al dashboard.
     *
     * @return navegación JSF (redirección o null)
     */
    public String login() {

        // Intenta autenticar al usuario con las credenciales proporcionadas
        Usuario usuario = usuarioService.autenticar(username, password);

        // Si la autenticación es exitosa
        if (usuario != null) {

            // Establece la sesión del usuario autenticado
            sessionManager.establecerSesion(usuario);

            // Redirige al dashboard principal
            return "dashboard.xhtml?faces-redirect=true";

        } else {

            // Agrega un mensaje de error a la vista
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Credenciales inválidas",
                    ""
                )
            );

            // Permanece en la misma página
            return null;
        }
    }
}
