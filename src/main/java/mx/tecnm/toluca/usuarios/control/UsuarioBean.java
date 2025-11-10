package mx.tecnm.toluca.usuarios.control;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import mx.tecnm.toluca.usuarios.modelo.EstadoCuenta;
import mx.tecnm.toluca.usuarios.modelo.Modulo;
import mx.tecnm.toluca.usuarios.modelo.RolInterno;
import mx.tecnm.toluca.usuarios.modelo.TipoUsuario;
import mx.tecnm.toluca.usuarios.modelo.Usuario;
import mx.tecnm.toluca.usuarios.modelo.AuditoriaAccion;
import mx.tecnm.toluca.usuarios.modelo.Accion; // Importar la entidad Accion

@Named("usuarioBean") // Nombre explícito para usar en JSF
@ViewScoped
public class UsuarioBean implements Serializable {

    @Inject
    private EntityManager em;
    
    @Inject
    private LoginBean loginBean; // Para obtener el usuario actual en auditoría

    private List<Usuario> usuarios;
    private Usuario selectedUser; // Para editar/detalles
    
    // Campos para la creación de nuevo usuario
    private String newUserName;
    private String newUserEmail;
    private String newUserPhone;
    private String newUserPassword;
    private String newUserConfirmPassword;
    private Integer newUserTipoUsuario;
    private Integer newUserRole; // ID del rol
    private String newUserModule; // ID del módulo
    private Integer newUserStatus = 1; // Por defecto Activo (ID 1)
    private String newUserAddress;

    // Listas para dropdowns
    private List<TipoUsuario> tiposUsuarios;
    private List<RolInterno> rolesInternos;
    private List<EstadoCuenta> estadosCuenta;
    private List<Modulo> modulos;

    @PostConstruct
    public void init() {
        loadUsuarios();
        loadDropdowns();
    }
    
    // En UsuarioBean.java - agregar estos métodos:
    public void onTipoUsuarioChange() {
        // Solo para que el f:ajax funcione
        System.out.println("Tipo de usuario cambiado a: " + newUserTipoUsuario);
    }

    public void saveNewUser() {
        createUsuario(); // Redirigir al método existente
    }

    public void loadUsuarios() {
        // Carga ansiosa para las entidades relacionadas (JOIN FETCH)
        usuarios = em.createQuery("SELECT u FROM Usuario u JOIN FETCH u.idTipoUsuario JOIN FETCH u.idEstadoCuenta LEFT JOIN FETCH u.idRol LEFT JOIN FETCH u.idModulo ORDER BY u.nombreCompleto", Usuario.class)
                .getResultList();
    }

    public void loadDropdowns() {
        tiposUsuarios = em.createQuery("SELECT t FROM TipoUsuario t", TipoUsuario.class).getResultList();
        rolesInternos = em.createQuery("SELECT r FROM RolInterno r", RolInterno.class).getResultList();
        estadosCuenta = em.createQuery("SELECT e FROM EstadoCuenta e", EstadoCuenta.class).getResultList();
        modulos = em.createQuery("SELECT m FROM Modulo m", Modulo.class).getResultList();
    }

    // Método para seleccionar un usuario para edición. Se invoca desde el f:ajax del h:commandLink.
    @Transactional
    public void selectUserForEdit(UUID id) {
        this.selectedUser = em.find(Usuario.class, id);
        if (selectedUser != null) {
            // Cargar los IDs de las entidades relacionadas en los campos del bean para el <h:selectOneMenu>
            newUserRole = (selectedUser.getIdRol() != null) ? selectedUser.getIdRol().getIdRol() : null;
            newUserModule = (selectedUser.getIdModulo() != null) ? selectedUser.getIdModulo().getIdModulo() : null;
            newUserStatus = selectedUser.getIdEstadoCuenta().getIdEstadoCuenta();
            newUserAddress = selectedUser.getDireccion(); // Cargar la dirección si existe
            
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario seleccionado", "Usuario " + selectedUser.getNombreCompleto() + " cargado para edición."));
        } else {
             FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró el usuario seleccionado."));
        }
    }

    @Transactional
    public void updateUsuario() {
        if (selectedUser == null || selectedUser.getIdUsuario() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado ningún usuario para actualizar."));
            return;
        }

        try {
            // No es necesario actualizar los campos básicos con sus propios setters si ya están en selectedUser
            // JPA detecta los cambios en la entidad managed y los persiste en merge.
            selectedUser.setDireccion(newUserAddress); // Actualizar dirección del campo del bean

            // Actualizar referencias a entidades usando los IDs del formulario
            selectedUser.setIdRol(newUserRole != null ? em.find(RolInterno.class, newUserRole) : null);
            selectedUser.setIdEstadoCuenta(newUserStatus != null ? em.find(EstadoCuenta.class, newUserStatus) : null);
            selectedUser.setIdModulo(newUserModule != null && !newUserModule.isEmpty() ? em.find(Modulo.class, newUserModule) : null);
            
            em.merge(selectedUser);
            
            // Registrar auditoría de acción (ID 2 para 'Editar')
            if (loginBean.getCurrentUser() != null) {
                AuditoriaAccion auditoria = new AuditoriaAccion(loginBean.getCurrentUser());
                auditoria.setIdModulo(em.find(Modulo.class, "Usuarios")); // Módulo de Usuarios
                auditoria.setIdAccion(em.find(Accion.class, 2)); // Acción 'Editar' (ID 2)
                auditoria.setIdRegistroAfectado(selectedUser.getIdUsuario().toString());
                auditoria.setDetallesCambio("{\"accion\": \"Actualización de datos de usuario\", \"usuario_afectado\": \"" + selectedUser.getCorreoElectronico() + "\"}");
                em.persist(auditoria);
            }


            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario actualizado correctamente."));
            loadUsuarios(); // Recargar la lista
            // selectedUser = null; // Limpiar la selección después de la actualización si no se necesita seguir editando
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al actualizar", "No se pudo actualizar el usuario: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Transactional
    public void createUsuario() {
        if (!newUserPassword.equals(newUserConfirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro.", "Las contraseñas no coinciden."));
            return;
        }

        try {
             // Verificar si el correo ya existe
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.correoElectronico = :email", Long.class);
            countQuery.setParameter("email", newUserEmail);
            if (countQuery.getSingleResult() > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro.", "El correo electrónico ya está en uso."));
                return;
            }

            Usuario newUser = new Usuario();
            newUser.setCorreoElectronico(newUserEmail);
            newUser.setNombreCompleto(newUserName);
            newUser.setTelefono(newUserPhone);
            newUser.setContrasena(BCrypt.hashpw(newUserPassword, BCrypt.gensalt()));

            // Obtener TipoUsuario, EstadoCuenta, RolInterno y Modulo por ID
            TipoUsuario tipoUsuario = em.find(TipoUsuario.class, newUserTipoUsuario);
            EstadoCuenta estadoCuenta = em.find(EstadoCuenta.class, newUserStatus);

            newUser.setIdTipoUsuario(tipoUsuario);
            newUser.setIdEstadoCuenta(estadoCuenta);

            if (newUserTipoUsuario == 1) { // Si es interno (ID 1 para 'Interno')
                RolInterno rol = em.find(RolInterno.class, newUserRole);
                newUser.setIdRol(rol);
                if (newUserModule != null && !newUserModule.isEmpty()) {
                    Modulo modulo = em.find(Modulo.class, newUserModule);
                    newUser.setIdModulo(modulo);
                }
            } else { // Si no es interno, asegúrate de que el rol y módulo sean null
                 newUser.setIdRol(null);
                 newUser.setIdModulo(null);
            }
            newUser.setDireccion(newUserAddress);
            newUser.setFechaCreacion(OffsetDateTime.now());

            em.persist(newUser);
            
            // Registrar auditoría de acción (ID 1 para 'Crear')
            if (loginBean.getCurrentUser() != null) {
                AuditoriaAccion auditoria = new AuditoriaAccion(loginBean.getCurrentUser());
                auditoria.setIdModulo(em.find(Modulo.class, "Usuarios")); // Módulo de Usuarios
                auditoria.setIdAccion(em.find(Accion.class, 1)); // Acción 'Crear' (ID 1)
                auditoria.setIdRegistroAfectado(newUser.getIdUsuario().toString());
                auditoria.setDetallesCambio("{\"accion\": \"Creación de nuevo usuario\", \"usuario_creado\": \"" + newUser.getCorreoElectronico() + "\", \"tipo_usuario\": \"" + tipoUsuario.getNombreTipo() + "\"}");
                em.persist(auditoria);
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario " + newUserName + " creado correctamente."));
            resetNewUserFields();
            loadUsuarios();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al crear usuario", "No se pudo crear el usuario: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void resetNewUserFields() {
        newUserName = null;
        newUserEmail = null;
        newUserPhone = null;
        newUserPassword = null;
        newUserConfirmPassword = null;
        newUserTipoUsuario = null;
        newUserRole = null;
        newUserModule = null;
        newUserStatus = 1; // Por defecto Activo
        newUserAddress = null;
    }

    @Transactional
    public void deleteUsuario(UUID idUsuario) {
        try {
            Usuario userToDelete = em.find(Usuario.class, idUsuario);
            if (userToDelete != null) {
                em.remove(userToDelete);
                
                // Registrar auditoría de acción (ID 3 para 'Eliminar')
                if (loginBean.getCurrentUser() != null) {
                    AuditoriaAccion auditoria = new AuditoriaAccion(loginBean.getCurrentUser());
                    auditoria.setIdModulo(em.find(Modulo.class, "Usuarios")); // Módulo de Usuarios
                    auditoria.setIdAccion(em.find(Accion.class, 3)); // Acción 'Eliminar' (ID 3)
                    auditoria.setIdRegistroAfectado(idUsuario.toString());
                    auditoria.setDetallesCambio("{\"accion\": \"Eliminación de usuario\", \"usuario_eliminado_email\": \"" + userToDelete.getCorreoElectronico() + "\"}");
                    em.persist(auditoria);
                }
                
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario eliminado correctamente."));
                loadUsuarios();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Usuario no encontrado."));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al eliminar", "No se pudo eliminar el usuario: " + e.getMessage()));
            e.printStackTrace();
        }
    }
    
    // Método para la lógica de alternar campos internos del formulario de nuevo usuario
    public void toggleInternalFields() {
        // La lógica de display CSS se manejará directamente en el XHTML con EL
        // Este método puede ser útil si se necesita realizar alguna lógica de backend
        // o inicialización de datos basada en el cambio del tipo de usuario.
        // Por ahora, solo es un placeholder si el f:ajax requiere un listener en el bean.
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Debug", "Tipo de usuario cambiado a: " + newUserTipoUsuario));
    }


    // --- Getters y Setters ---
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Usuario selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getNewUserName() {
        return newUserName;
    }

    public void setNewUserName(String newUserName) {
        this.newUserName = newUserName;
    }

    public String getNewUserEmail() {
        return newUserEmail;
    }

    public void setNewUserEmail(String newUserEmail) {
        this.newUserEmail = newUserEmail;
    }

    public String getNewUserPhone() {
        return newUserPhone;
    }

    public void setNewUserPhone(String newUserPhone) {
        this.newUserPhone = newUserPhone;
    }

    public String getNewUserPassword() {
        return newUserPassword;
    }

    public void setNewUserPassword(String newUserPassword) {
        this.newUserPassword = newUserPassword;
    }

    public String getNewUserConfirmPassword() {
        return newUserConfirmPassword;
    }

    public void setNewUserConfirmPassword(String newUserConfirmPassword) {
        this.newUserConfirmPassword = newUserConfirmPassword;
    }

    public Integer getNewUserTipoUsuario() {
        return newUserTipoUsuario;
    }

    public void setNewUserTipoUsuario(Integer newUserTipoUsuario) {
        this.newUserTipoUsuario = newUserTipoUsuario;
    }

    public Integer getNewUserRole() {
        return newUserRole;
    }

    public void setNewUserRole(Integer newUserRole) {
        this.newUserRole = newUserRole;
    }

    public String getNewUserModule() {
        return newUserModule;
    }

    public void setNewUserModule(String newUserModule) {
        this.newUserModule = newUserModule;
    }

    public Integer getNewUserStatus() {
        return newUserStatus;
    }

    public void setNewUserStatus(Integer newUserStatus) {
        this.newUserStatus = newUserStatus;
    }

    public String getNewUserAddress() {
        return newUserAddress;
    }

    public void setNewUserAddress(String newUserAddress) {
        this.newUserAddress = newUserAddress;
    }

    public List<TipoUsuario> getTiposUsuarios() {
        return tiposUsuarios;
    }

    public List<RolInterno> getRolesInternos() {
        return rolesInternos;
    }

    public List<EstadoCuenta> getEstadosCuenta() {
        return estadosCuenta;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    // Para convertir OffsetDateTime a String para inputs de fecha/hora
    // Se usa un formateador local de fecha-hora para compatibilidad con input type="datetime-local"
    public String getFormattedLastSession() {
        return selectedUser != null && selectedUser.getUltimaSesion() != null ?
               selectedUser.getUltimaSesion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }
    // No necesitamos un setter si el input es readonly

    public String getFormattedRegDate() {
        return selectedUser != null && selectedUser.getFechaCreacion() != null ?
               selectedUser.getFechaCreacion().format(DateTimeFormatter.ISO_LOCAL_DATE) : "";
    }
}