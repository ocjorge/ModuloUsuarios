package mx.tecnm.toluca.usuarios.control;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.time.OffsetDateTime;
import org.mindrot.jbcrypt.BCrypt; // Para hashing de contraseñas
import mx.tecnm.toluca.usuarios.modelo.AuditoriaAcceso;
import mx.tecnm.toluca.usuarios.modelo.EstadoCuenta;
import mx.tecnm.toluca.usuarios.modelo.Modulo;
import mx.tecnm.toluca.usuarios.modelo.RolInterno;
import mx.tecnm.toluca.usuarios.modelo.TipoEvento;
import mx.tecnm.toluca.usuarios.modelo.TipoUsuario;
import mx.tecnm.toluca.usuarios.modelo.Usuario;

@Named("loginBean") // Nombre explícito para usar en JSF
@SessionScoped
public class LoginBean implements Serializable {

    @Inject
    private EntityManager em;

    private String username;
    private String password;
    private Usuario currentUser;

    // Campos para el registro
    private String registerEmail;
    private String registerName;
    private String registerPhone;
    private String registerPassword;
    private String registerConfirmPassword;
    private Integer newUserTipoUsuario;
    private Integer newUserRole; // ID del rol
    private String newUserModule; // ID del módulo
    private Integer newUserStatus = 1; // Por defecto Activo (ID 1)

    @Transactional
    public String login() {
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.correoElectronico = :email", Usuario.class);
            query.setParameter("email", username);
            Usuario user = query.getSingleResult();

            if (BCrypt.checkpw(password, user.getContrasena())) {
                if (user.getIdEstadoCuenta().getNombreEstado().equals("Activo")) {
                    currentUser = user;
                    user.setUltimaSesion(OffsetDateTime.now());
                    em.merge(user); // Actualizar última sesión

                    // Registrar auditoría de acceso exitoso (ID 1 para 'Login')
                    TipoEvento loginExitoso = em.find(TipoEvento.class, 1);
                    AuditoriaAcceso auditoria = new AuditoriaAcceso(user, loginExitoso);
                    em.persist(auditoria);

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Inicio de sesión exitoso.", "Bienvenido, " + currentUser.getNombreCompleto()));
                    return "/index.xhtml?faces-redirect=true"; // Redirigir a la página principal
                } else {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_WARN, "Cuenta " + user.getIdEstadoCuenta().getNombreEstado() + ".", "Contacte al administrador."));

                    // Registrar auditoría de acceso denegado (estado inactivo/bloqueado) (ID 3 para 'Acceso Denegado')
                    TipoEvento accesoDenegado = em.find(TipoEvento.class, 3);
                    AuditoriaAcceso auditoria = new AuditoriaAcceso(user, accesoDenegado);
                    em.persist(auditoria);
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas.", "Verifique su usuario y contraseña."));

                // Si se encuentra el usuario pero la contraseña es incorrecta, registrar intento fallido (ID 3 para 'Acceso Denegado')
                TypedQuery<Usuario> findUserQuery = em.createQuery(
                        "SELECT u FROM Usuario u WHERE u.correoElectronico = :email", Usuario.class);
                findUserQuery.setParameter("email", username);
                try {
                    Usuario existingUser = findUserQuery.getSingleResult();
                    TipoEvento accesoDenegado = em.find(TipoEvento.class, 3);
                    AuditoriaAcceso auditoria = new AuditoriaAcceso(existingUser, accesoDenegado);
                    em.persist(auditoria);
                } catch (NoResultException ex) {
                    // Usuario no encontrado, no se registra auditoría para evitar enumeración de usuarios
                }
            }
        } catch (NoResultException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales incorrectas.", "Verifique su usuario y contraseña."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error en el sistema.", "Ocurrió un error inesperado."));
            e.printStackTrace();
        }
        return null; // Mantenerse en la misma página
    }

    @Transactional
    public void register() { // Cambiado a void para f:ajax
        if (!registerPassword.equals(registerConfirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro.", "Las contraseñas no coinciden."));
            return;
        }

        try {
            // Verificar si el correo ya existe
            TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.correoElectronico = :email", Long.class);
            countQuery.setParameter("email", registerEmail);
            if (countQuery.getSingleResult() > 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de registro.", "El correo electrónico ya está en uso."));
                return;
            }


            Usuario newUser = new Usuario();
            newUser.setCorreoElectronico(registerEmail);
            newUser.setNombreCompleto(registerName);
            newUser.setTelefono(registerPhone);
            newUser.setContrasena(BCrypt.hashpw(registerPassword, BCrypt.gensalt()));

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
            }
            // La dirección no se pide en el formulario de registro inicial, se deja null
            newUser.setFechaCreacion(OffsetDateTime.now());

            em.persist(newUser);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro exitoso.", "¡Ya puedes iniciar sesión!"));
            // Limpiar campos del formulario después del registro exitoso
            resetRegisterFields();
            // No redirige, el JSF se encarga de renderizar la página
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al registrar.", "No se pudo completar el registro: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    private void resetRegisterFields() {
        registerEmail = null;
        registerName = null;
        registerPhone = null;
        registerPassword = null;
        registerConfirmPassword = null;
        newUserTipoUsuario = null;
        newUserRole = null;
        newUserModule = null;
        newUserStatus = 1;
    }

    @Transactional
    public String logout() {
        // Registrar auditoría de logout (ID 2 para 'Logout')
        if (currentUser != null) {
            try {
                TipoEvento logoutEvento = em.find(TipoEvento.class, 2);
                AuditoriaAcceso auditoria = new AuditoriaAcceso(currentUser, logoutEvento);
                em.persist(auditoria);
            } catch (Exception e) {
                e.printStackTrace(); // Log del error, pero no impedir el logout
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        currentUser = null;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sesión cerrada.", "Hasta pronto."));
        return "/index.xhtml?faces-redirect=true"; // Redirigir al login
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // --- Getters y Setters ---
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario currentUser) {
        this.currentUser = currentUser;
    }

    public String getRegisterEmail() {
        return registerEmail;
    }

    public void setRegisterEmail(String registerEmail) {
        this.registerEmail = registerEmail;
    }

    public String getRegisterName() {
        return registerName;
    }

    public void setRegisterName(String registerName) {
        this.registerName = registerName;
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone;
    }

    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }

    public String getRegisterConfirmPassword() {
        return registerConfirmPassword;
    }

    public void setRegisterConfirmPassword(String registerConfirmPassword) {
        this.registerConfirmPassword = registerConfirmPassword;
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
}
