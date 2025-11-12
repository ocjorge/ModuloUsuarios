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

    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado = new Usuario();
    private String passwordPlano;
    private boolean edicion;

    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    public void prepararNuevo() {
        this.usuarioSeleccionado = new Usuario();
        this.passwordPlano = null;
        this.edicion = false;
    }

    public void cargarParaEdicion(String id) {
        if (id != null) {
            Usuario encontrado = usuarioService.buscarPorId(UUID.fromString(id));
            if (encontrado != null) {
                this.usuarioSeleccionado = encontrado;
                this.edicion = true;
            }
        }
    }

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
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "La contraseña es obligatoria para un usuario nuevo", ""));
                    return;
                }
                usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario registrado", ""));
            }
            cargarUsuarios();
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar el usuario", ex.getMessage()));
        }
    }

    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", ""));
    }

    private void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

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