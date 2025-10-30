package mx.tecnm.toluca.usuarios;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap; // Necesario para Map
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Named("userManagerBean")
@ViewScoped
public class UserManagerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginUsername;
    private String loginPassword;
    private String currentUserName;
    private String filterUsersInput; // Para el input de filtro de usuarios
    private String filterTicketsInput; // Para el input de filtro de tickets


    private String registerEmail;
    private String registerName;
    private String registerPhone;
    private String registerPassword;
    private String registerConfirmPassword;

    private User selectedUser;
    private String newUserName;
    private String newUserEmail;
    private String newUserPhone;
    private String newUserModule;
    private String newUserRole;
    private String newUserStatus = "Activo";

    private List<Ticket> tickets;
    private List<Ticket> filteredTickets;
    private Ticket selectedTicket;
    private String ticketRejectionReason;

    private List<User> users;
    private List<User> filteredUsers;
    private int nextUserId = 11;
    private int nextTicketId = 6;

    private Map<String, String> validCredentials;

    @PostConstruct
    public void init() {
        users = new ArrayList<>();
        users.add(new User(1, "María", "user@gmail.com", "ERP", "9900258789", "Activo", "Gerente", "2024-01-15", "2025-10-15T10:30"));
        users.add(new User(2, "Emanuel", "usuario@gmail.com.mx", "Bancos", "9911165670", "Inactivo", "Jefe", "2024-02-20", "2025-10-10T14:20"));
        users.add(new User(3, "José", "jt615257@gmail.com", "Proveedores", "9981298737", "Activo", "Operador", "2024-03-10", "2025-10-16T09:15"));
        users.add(new User(4, "Shaggy", "shaggy@buu.net", "CRM", "54948151", "Inactivo", "Gerente", "2024-04-05", "2025-09-30T16:45"));
        users.add(new User(5, "Scrapy", "sam@gmail.com", "Usuarios", "9975201478", "Activo", "Jefe", "2024-05-12", "2025-10-16T08:00"));
        users.add(new User(6, "Ana", "ana@empresa.com", "ERP", "5587412369", "Activo", "Operador", "2024-06-18", "2025-10-14T11:30"));
        users.add(new User(7, "Carlos", "carlos@banco.com", "Bancos", "5578963214", "Inactivo", "Jefe", "2024-07-22", "2025-10-01T15:00"));
        users.add(new User(8, "Lucía", "lucia@proveedor.com", "Proveedores", "5563214789", "Activo", "Gerente", "2024-08-30", "2025-10-15T13:45"));
        users.add(new User(9, "Diego", "diego@crm.com", "CRM", "5512345678", "Inactivo", "Operador", "2024-09-10", "2025-09-28T10:20"));
        users.add(new User(10, "Fernanda", "fer@usuarios.com", "Usuarios", "5598765432", "Activo", "Jefe", "2024-10-01", "2025-10-16T07:30"));
        filteredUsers = new ArrayList<>(users);

        tickets = new ArrayList<>();
        tickets.add(new Ticket("TK-001", "Asignación Rol", "ERP Central", "2025-10-22", "Pendiente", "María (user@gmail.com)", Map.of("email", "user@gmail.com", "rol_propuesto", "Jefe", "justificacion", "Promoción por desempeño")));
        tickets.add(new Ticket("TK-002", "Edición Usuario", "Portal de Proveedores", "2025-10-23", "En Revisión", "Lucía (lucia@proveedor.com)", Map.of("email", "lucia@proveedor.com", "telefono", "5598765999", "cambio", "Actualización de contacto")));
        tickets.add(new Ticket("TK-003", "Creación Usuario", "Gestión de Clientes", "2025-10-19", "Aprobado", "Gerente CRM", Map.of("email", "nuevo.operador@tienda.com", "cambio", "Creación de nuevo usuario")));
        tickets.add(new Ticket("TK-004", "Cambio Estado", "ERP Central", "2025-10-14", "Rechazado", "Ana (ana@empresa.com)", Map.of("email", "usuario.inactivo@tienda.com", "estado_propuesto", "Activo", "motivo_rechazo", "Documentación incompleta")));
        tickets.add(new Ticket("TK-005", "Edición Usuario", "Gestión de Clientes", "2025-10-24", "Pendiente", "Cliente 2", Map.of("email", "usuario@gmail.com.mx", "telefono", "5511223344", "cambio", "Actualización de número de cliente")));
        filteredTickets = new ArrayList<>(tickets);

        validCredentials = Map.of(
            "admin", "admin123",
            "usuario", "password",
            "KC", "12345"
        );
    }

    public String handleLogin() {
        if (validCredentials.containsKey(loginUsername) && validCredentials.get(loginUsername).equals(loginPassword)) {
            currentUserName = loginUsername;
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "¡Inicio de sesión exitoso!");
            return "mainPage.xhtml?faces-redirect=true";
        } else {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Credenciales incorrectas. Intente nuevamente.");
            return null;
        }
    }

    public String handleRegister() {
        if (!registerPassword.equals(registerConfirmPassword)) {
            addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Las contraseñas no coinciden.");
            return null;
        }
        if (!registerPhone.matches("\\d{10}")) {
            addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "El teléfono debe tener 10 dígitos.");
            return null;
        }

        User newUser = new User(nextUserId++, registerName, registerEmail, "", registerPhone, "Activo", "Operador",
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0, 16));
        users.add(newUser);
        filteredUsers.add(newUser);

        addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "¡Registro exitoso! Ya puede iniciar sesión.");
        resetRegisterForm();
        return "loginPage.xhtml?faces-redirect=true";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        addMessage(FacesMessage.SEVERITY_INFO, "Info", "Sesión cerrada correctamente.");
        return "loginPage.xhtml?faces-redirect=true";
    }

    public void editUser(User user) {
        this.selectedUser = user;
        addMessage(FacesMessage.SEVERITY_INFO, "Info", "Preparando edición para: " + user.getName());
    }

    public String handleEditUser() {
        if (selectedUser != null) {
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario " + selectedUser.getName() + " actualizado exitosamente.");
            selectedUser = null;
            return "mainPage.xhtml?faces-redirect=true";
        }
        return null;
    }

    public String handleNewUser() {
        User newUser = new User(nextUserId++, newUserName, newUserEmail, newUserModule, newUserPhone, newUserStatus, newUserRole,
                LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).substring(0, 16));
        users.add(newUser);
        filteredUsers.add(newUser);
        addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario " + newUserName + " creado exitosamente.");
        resetNewUserForm();
        return "mainPage.xhtml?faces-redirect=true";
    }

    public void deleteUser(User user) {
        users.remove(user);
        filteredUsers.remove(user);
        addMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuario " + user.getName() + " eliminado.");
    }
    
    public void filterUsers() {
        if (filterUsersInput != null && !filterUsersInput.trim().isEmpty()) {
            String lowerFilter = filterUsersInput.toLowerCase().trim();
            filteredUsers = users.stream()
                                 .filter(user -> user.getName().toLowerCase().contains(lowerFilter) ||
                                                  user.getEmail().toLowerCase().contains(lowerFilter))
                                 .collect(Collectors.toList());
            if (filteredUsers.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron usuarios con el criterio '" + filterUsersInput + "'.");
            } else {
                addMessage(FacesMessage.SEVERITY_INFO, "Info", "Filtrando usuarios por: " + filterUsersInput);
            }
        } else {
            filteredUsers = new ArrayList<>(users);
            addMessage(FacesMessage.SEVERITY_INFO, "Info", "Mostrando todos los usuarios.");
        }
    }

    public void selectTicketForReview(Ticket ticket) {
        this.selectedTicket = ticket;
        this.ticketRejectionReason = null;
    }

    public void approveTicket() {
        if (selectedTicket != null) {
            User userToUpdate = users.stream()
                                     .filter(u -> u.getEmail().equals(selectedTicket.getDetalles().get("email")))
                                     .findFirst()
                                     .orElse(null);

            if (userToUpdate != null) {
                if (selectedTicket.getDetalles().containsKey("estado_propuesto")) {
                    userToUpdate.setStatus(selectedTicket.getDetalles().get("estado_propuesto"));
                }
                if (selectedTicket.getDetalles().containsKey("rol_propuesto")) {
                    userToUpdate.setRole(selectedTicket.getDetalles().get("rol_propuesto"));
                }
                if (selectedTicket.getDetalles().containsKey("telefono")) {
                    userToUpdate.setPhone(selectedTicket.getDetalles().get("telefono"));
                }
                if (selectedTicket.getDetalles().containsKey("nombre_propuesto")) {
                    userToUpdate.setName(selectedTicket.getDetalles().get("nombre_propuesto"));
                }
                addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cambios aplicados al usuario " + userToUpdate.getName());
            } else {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Usuario no encontrado para aplicar cambios del ticket.");
            }

            selectedTicket.setEstado("Aprobado");
            selectedTicket.getDetalles().put("aprobado_por", currentUserName);
            addMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Ticket " + selectedTicket.getId() + " APROBADO.");
            selectedTicket = null;
            filterTickets(null);
        }
    }

    public void rejectTicket() {
        if (selectedTicket != null) {
            if (ticketRejectionReason != null && !ticketRejectionReason.trim().isEmpty()) {
                selectedTicket.setEstado("Rechazado");
                selectedTicket.getDetalles().put("motivo_rechazo", ticketRejectionReason);
                selectedTicket.getDetalles().put("rechazado_por", currentUserName);
                addMessage(FacesMessage.SEVERITY_ERROR, "Rechazado", "Ticket " + selectedTicket.getId() + " RECHAZADO. Motivo: " + ticketRejectionReason);
                selectedTicket = null;
                ticketRejectionReason = null;
                filterTickets(null);
            } else {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Debe ingresar un motivo para rechazar el ticket.");
            }
        }
    }
    
    public String navigateToEditUserFromTicket() {
        if (selectedTicket != null && selectedTicket.getDetalles().containsKey("email")) {
            User userToEdit = users.stream()
                                 .filter(u -> u.getEmail().equals(selectedTicket.getDetalles().get("email")))
                                 .findFirst()
                                 .orElse(null);
            if (userToEdit != null) {
                this.selectedUser = userToEdit;
                
                // Inicializar el mapa de resaltado
                selectedUser.setHighlightedFields(new HashMap<>());

                if (selectedTicket.getDetalles().containsKey("rol_propuesto")) {
                    userToEdit.setRole(selectedTicket.getDetalles().get("rol_propuesto"));
                    selectedUser.getHighlightedFields().put("role", true);
                }
                if (selectedTicket.getDetalles().containsKey("telefono")) {
                    userToEdit.setPhone(selectedTicket.getDetalles().get("telefono"));
                    selectedUser.getHighlightedFields().put("phone", true);
                }
                if (selectedTicket.getDetalles().containsKey("estado_propuesto")) {
                    userToEdit.setStatus(selectedTicket.getDetalles().get("estado_propuesto"));
                    selectedUser.getHighlightedFields().put("status", true);
                }
                if (selectedTicket.getDetalles().containsKey("nombre_propuesto")) {
                    userToEdit.setName(selectedTicket.getDetalles().get("nombre_propuesto"));
                    selectedUser.getHighlightedFields().put("name", true);
                }

                addMessage(FacesMessage.SEVERITY_INFO, "Info", "Revise los campos resaltados y presione 'ACTUALIZAR USUARIO' para aplicar el cambio del ticket: **" + selectedTicket.getId() + "**");
                selectedTicket.setEstado("En Revisión");
                filterTickets(null);
                return "detailsPage.xhtml?faces-redirect=true";
            } else {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario no encontrado para el ticket seleccionado.");
                return null;
            }
        }
        return null;
    }

    public void filterTickets(String filterValue) {
        if (filterValue == null || filterValue.trim().isEmpty() || "Todos".equalsIgnoreCase(filterValue)) {
            filteredTickets = new ArrayList<>(tickets);
            addMessage(FacesMessage.SEVERITY_INFO, "Info", "Mostrando todos los tickets.");
        } else {
            String lowerFilter = filterValue.toLowerCase().trim();
            filteredTickets = tickets.stream()
                                     .filter(ticket -> ticket.getId().toLowerCase().contains(lowerFilter) ||
                                                      ticket.getModulo().toLowerCase().contains(lowerFilter) ||
                                                      ticket.getUsuarioSolicitante().toLowerCase().contains(lowerFilter) ||
                                                      ticket.getTipo().toLowerCase().contains(lowerFilter) ||
                                                      ticket.getEstado().toLowerCase().replace(" ", "").equals(lowerFilter.replace(" ", "")))
                                     .collect(Collectors.toList());
            if (filteredTickets.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron tickets con el criterio '" + filterValue + "'.");
            } else {
                addMessage(FacesMessage.SEVERITY_INFO, "Info", "Filtrando tickets por: " + filterValue);
            }
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    private void resetRegisterForm() {
        registerEmail = null;
        registerName = null;
        registerPhone = null;
        registerPassword = null;
        registerConfirmPassword = null;
    }

    private void resetNewUserForm() {
        newUserName = null;
        newUserEmail = null;
        newUserPhone = null;
        newUserModule = null;
        newUserRole = null;
        newUserStatus = "Activo";
    }
    
    // Clase interna User para simular el modelo de datos
    public static class User implements Serializable {
        private int id;
        private String name;
        private String email;
        private String module;
        private String phone;
        private String status;
        private String role;
        private String regDate;
        private String lastSession;
        private Map<String, Boolean> highlightedFields; // Nuevo para el resaltado

        public User() {}

        public User(int id, String name, String email, String module, String phone, String status, String role, String regDate, String lastSession) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.module = module;
            this.phone = phone;
            this.status = status;
            this.role = role;
            this.regDate = regDate;
            this.lastSession = lastSession;
            this.highlightedFields = new HashMap<>(); // Inicializar
        }

        // Getters y Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getModule() { return module; }
        public void setModule(String module) { this.module = module; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getRegDate() { return regDate; }
        public void setRegDate(String regDate) { this.regDate = regDate; }
        public String getLastSession() { return lastSession; }
        public void setLastSession(String lastSession) { this.lastSession = lastSession; }

        public Map<String, Boolean> getHighlightedFields() {
            if (highlightedFields == null) {
                highlightedFields = new HashMap<>();
            }
            return highlightedFields;
        }

        public void setHighlightedFields(Map<String, Boolean> highlightedFields) {
            this.highlightedFields = highlightedFields;
        }
    }

    // Clase interna Ticket para simular el modelo de datos
    public static class Ticket implements Serializable {
        private String id;
        private String tipo;
        private String modulo;
        private String fecha;
        private String estado;
        private String usuarioSolicitante;
        private Map<String, String> detalles;

        public Ticket() {}

        public Ticket(String id, String tipo, String modulo, String fecha, String estado, String usuarioSolicitante, Map<String, String> detalles) {
            this.id = id;
            this.tipo = tipo;
            this.modulo = modulo;
            this.fecha = fecha;
            this.estado = estado;
            this.usuarioSolicitante = usuarioSolicitante;
            this.detalles = detalles;
        }

        // Getters y Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getModulo() { return modulo; }
        public void setModulo(String modulo) { this.modulo = modulo; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getUsuarioSolicitante() { return usuarioSolicitante; }
        public void setUsuarioSolicitante(String usuarioSolicitante) { this.usuarioSolicitante = usuarioSolicitante; }
        public Map<String, String> getDetalles() { return detalles; }
        public void setDetalles(Map<String, String> detalles) { this.detalles = detalles; }
    }

    // --- Getters y Setters para las propiedades del bean ---
    public String getLoginUsername() { return loginUsername; }
    public void setLoginUsername(String loginUsername) { this.loginUsername = loginUsername; }
    public String getLoginPassword() { return loginPassword; }
    public void setLoginPassword(String loginPassword) { this.loginPassword = loginPassword; }
    public String getCurrentUserName() { return currentUserName; }
    public void setCurrentUserName(String currentUserName) { this.currentUserName = currentUserName; }

    public String getRegisterEmail() { return registerEmail; }
    public void setRegisterEmail(String registerEmail) { this.registerEmail = registerEmail; }
    public String getRegisterName() { return registerName; }
    public void setRegisterName(String registerName) { this.registerName = registerName; }
    public String getRegisterPhone() { return registerPhone; }
    public void setRegisterPhone(String registerPhone) { this.registerPhone = registerPhone; }
    public String getRegisterPassword() { return registerPassword; }
    public void setRegisterPassword(String registerPassword) { this.registerPassword = registerPassword; }
    public String getRegisterConfirmPassword() { return registerConfirmPassword; }
    public void setRegisterConfirmPassword(String registerConfirmPassword) { this.registerConfirmPassword = registerConfirmPassword; }

    public List<User> getUsers() { return users; }
    public List<User> getFilteredUsers() { return filteredUsers; }
    public void setFilteredUsers(List<User> filteredUsers) { this.filteredUsers = filteredUsers; }

    public User getSelectedUser() { return selectedUser; }
    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }

    public String getNewUserName() { return newUserName; }
    public void setNewUserName(String newUserName) { this.newUserName = newUserName; }
    public String getNewUserEmail() { return newUserEmail; }
    public void setNewUserEmail(String newUserEmail) { this.newUserEmail = newUserEmail; }
    public String getNewUserPhone() { return newUserPhone; }
    public void setNewUserPhone(String newUserPhone) { this.newUserPhone = newUserPhone; }
    public String getNewUserModule() { return newUserModule; }
    public void setNewUserModule(String newUserModule) { this.newUserModule = newUserModule; }
    public String getNewUserRole() { return newUserRole; }
    public void setNewUserRole(String newUserRole) { this.newUserRole = newUserRole; }
    public String getNewUserStatus() { return newUserStatus; }
    public void setNewUserStatus(String newUserStatus) { this.newUserStatus = newUserStatus; }

    public List<Ticket> getTickets() { return tickets; }
    public List<Ticket> getFilteredTickets() { return filteredTickets; }
    public void setFilteredTickets(List<Ticket> filteredTickets) { this.filteredTickets = filteredTickets; }
    
    public Ticket getSelectedTicket() { return selectedTicket; }
    public void setSelectedTicket(Ticket selectedTicket) { this.selectedTicket = selectedTicket; }
    public String getTicketRejectionReason() { return ticketRejectionReason; }
    public void setTicketRejectionReason(String ticketRejectionReason) { this.ticketRejectionReason = ticketRejectionReason; }

    public String getFilterUsersInput() { return filterUsersInput; }
    public void setFilterUsersInput(String filterUsersInput) { this.filterUsersInput = filterUsersInput; }

    public String getFilterTicketsInput() { return filterTicketsInput; }
    public void setFilterTicketsInput(String filterTicketsInput) { this.filterTicketsInput = filterTicketsInput; }
}