package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import java.util.List;

import mx.tecnm.toluca.usuarios.model.*;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private SessionManager sessionManager;

    private String idParam;
    private boolean edicion;

    private Usuario usuarioSeleccionado = new Usuario();
    private String passwordPlano;
    private List<Usuario> usuarios;

    // ---- IDs para selects (DENTRO DE LA CLASE) ----
    private Integer tipoUsuarioId;
    private Integer rolId;
    private Integer estadoCuentaId;
    private String moduloId;



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
            // Modo nuevo
            usuarioSeleccionado = new Usuario();
            edicion = false;

            tipoUsuarioId = null;
            rolId = null;
            estadoCuentaId = null;
            moduloId = null;

            return;
        }

        // Modo edición
        try {
            UUID uuid = UUID.fromString(idParam);
            Usuario u = usuarioService.buscarPorId(uuid);

            if (u != null) {
                usuarioSeleccionado = u;
                edicion = true;

                tipoUsuarioId  = (u.getTipoUsuario() != null) ? u.getTipoUsuario().getId() : null;
                rolId          = (u.getRolInterno() != null) ? u.getRolInterno().getId() : null;
                estadoCuentaId = (u.getEstadoCuenta() != null) ? u.getEstadoCuenta().getId() : null;
                moduloId = (u.getModulo() != null) ? u.getModulo().getId() : null;

            }

        } catch (Exception ex) {
            edicion = false;
        }
    }

    // ============================================================
    public void guardar() {
    try {

        // ============================
        // VALIDAR USERNAME
        // ============================
        if (usuarioSeleccionado.getUsername() == null ||
            usuarioSeleccionado.getUsername().isBlank()) {

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "El username es obligatorio", ""));
            return;
        }

        // Buscar si ya existe un usuario con ese username
        Usuario repetido = usuarioService.buscarPorUsername(usuarioSeleccionado.getUsername());

        if (!edicion && repetido != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ese username ya existe. Usa uno diferente.", ""));
            return;
        }

        if (edicion && repetido != null &&
            !repetido.getId().equals(usuarioSeleccionado.getId())) {

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Ese username ya está asignado a otro usuario.", ""));
            return;
        }


        // ============================
        // ASIGNAR ENTIDADES (catálogos)
        // ============================
        usuarioSeleccionado.setTipoUsuario(
                usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId)
        );

        usuarioSeleccionado.setRolInterno(
                usuarioService.buscarRolPorId(rolId)
        );

        usuarioSeleccionado.setEstadoCuenta(
                usuarioService.buscarEstadoCuentaPorId(estadoCuentaId)
        );

        usuarioSeleccionado.setModulo(
                usuarioService.buscarModuloPorId(moduloId)
        );


        // ============================
        // GUARDAR / ACTUALIZAR
        // ============================
        if (edicion) {

            boolean rehash = passwordPlano != null && !passwordPlano.isBlank();

            if (rehash) {
                usuarioSeleccionado.setContrasena(passwordPlano);
            }

            usuarioService.actualizar(usuarioSeleccionado, rehash);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Usuario actualizado correctamente", ""));

        } else {

            if (passwordPlano == null || passwordPlano.isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "La contraseña es obligatoria para nuevos usuarios", ""));
                return;
            }

            usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Usuario registrado correctamente", ""));
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    // ============================================================
    // Getters / Setters
    // ============================================================

    public String getIdParam() { return idParam; }
    public void setIdParam(String idParam) { this.idParam = idParam; }

    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario u) { this.usuarioSeleccionado = u; }

    public String getPasswordPlano() { return passwordPlano; }
    public void setPasswordPlano(String passwordPlano) { this.passwordPlano = passwordPlano; }

    public boolean isEdicion() { return edicion; }

    // Catálogos
    public List<TipoUsuario> getTiposUsuario() { return usuarioService.listarTiposUsuario(); }
    public List<RolInterno> getRolesInternos() { return usuarioService.listarRoles(); }
    public List<EstadoCuenta> getEstadosCuenta() { return usuarioService.listarEstadosCuenta(); }
    public List<Modulo> getModulos() { return usuarioService.listarModulos(); }

    public SessionManager getSessionManager() { return sessionManager; }

    // IDs para selects
public Integer getTipoUsuarioId() { return tipoUsuarioId; }
public void setTipoUsuarioId(Integer id) { this.tipoUsuarioId = id; }

public Integer getRolId() { return rolId; }
public void setRolId(Integer id) { this.rolId = id; }

public Integer getEstadoCuentaId() { return estadoCuentaId; }
public void setEstadoCuentaId(Integer id) { this.estadoCuentaId = id; }

public String getModuloId() { return moduloId; }
public void setModuloId(String moduloId) { this.moduloId = moduloId; }
private String filtro;

public String getFiltro() {
    return filtro;
}

public void setFiltro(String filtro) {
    this.filtro = filtro;
}

public void filtrar() {
    if (filtro == null || filtro.isBlank()) {
        usuarios = usuarioService.listarUsuarios();
        return;
    }

    String f = filtro.toLowerCase();

    usuarios = usuarioService.listarUsuarios()
            .stream()
            .filter(u ->
                    (u.getNombreCompleto() != null && u.getNombreCompleto().toLowerCase().contains(f)) ||
                    (u.getCorreoElectronico() != null && u.getCorreoElectronico().toLowerCase().contains(f)) ||
                    (u.getUsername() != null && u.getUsername().toLowerCase().contains(f))
            ).toList();
}



}
