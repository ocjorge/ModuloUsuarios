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

    // IDs seleccionados desde combos
    private Integer tipoUsuarioId;     // 1 = interno, 2 = externo, 3 = cliente final
    private Integer rolId;
    private Integer estadoCuentaId;
    private String moduloId;

    // Filtro global
    private String filtro;

    // Empresas permitidas para externos
    private String empresaCodigo; // FV, KNS, GRN, CRE

    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ============================================
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ============================================
    public void initForm() {
        if (idParam == null || idParam.isBlank()) {
            usuarioSeleccionado = new Usuario();
            edicion = false;
            tipoUsuarioId = null;
            rolId = null;
            estadoCuentaId = null;
            moduloId = null;
            empresaCodigo = null;
            return;
        }

        try {
            UUID uuid = UUID.fromString(idParam);
            Usuario u = usuarioService.buscarPorId(uuid);

            if (u != null) {

                usuarioSeleccionado = u;
                edicion = true;

                tipoUsuarioId = (u.getTipoUsuario() != null) ? u.getTipoUsuario().getId() : null;
                rolId = (u.getRolInterno() != null) ? u.getRolInterno().getId() : null;
                estadoCuentaId = (u.getEstadoCuenta() != null) ? u.getEstadoCuenta().getId() : null;
                moduloId = (u.getModulo() != null) ? u.getModulo().getId() : null;

                empresaCodigo = null;

                if (isExterno() && usuarioSeleccionado.getUsername() != null) {
                    String user = usuarioSeleccionado.getUsername();
                    if (user.startsWith("FV-")) empresaCodigo = "FV";
                    if (user.startsWith("KNS-")) empresaCodigo = "KNS";
                    if (user.startsWith("GRN-")) empresaCodigo = "GRN";
                    if (user.startsWith("CRE-")) empresaCodigo = "CRE";
                }
            }

        } catch (Exception e) {
            edicion = false;
        }
    }

    // ============================================
    // Reglas de negocio
    public boolean isInterno() {
        return tipoUsuarioId != null && tipoUsuarioId == 1;
    }

    public boolean isExterno() {
        return tipoUsuarioId != null && tipoUsuarioId == 2;
    }

    public boolean isClienteFinal() {
        return tipoUsuarioId != null && tipoUsuarioId == 3;
    }

    // Acceso desde JSF
    public boolean getInterno() { return isInterno(); }
    public boolean getExterno() { return isExterno(); }
    public boolean getClienteFinal() { return isClienteFinal(); }

    // ============================================
    public void guardar() {
        try {
            if (tipoUsuarioId == null) {
                mensaje("Debe seleccionar el tipo de usuario", FacesMessage.SEVERITY_WARN);
                return;
            }

            usuarioSeleccionado.setTipoUsuario(
                usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId)
            );

            usuarioSeleccionado.setEstadoCuenta(
                usuarioService.buscarEstadoCuentaPorId(estadoCuentaId)
            );

            Modulo modulo = null;
            RolInterno rol = null;

            // ------- Interno -------
            if (isInterno()) {
                if (moduloId == null) {
                    mensaje("Debe elegir un módulo para usuarios internos", FacesMessage.SEVERITY_WARN);
                    return;
                }
                modulo = usuarioService.buscarModuloPorId(moduloId);

                if (rolId == null) {
                    mensaje("Debe elegir un rol para usuarios internos", FacesMessage.SEVERITY_WARN);
                    return;
                }
                rol = usuarioService.buscarRolPorId(rolId);

            // ------- Externo -------
            } else if (isExterno()) {
                modulo = usuarioService.buscarModuloPorId("PRV");
                rol = null;

                if (empresaCodigo == null || empresaCodigo.isBlank()) {
                    mensaje("Debe seleccionar la empresa del proveedor/banco", FacesMessage.SEVERITY_WARN);
                    return;
                }

                String base = usuarioSeleccionado.getUsername();
                String prefijo = empresaCodigo + "-";

                if (!base.startsWith(prefijo)) {
                    usuarioSeleccionado.setUsername(prefijo + base);
                }

            // ------- Cliente Final -------
            } else if (isClienteFinal()) {
                modulo = usuarioService.buscarModuloPorId("CLI");
                rol = null;
                empresaCodigo = null;
            }

            usuarioSeleccionado.setModulo(modulo);
            usuarioSeleccionado.setRolInterno(rol);

            // ------- Validar username duplicado -------
            Usuario repetido = usuarioService.buscarPorUsername(usuarioSeleccionado.getUsername());
            if (!edicion && repetido != null) {
                mensaje("El nombre de usuario ya existe", FacesMessage.SEVERITY_WARN);
                return;
            }

            // ------- Guardar -------
            if (!edicion) {
                if (passwordPlano == null || passwordPlano.isBlank()) {
                    mensaje("La contraseña es obligatoria", FacesMessage.SEVERITY_WARN);
                    return;
                }
                usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);
                mensaje("Usuario registrado correctamente", FacesMessage.SEVERITY_INFO);
            } else {
                boolean rehash = passwordPlano != null && !passwordPlano.isBlank();
                if (rehash) usuarioSeleccionado.setContrasena(passwordPlano);

                usuarioService.actualizar(usuarioSeleccionado, rehash);
                mensaje("Usuario actualizado correctamente", FacesMessage.SEVERITY_INFO);
            }

            cargarUsuarios();

        } catch (Exception e) {
            mensaje("No se pudo guardar: " + e.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    // ============================================
    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();
        mensaje("Usuario eliminado", FacesMessage.SEVERITY_INFO);
    }

    public void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    public void filtrar() {
        if (filtro == null || filtro.isBlank()) {
            cargarUsuarios();
            return;
        }
        String f = filtro.toLowerCase();
        usuarios = usuarios.stream()
                .filter(u ->
                        u.getNombreCompleto().toLowerCase().contains(f) ||
                        u.getCorreoElectronico().toLowerCase().contains(f) ||
                        u.getUsername().toLowerCase().contains(f)
                ).toList();
    }

    private void mensaje(String msg, FacesMessage.Severity tipo) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(tipo, msg, ""));
    }

    // ============================================
    // GETTERS / SETTERS
    public String getIdParam() { return idParam; }
    public void setIdParam(String idParam) { this.idParam = idParam; }
    public Usuario getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuario u) { this.usuarioSeleccionado = u; }
    public String getPasswordPlano() { return passwordPlano; }
    public void setPasswordPlano(String p) { this.passwordPlano = p; }
    public boolean isEdicion() { return edicion; }
    public List<Usuario> getUsuarios() { return usuarios; }
    public String getFiltro() { return filtro; }
    public void setFiltro(String f) { this.filtro = f; }
    public Integer getTipoUsuarioId() { return tipoUsuarioId; }
    public void setTipoUsuarioId(Integer id) { this.tipoUsuarioId = id; }
    public Integer getRolId() { return rolId; }
    public void setRolId(Integer id) { this.rolId = id; }
    public Integer getEstadoCuentaId() { return estadoCuentaId; }
    public void setEstadoCuentaId(Integer id) { this.estadoCuentaId = id; }
    public String getModuloId() { return moduloId; }
    public void setModuloId(String id) { this.moduloId = id; }
    public String getEmpresaCodigo() { return empresaCodigo; }
    public void setEmpresaCodigo(String c) { this.empresaCodigo = c; }
    public SessionManager getSessionManager() { return sessionManager; }

    public List<TipoUsuario> getTiposUsuario() { return usuarioService.listarTiposUsuario(); }
    public List<RolInterno> getRolesInternos() { return usuarioService.listarRoles(); }
    public List<EstadoCuenta> getEstadosCuenta() { return usuarioService.listarEstadosCuenta(); }
    public List<Modulo> getModulos() { return usuarioService.listarModulos(); }
}
