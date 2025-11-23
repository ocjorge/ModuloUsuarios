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

    // --- Parámetro recibido desde la vista
    private UUID id;

    // --- IDs seleccionados en el formulario
    private UUID tipoUsuarioId;
    private UUID rolId;
    private UUID estadoCuentaId;
    private UUID moduloId;

    // Datos del usuario
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado = new Usuario();
    private String passwordPlano;
    private boolean edicion;

    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ---------------- SESIÓN ----------------
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ---------------- LISTADO ----------------
    private void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    // ---------------- EDICIÓN ----------------
public void cargarParaEdicion(String id) {
    if (id != null) {
        Usuario encontrado = usuarioService.buscarPorId(UUID.fromString(id));
        if (encontrado != null) {
            this.usuarioSeleccionado = encontrado;
            this.edicion = true;
        }
    }
}


    // ---------------- GUARDAR ----------------
    public void guardar() {
        try {

            // Cargar entidades a partir de los IDs seleccionados
            usuarioSeleccionado.setTipoUsuario(usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId));
            usuarioSeleccionado.setRolInterno(usuarioService.buscarRolPorId(rolId));
            usuarioSeleccionado.setEstadoCuenta(usuarioService.buscarEstadoCuentaPorId(estadoCuentaId));
            usuarioSeleccionado.setModulo(usuarioService.buscarModuloPorId(moduloId));

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

    // ---------------- ELIMINAR ----------------
    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", ""));
    }

    // ----------- GETTERS / SETTERS ------------

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) { this.usuarioSeleccionado = usuarioSeleccionado; }

    public String getPasswordPlano() { return passwordPlano; }
    public void setPasswordPlano(String passwordPlano) { this.passwordPlano = passwordPlano; }

    public boolean isEdicion() { return edicion; }

    public List<TipoUsuario> getTiposUsuario() { return usuarioService.listarTiposUsuario(); }
    public List<RolInterno> getRolesInternos() { return usuarioService.listarRoles(); }
    public List<EstadoCuenta> getEstadosCuenta() { return usuarioService.listarEstadosCuenta(); }
    public List<Modulo> getModulos() { return usuarioService.listarModulos(); }

    public UUID getTipoUsuarioId() { return tipoUsuarioId; }
    public void setTipoUsuarioId(UUID id) { this.tipoUsuarioId = id; }

    public UUID getRolId() { return rolId; }
    public void setRolId(UUID id) { this.rolId = id; }

    public UUID getEstadoCuentaId() { return estadoCuentaId; }
    public void setEstadoCuentaId(UUID id) { this.estadoCuentaId = id; }

    public UUID getModuloId() { return moduloId; }
    public void setModuloId(UUID id) { this.moduloId = id; }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
