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
    private Integer tipoUsuarioId;   // 1 = interno, 2 = externo, 3 = cliente final
    private Integer rolId;
    private Integer estadoCuentaId;
    private String moduloId;

    // FILTRO GLOBAL
    private String filtro;

    // Empresa para externos (códigos: FV, KNS, GRN, CRE)
    private String empresaCodigo;

    @PostConstruct
    public void init() {
        cargarUsuarios();
    }

    // ================================
    // SESIÓN
    // ================================
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    // ================================
    // INICIALIZAR FORMULARIO
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
            empresaCodigo = null;
            passwordPlano = null;

            return;
        }

        try {
            UUID uuid = UUID.fromString(idParam);
            Usuario u = usuarioService.buscarPorId(uuid);

            if (u != null) {
                usuarioSeleccionado = u;
                edicion = true;

                tipoUsuarioId  = (u.getTipoUsuario()   != null) ? u.getTipoUsuario().getId()   : null;
                rolId          = (u.getRolInterno()    != null) ? u.getRolInterno().getId()    : null;
                estadoCuentaId = (u.getEstadoCuenta()  != null) ? u.getEstadoCuenta().getId()  : null;
                moduloId       = (u.getModulo()        != null) ? u.getModulo().getId()        : null;

                // Inferir empresa para externos proveedores a partir del prefijo del username
                empresaCodigo = null;
                if (isExterno() && usuarioSeleccionado.getUsername() != null) {
                    String user = usuarioSeleccionado.getUsername();
                    if (user.startsWith("FV-"))  empresaCodigo = "FV";
                    else if (user.startsWith("KNS-")) empresaCodigo = "KNS";
                    else if (user.startsWith("GRN-")) empresaCodigo = "GRN";
                    else if (user.startsWith("CRE-")) empresaCodigo = "CRE";
                }
            }

        } catch (Exception ex) {
            edicion = false;
        }
    }

    // ================================
    // REGLAS DE NEGOCIO POR TIPO
    // ================================
    public boolean isInterno() {
        return tipoUsuarioId != null && tipoUsuarioId == 1;
    }

    public boolean isExterno() {
        return tipoUsuarioId != null && tipoUsuarioId == 2;
    }

    public boolean isClienteFinal() {
        return tipoUsuarioId != null && tipoUsuarioId == 3;
    }

    // Getters para EL
    public boolean getInterno()      { return isInterno(); }
    public boolean getExterno()      { return isExterno(); }
    public boolean getClienteFinal() { return isClienteFinal(); }

    // ================================
    // GUARDAR
    // ================================
    public void guardar() {
        try {
            // -------- Validaciones básicas ----------
            if (tipoUsuarioId == null) {
                agregarMensaje("Debe seleccionar el tipo de usuario", FacesMessage.SEVERITY_WARN);
                return;
            }

            if (usuarioSeleccionado.getUsername() == null ||
                    usuarioSeleccionado.getUsername().isBlank()) {
                agregarMensaje("Debe ingresar un nombre de usuario", FacesMessage.SEVERITY_ERROR);
                return;
            }

            // Entidades base
            if (estadoCuentaId != null) {
                usuarioSeleccionado.setEstadoCuenta(
                        usuarioService.buscarEstadoCuentaPorId(estadoCuentaId)
                );
            } else {
                agregarMensaje("Debe seleccionar el estado de la cuenta", FacesMessage.SEVERITY_WARN);
                return;
            }

            usuarioSeleccionado.setTipoUsuario(
                    usuarioService.buscarTipoUsuarioPorId(tipoUsuarioId)
            );

            Modulo modulo = null;
            RolInterno rol = null;

            // ================== CASO INTERNO (tipo 1) ==================
            if (isInterno()) {

                // Módulo obligatorio y libre (ERP, CRM, USR, BCO...)
                if (moduloId == null || moduloId.isBlank()) {
                    agregarMensaje("Debe elegir el módulo para usuarios internos", FacesMessage.SEVERITY_WARN);
                    return;
                }
                modulo = usuarioService.buscarModuloPorId(moduloId);

                // Rol obligatorio
                if (rolId == null) {
                    agregarMensaje("El rol es obligatorio para usuarios internos", FacesMessage.SEVERITY_WARN);
                    return;
                }
                rol = usuarioService.buscarRolPorId(rolId);
                if (rol == null) {
                    agregarMensaje("El rol seleccionado no es válido", FacesMessage.SEVERITY_ERROR);
                    return;
                }

                // No empresa
                empresaCodigo = null;

            // ================== CASO EXTERNO (tipo 2) ==================
            } else if (isExterno()) {

                if (moduloId == null || moduloId.isBlank()) {
                    agregarMensaje("Para usuarios externos solo se permite módulo PRV o BCO", FacesMessage.SEVERITY_WARN);
                    return;
                }

                if (!"PRV".equals(moduloId) && !"BCO".equals(moduloId)) {
                    agregarMensaje("Para usuarios externos solo se permite módulo PRV (proveedores) o BCO (bancos)",
                            FacesMessage.SEVERITY_ERROR);
                    return;
                }

                modulo = usuarioService.buscarModuloPorId(moduloId);

                // Externos NUNCA llevan rol (chk_rol_interno)
                rol = null;

                // Si es proveedor (PRV): empresa obligatoria + prefijo en ALTA
                if ("PRV".equals(moduloId)) {
                    if (empresaCodigo == null || empresaCodigo.isBlank()) {
                        agregarMensaje("Debe seleccionar la empresa del proveedor (FV, KNS, GRN, CRE)",
                                FacesMessage.SEVERITY_WARN);
                        return;
                    }

                    if (!edicion) {
                        // Opción A: solo prefijar cuando es ALTA nueva
                        String baseUser = usuarioSeleccionado.getUsername().trim();
                        String prefijo = empresaCodigo + "-";
                        if (!baseUser.startsWith(prefijo)) {
                            usuarioSeleccionado.setUsername(prefijo + baseUser);
                        }
                    }

                } else {
                    // Externo banco (BCO): sin empresa, sin prefijo
                    empresaCodigo = null;
                }

            // ================== CASO CLIENTE FINAL (tipo 3) ==================
            } else if (isClienteFinal()) {

                // Siempre módulo CLI
                modulo = usuarioService.buscarModuloPorId("CLI");
                // Sin rol
                rol = null;
                // Sin empresa
                empresaCodigo = null;

                // Prefijo CLI- SOLO en alta
                if (!edicion) {
                    String baseUser = usuarioSeleccionado.getUsername().trim();
                    String prefijo = "CLI-";
                    if (!baseUser.startsWith(prefijo)) {
                        usuarioSeleccionado.setUsername(prefijo + baseUser);
                    }
                }
            }

            usuarioSeleccionado.setModulo(modulo);
            usuarioSeleccionado.setRolInterno(rol);

            // Validar username repetido ya con prefijo aplicado (si corresponde)
            Usuario repetido = usuarioService.buscarPorUsername(usuarioSeleccionado.getUsername());
            if (!edicion && repetido != null) {
                agregarMensaje("El nombre de usuario ya existe", FacesMessage.SEVERITY_ERROR);
                return;
            }

            // -------- Alta o edición ----------
            if (edicion) {
                boolean rehash = (passwordPlano != null && !passwordPlano.isBlank());
                if (rehash) {
                    // Se rehashea en el servicio
                    usuarioSeleccionado.setContrasena(passwordPlano);
                }

                usuarioService.actualizar(usuarioSeleccionado, rehash);
                agregarMensaje("Usuario actualizado correctamente", FacesMessage.SEVERITY_INFO);

            } else {
                if (passwordPlano == null || passwordPlano.isBlank()) {
                    agregarMensaje("La contraseña es obligatoria para nuevos usuarios",
                            FacesMessage.SEVERITY_WARN);
                    return;
                }

                usuarioService.guardarNuevo(usuarioSeleccionado, passwordPlano);
                agregarMensaje("Usuario registrado correctamente", FacesMessage.SEVERITY_INFO);
            }

            cargarUsuarios();

        } catch (Exception ex) {
            agregarMensaje("No se pudo guardar el usuario: " + ex.getMessage(),
                    FacesMessage.SEVERITY_ERROR);
        }
    }

    // ================================
    public void eliminar(UUID id) {
        usuarioService.eliminar(id);
        cargarUsuarios();
        agregarMensaje("Usuario eliminado", FacesMessage.SEVERITY_INFO);
    }

    // ================================
    public void cargarUsuarios() {
        usuarios = usuarioService.listarUsuarios();
    }

    // ================================
    // FILTRO GLOBAL
    // ================================
    public void filtrar() {
        if (filtro == null || filtro.isBlank()) {
            cargarUsuarios();
            return;
        }

        String f = filtro.toLowerCase();

        usuarios = usuarios.stream()
                .filter(u ->
                        (u.getNombreCompleto()   != null && u.getNombreCompleto().toLowerCase().contains(f)) ||
                        (u.getCorreoElectronico() != null && u.getCorreoElectronico().toLowerCase().contains(f)) ||
                        (u.getUsername()          != null && u.getUsername().toLowerCase().contains(f))
                )
                .toList();
    }

    // ================================
    private void agregarMensaje(String msg, FacesMessage.Severity tipo) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(tipo, msg, ""));
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

    public String getEmpresaCodigo() {
        return empresaCodigo;
    }

    public void setEmpresaCodigo(String empresaCodigo) {
        this.empresaCodigo = empresaCodigo;
    }
}
