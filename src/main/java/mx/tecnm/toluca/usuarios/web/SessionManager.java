package mx.tecnm.toluca.usuarios.web;

// Alcance del bean a nivel de sesión (persiste mientras dure la sesión del usuario)
import jakarta.enterprise.context.SessionScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para manejo del contexto JSF
import jakarta.faces.context.FacesContext;

// Importaciones para E/S, serialización y UUID
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

// Importaciones del modelo y servicio
import mx.tecnm.toluca.usuarios.model.Usuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

/**
 * Bean encargado de administrar la sesión del usuario.
 * Controla el estado de autenticación, el usuario actual
 * y las redirecciones relacionadas con la sesión.
 */
@Named
@SessionScoped
public class SessionManager implements Serializable {

    /**
     * Identificador del usuario autenticado.
     * Se almacena para recuperar la información del usuario cuando sea necesario.
     */
    private UUID usuarioId;

    /**
     * Usuario actualmente autenticado.
     * Se recarga desde la base de datos para evitar información desactualizada.
     */
    private Usuario usuarioActual;

    /**
     * Servicio de usuarios.
     * Se utiliza para buscar y recargar la información del usuario.
     */
    @Inject
    private UsuarioService usuarioService;

    /**
     * Establece la sesión del usuario autenticado.
     * Guarda el identificador y el objeto usuario.
     *
     * @param usuario usuario autenticado
     */
    public void establecerSesion(Usuario usuario) {
        this.usuarioId = usuario.getId();
        this.usuarioActual = usuario;
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     * Cada vez que se invoca, recarga el usuario desde la base de datos
     * para evitar trabajar con datos obsoletos.
     *
     * @return usuario actual o null si no hay sesión
     */
    public Usuario getUsuarioActual() {

        // Si no hay usuario en sesión, retorna null
        if (usuarioId == null) return null;

        // Recarga el usuario desde la base de datos
        usuarioActual = usuarioService.buscarPorId(usuarioId);

        return usuarioActual;
    }

    /**
     * Indica si existe una sesión autenticada.
     *
     * @return true si el usuario está autenticado, false en caso contrario
     */
    public boolean isAutenticado() {
        return usuarioId != null;
    }

    /**
     * Cierra la sesión del usuario.
     * Invalida la sesión HTTP y redirige a la página de login.
     *
     * @throws IOException en caso de error de redirección
     */
    public void cerrarSesion() throws IOException {

        // Invalida la sesión actual
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .invalidateSession();

        // Redirige a la página de login
        FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect("login.xhtml");
    }

    /**
     * Redirige al login si el usuario no está autenticado.
     *
     * @throws IOException en caso de error de redirección
     */
    public void redirigirSiNoAutenticado() throws IOException {

        if (!isAutenticado()) {
            FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("login.xhtml");
        }
    }

    /**
     * Redirige a la vista de usuarios si el usuario ya está autenticado.
     *
     * @throws IOException en caso de error de redirección
     */
    public void redirigirSiAutenticado() throws IOException {

        if (isAutenticado()) {
            FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("usuarios.xhtml");
        }
    }
}
