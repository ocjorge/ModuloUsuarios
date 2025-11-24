package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import mx.tecnm.toluca.usuarios.model.*;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private SessionManager sessionManager;

    // Parametro recibido por URL
    private String idParam;

    // Estado del formulario
    private boolean edicion;

    // Datos del usuario
    private Usuario usuarioSeleccionado = new Usuario();
    private String passwordPlano;

    private List<Usuario> usuarios;

    // ============================================================
    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ============================================================
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ============================================================
    public void initForm() {
        if (idParam == null || idParam.isBlank()) {
            prepararNuevo();
        } else {
            cargarParaEdicion(idParam);
        }
    }

    public void prepararNuevo() {
        usuarioSeleccionado = new Usuario();
        passwordPlano = null;
        edicion = false;
    }

    // ============================================================
    public void cargarParaEdicion(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Usuario encontrado = usuarioService.buscarPorId(uuid);
            if (encontrado != null) {
                usuarioSeleccionado = encontrado;
                edicion = true;
            }
        } catch (Exception e) {
            edicion = false;
        }
    }

    // ============================================================
    public void guardar() {
        try {

            if (edicion) {
                boolean rehash = passwordPlano != null && !passwordPlano.isBlank();
                if (rehash) {
                    usuarioSeleccionado.setContrasena(passwordPlano);
                }
                usuarioService.actualizar(usuarioSeleccionado, rehash);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario actualizado", ""));

            } else {
                if (passwordPlano == null || passwordPlano.isBlank()) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN,
                                    "La contraseña es obligatoria para un usuario nuevo", ""));
                    return;
                }

                usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);

                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado", ""));
            }

            cargarUsuarios();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "No se pudo guardar el usuario", ex.getMessage()));
        }
    }

    // ============================================================
    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", ""));
    }

    // ============================================================
    private void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    // ============================================================
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public String getPasswordPlano() {
        return passwordPlano;
    }

    public void setPasswordPlano(String passwordPlano) {
        this.passwordPlano = passwordPlano;
    }

    public boolean isEdicion() {
        return edicion;
    }

    public String getIdParam() {
        return idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    // Datos de catálogos
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

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
