package mx.tecnm.toluca.usuarios.web;

// Importación para ejecutar lógica después de crear el bean
import jakarta.annotation.PostConstruct;

// Importaciones para mensajes y contexto JSF
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// Alcance del bean a nivel de vista
import jakarta.faces.view.ViewScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para E/S y serialización
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

// Importaciones del modelo
import mx.tecnm.toluca.usuarios.model.*;

// Importación del servicio de usuarios
import mx.tecnm.toluca.usuarios.service.UsuarioService;

/**
 * Bean de respaldo para la gestión de usuarios.
 * Maneja listado, creación, edición, eliminación
 * y filtrado de usuarios desde la vista JSF.
 */
@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    /**
     * Servicio de usuarios.
     * Proporciona la lógica de negocio relacionada con usuarios.
     */
    @Inject
    private UsuarioService usuarioService;

    /**
     * Administrador de sesión.
     * Se utiliza para verificar autenticación y redirecciones.
     */
    @Inject
    private SessionManager sessionManager;

    /**
     * Parámetro recibido por URL (id del usuario).
     */
    private String idParam;

    /**
     * Indica si el formulario está en modo edición.
     */
    private boolean edicion;

    /**
     * Usuario actualmente seleccionado (crear / editar).
     */
    private Usuario usuarioSeleccionado = new Usuario();

    /**
     * Contraseña en texto plano capturada desde el formulario.
     */
    private String passwordPlano;

    /**
     * Lista de usuarios mostrados en la vista.
     */
    private List<Usuario> usuarios;

    // ---------------- IDS DE SELECTS ----------------

    /**
     * Identificador del tipo de usuario seleccionado.
     */
    private Integer tipoUsuarioId;

    /**
     * Identificador del rol interno seleccionado.
     */
    private Integer rolId;

    /**
     * Identificador del estado de cuenta seleccionado.
     */
    private Integer estadoCuentaId;

    /**
     * Identificador del módulo seleccionado.
     */
    private String moduloId;

    // ---------------- FILTRO GLOBAL ----------------

    /**
     * Texto utilizado para el filtro global de usuarios.
     */
    private String filtro;

    // ================================
    /**
     * Método ejecutado después de crear el bean.
     * Carga inicialmente la lista de usuarios.
     */
    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ================================
    /**
     * Verifica si existe una sesión activa.
     * Si no está autenticado, redirige al login.
     */
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ================================
    /**
     * Inicializa el formulario de usuario.
     * Determina si se trata de creación o edición
     * en función del parámetro recibido por URL.
     */
    public void initForm() {

        // Crear nuevo usuario
        if (idParam == null || idParam.isBlank()) {

            usuarioSeleccionado = new Usuario();
            edicion = false;

            tipoUsuarioId = null;
            rolId = null;
            estadoCuentaId = null;
            moduloId = null;

            return;
        }

        // Edición de usuario existente
        try {
            UUID uuid = UUID.fromString(idParam);
            Usuario u = usuarioService.buscarPorId(uuid);

            if (u != null) {
                usuarioSeleccionado = u;
                edicion = true;

                // Carga de IDs para los selects
                tipoUsuarioId  = (u.getTipoUsuario() != null) ? u.getTipoUsuario().getId() : null;
                rolId          = (u.getRolInterno() != null) ? u.getRolInterno().getId() : null;
                estadoCuentaId = (u.getEstadoCuenta() != null) ? u.getEstadoCuenta().getId() : null;
                moduloId       = (u.getModulo() != null) ? u.getModulo().getId() : null;
            }

        } catch (Exception ex) {
            edicion = false;
        }
    }

    // ================================
    /**
     * Guarda o actualiza un usuario según el modo.
     * Incluye validaciones de username y contraseña.
     */
    public void guardar() {
        try {

            // Asigna las entidades relacionadas usando los IDs seleccionados
            usuarioSeleccionado.setTipoUsuario(usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId));
            usuarioSeleccionado.setRolInterno(usuarioService.buscarRolPorId(rolId));
            usuarioSeleccionado.setEstadoCuenta(usuarioService.buscarEstadoCuentaPorId(estadoCuentaId));
            usuarioSeleccionado.setModulo(usuarioService.buscarModuloPorId(moduloId));

            // Validación de username obligatorio
            if (usuarioSeleccionado.getUsername() == null || usuarioSeleccionado.getUsername().isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe ingresar un nombre de usuario", ""));
                return;
            }

            // Validación de username repetido en alta
            Usuario repetido = usuarioService.buscarPorUsername(usuarioSeleccionado.getUsername());
            if (!edicion && repetido != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "El nombre de usuario ya existe", ""));
                return;
            }

            // ---------------- EDICIÓN ----------------
            if (edicion) {

                // Rehash de contraseña solo si se capturó una nueva
                boolean rehash = passwordPlano != null && !passwordPlano.isBlank();
                if (rehash) {
                    usuarioSeleccionado.setContrasena(passwordPlano);
                }

                usuarioService.actualizar(usuarioSeleccionado, rehash);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Usuario actualizado correctamente", ""));

            // ---------------- ALTA ----------------
            } else {

                // Contraseña obligatoria en alta
                if (passwordPlano == null || passwordPlano.isBlank()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "La contraseña es obligatoria", ""));
                    return;
                }

                usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO,
                                "Usuario registrado correctamente", ""));
            }

            // Recarga la lista de usuarios
            cargarUsuarios();

        } catch (Exception ex) {

            // Manejo de error general
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo guardar el usuario", ex.getMessage()));
        }
    }

    // ================================
    /**
     * Elimina un usuario por su identificador.
     */
    public void eliminar(UUID id) {

        usuarioService.eliminar(id);
        cargarUsuarios();

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", "")
        );
    }

    // ================================
    /**
     * Carga la lista completa de usuarios.
     */
    public void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    // ================================
    // FILTRO GLOBAL
    /**
     * Aplica un filtro global por nombre, correo o username.
     */
    public void filtrar() {

        if (filtro == null || filtro.isBlank()) {
            cargarUsuarios();
            return;
        }

        String f = filtro.toLowerCase();

        usuarios = usuarios.stream()
                .filter(u ->
                        (u.getNombreCompleto() != null && u.getNombreCompleto().toLowerCase().contains(f)) ||
                        (u.getCorreoElectronico() != null && u.getCorreoElectronico().toLowerCase().contains(f)) ||
                        (u.getUsername() != null && u.getUsername().toLowerCase().contains(f))
                )
                .toList();
    }

    // ================================
    // GETTERS / SETTERS
    // ================================

    public String getIdParam() { return idParam; }
    public void setIdParam(String idParam) { this.idParam = idParam; }

    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario u) { this.usuarioSeleccionado = u; }

    public String getPasswordPlano() { return passwordPlano; }
    public void setPasswordPlano(String passwordPlano) { this.passwordPlano = passwordPlano; }

    public boolean isEdicion() { return edicion; }

    public List<Usuario> getUsuarios() { return usuarios; }

    public String getFiltro() { return filtro; }
    public void setFiltro(String filtro) { this.filtro = filtro; }

    public Integer getTipoUsuarioId() { return tipoUsuarioId; }
    public void setTipoUsuarioId(Integer tipoUsuarioId) { this.tipoUsuarioId = tipoUsuarioId; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public Integer getEstadoCuentaId() { return estadoCuentaId; }
    public void setEstadoCuentaId(Integer estadoCuentaId) { this.estadoCuentaId = estadoCuentaId; }

    public String getModuloId() { return moduloId; }
    public void setModuloId(String moduloId) { this.moduloId = moduloId; }

    public SessionManager getSessionManager() { return sessionManager; }

    // ---------------- CATÁLOGOS ----------------

    public List<TipoUsuario> getTiposUsuario() {
        return usuarioService.listarTiposUsuario();
    }

    public List<RolInterno> getRolesInternos() {
        return usuarioService.listarRoles();
    }

    public List<EstadoCuenta> getEstadosCuenta() {
        return usuarioService.listarEstadosCuenta();
    }

    public List<Modulo> getModulos() {
        return usuarioService.listarModulos();
    }
}
