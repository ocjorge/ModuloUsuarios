package mx.tecnm.toluca.usuarios.web;

// Importación para ejecutar lógica después de crear el bean
import jakarta.annotation.PostConstruct;

// Importaciones para manejo de mensajes JSF
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// Alcance del bean a nivel de vista
import jakarta.faces.view.ViewScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para E/S, serialización y UUID
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

// Importaciones del modelo
import mx.tecnm.toluca.usuarios.model.*;

// Importación del servicio de tickets
import mx.tecnm.toluca.usuarios.service.TicketService;

/**
 * Bean de respaldo para la gestión de tickets de revisión.
 * Maneja la carga de tickets, filtros, acciones de aprobación
 * y rechazo, así como la navegación a detalle.
 */
@Named
@ViewScoped
public class TicketBean implements Serializable {

    /**
     * Servicio de tickets.
     * Proporciona acceso a la lógica de negocio relacionada con tickets.
     */
    @Inject 
    private TicketService ticketService;

    /**
     * Administrador de sesión.
     * Se utiliza para validar la autenticación del usuario.
     */
    @Inject 
    private SessionManager sessionManager;

    /**
     * Lista de tickets mostrados en la vista.
     */
    private List<TicketRevision> tickets;

    // ---------------- FILTROS ----------------

    /**
     * Filtro por módulo.
     */
    private String moduloFiltro;

    /**
     * Filtro por estado del ticket.
     */
    private Integer estadoFiltro;

    /**
     * Filtro por tipo de cambio.
     */
    private Integer tipoCambioFiltro;

    // ---------------- CATÁLOGOS ----------------

    /**
     * Catálogo de módulos disponibles.
     */
    private List<Modulo> modulos;

    /**
     * Catálogo de estados de ticket.
     */
    private List<EstadoTicket> estadosTicket;

    /**
     * Catálogo de tipos de cambio.
     */
    private List<TipoCambio> tiposCambio;

    /**
     * Método ejecutado automáticamente después de crear el bean.
     * Inicializa catálogos y carga la lista completa de tickets.
     */
    @PostConstruct
    public void init() {

        // Carga catálogos para filtros
        modulos = ticketService.listarModulos();
        estadosTicket = ticketService.listarEstadosTicket();
        tiposCambio = ticketService.listarTiposCambio();

        // Carga inicial de tickets
        tickets = ticketService.listarTodos();
    }

    /**
     * Verifica si existe una sesión activa.
     * Si no está autenticado, redirige al login.
     *
     * @throws IOException en caso de error de redirección
     */
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    /**
     * Aplica los filtros seleccionados y actualiza la lista de tickets.
     * Muestra un mensaje informativo en la vista.
     */
    public void filtrar() {

        // Aplica filtros usando el servicio
        tickets = ticketService.listarFiltrados(
                moduloFiltro, 
                estadoFiltro, 
                tipoCambioFiltro
        );

        // Mensaje de confirmación
        FacesContext.getCurrentInstance().addMessage(
            null,
            new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Filtro aplicado",
                ""
            )
        );
    }

    /**
     * Limpia todos los filtros y vuelve a cargar
     * la lista completa de tickets.
     */
    public void limpiarFiltros() {

        moduloFiltro = null;
        estadoFiltro = null;
        tipoCambioFiltro = null;

        tickets = ticketService.listarTodos();
    }

    /**
     * Navega a la vista de detalle de un ticket específico.
     *
     * @param id identificador del ticket
     * @return URL de navegación con redirección
     */
    public String verDetalle(UUID id) {
        return "ticket-detalle.xhtml?faces-redirect=true&id=" + id;
    }

    /**
     * Aprueba un ticket.
     * Cambia su estado a "Aprobado" (id = 2) y recarga la lista.
     *
     * @param id identificador del ticket
     */
    public void aprobar(UUID id) {

        // Estado 2 = Aprobado
        ticketService.cambiarEstado(id, 2);

        // Recarga lista de tickets
        tickets = ticketService.listarTodos();
    }

    /**
     * Rechaza un ticket.
     * Cambia su estado a "Rechazado" (id = 3) y recarga la lista.
     *
     * @param id identificador del ticket
     */
    public void rechazar(UUID id) {

        // Estado 3 = Rechazado
        ticketService.cambiarEstado(id, 3);

        // Recarga lista de tickets
        tickets = ticketService.listarTodos();
    }

    // ---------------- GETTERS / SETTERS ----------------

    /**
     * @return lista de tickets
     */
    public List<TicketRevision> getTickets() { 
        return tickets; 
    }

    public String getModuloFiltro() { 
        return moduloFiltro; 
    }

    public void setModuloFiltro(String moduloFiltro) { 
        this.moduloFiltro = moduloFiltro; 
    }

    public Integer getEstadoFiltro() { 
        return estadoFiltro; 
    }

    public void setEstadoFiltro(Integer estadoFiltro) { 
        this.estadoFiltro = estadoFiltro; 
    }

    public Integer getTipoCambioFiltro() { 
        return tipoCambioFiltro; 
    }

    public void setTipoCambioFiltro(Integer tipoCambioFiltro) { 
        this.tipoCambioFiltro = tipoCambioFiltro; 
    }

    public List<Modulo> getModulos() { 
        return modulos; 
    }

    public List<EstadoTicket> getEstadosTicket() { 
        return estadosTicket; 
    }

    public List<TipoCambio> getTiposCambio() { 
        return tiposCambio; 
    }

    /**
     * @return SessionManager actual
     */
    public SessionManager getSessionManager() { 
        return sessionManager; 
    }
}
