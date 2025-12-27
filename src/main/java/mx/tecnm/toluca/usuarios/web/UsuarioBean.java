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

    // Parámetro recibido en URL
    private String idParam;

    // Control del formulario
    private boolean edicion;

    // Usuario en edición/creación
    private Usuario usuarioSeleccionado = new Usuario();
    private String passwordPlano;

    private List<Usuario> usuarios;

    // IDs de selects
    private Integer tipoUsuarioId;
    private Integer rolId;
    private Integer estadoCuentaId;
    private String moduloId;

    // FILTRO GLOBAL
    private String filtro;

    // ================================
    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ================================
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ================================
    public void initForm() {

        if (idParam == null || idParam.isBlank()) {
            // Crear nuevo
            usuarioSeleccionado = new Usuario();
            edicion = false;

            tipoUsuarioId = null;
            rolId = null;
            estadoCuentaId = null;
            moduloId = null;

            return;
        }

        // Edición
        try {
            UUID uuid = UUID.fromString(idParam);
            Usuario u = usuarioService.buscarPorId(uuid);

            if (u != null) {
                usuarioSeleccionado = u;
                edicion = true;

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
    public void guardar() {
        try {

            // Asignar entidades con los IDs
            usuarioSeleccionado.setTipoUsuario(usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId));
            usuarioSeleccionado.setRolInterno(usuarioService.buscarRolPorId(rolId));
            usuarioSeleccionado.setEstadoCuenta(usuarioService.buscarEstadoCuentaPorId(estadoCuentaId));
            usuarioSeleccionado.setModulo(usuarioService.buscarModuloPorId(moduloId));

            // Username obligatorio
            if (usuarioSeleccionado.getUsername() == null || usuarioSeleccionado.getUsername().isBlank()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Debe ingresar un nombre de usuario", ""));
                return;
            }

            // Validar username repetido
            Usuario repetido = usuarioService.buscarPorUsername(usuarioSeleccionado.getUsername());
            if (!edicion && repetido != null) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "El nombre de usuario ya existe", ""));
                return;
            }

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
                                    "La contraseña es obligatoria", ""));
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

    // ================================
    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado", ""));
    }

    // ================================
    public void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    // ================================
    // FILTRO GLOBAL
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
    public String getIdParam() {
        return idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario u) {
        this.usuarioSeleccionado = u;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public String getFiltro() {
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Integer getTipoUsuarioId() {
        return tipoUsuarioId;
    }

    public void setTipoUsuarioId(Integer tipoUsuarioId) {
        this.tipoUsuarioId = tipoUsuarioId;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public Integer getEstadoCuentaId() {
        return estadoCuentaId;
    }

    public void setEstadoCuentaId(Integer estadoCuentaId) {
        this.estadoCuentaId = estadoCuentaId;
    }

    public String getModuloId() {
        return moduloId;
    }

    public void setModuloId(String moduloId) {
        this.moduloId = moduloId;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
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
}
