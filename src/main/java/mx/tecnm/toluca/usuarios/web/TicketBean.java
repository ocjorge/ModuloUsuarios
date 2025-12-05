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
import mx.tecnm.toluca.usuarios.service.TicketService;

@Named
@ViewScoped
public class TicketBean implements Serializable {

    @Inject private TicketService ticketService;
    @Inject private SessionManager sessionManager;

    private List<TicketRevision> tickets;

    // filtros
    private String moduloFiltro;
    private Integer estadoFiltro;
    private Integer tipoCambioFiltro;

    // catálogos
    private List<Modulo> modulos;
    private List<EstadoTicket> estadosTicket;
    private List<TipoCambio> tiposCambio;

    @PostConstruct
    public void init() {
        modulos = ticketService.listarModulos();
        estadosTicket = ticketService.listarEstadosTicket();
        tiposCambio = ticketService.listarTiposCambio();
        tickets = ticketService.listarTodos();
    }

    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    public void filtrar() {
        tickets = ticketService.listarFiltrados(moduloFiltro, estadoFiltro, tipoCambioFiltro);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Filtro aplicado", ""));
    }

    public void limpiarFiltros() {
        moduloFiltro = null;
        estadoFiltro = null;
        tipoCambioFiltro = null;
        tickets = ticketService.listarTodos();
    }

    public String verDetalle(UUID id) {
        return "ticket-detalle.xhtml?faces-redirect=true&id=" + id;
    }

    public void aprobar(UUID id) {
        ticketService.cambiarEstado(id, 2); // Aprobado
        tickets = ticketService.listarTodos();
    }

    public void rechazar(UUID id) {
        ticketService.cambiarEstado(id, 3); // Rechazado
        tickets = ticketService.listarTodos();
    }

    // getters/setters
    public List<TicketRevision> getTickets() { return tickets; }

    public String getModuloFiltro() { return moduloFiltro; }
    public void setModuloFiltro(String moduloFiltro) { this.moduloFiltro = moduloFiltro; }

    public Integer getEstadoFiltro() { return estadoFiltro; }
    public void setEstadoFiltro(Integer estadoFiltro) { this.estadoFiltro = estadoFiltro; }

    public Integer getTipoCambioFiltro() { return tipoCambioFiltro; }
    public void setTipoCambioFiltro(Integer tipoCambioFiltro) { this.tipoCambioFiltro = tipoCambioFiltro; }

    public List<Modulo> getModulos() { return modulos; }
    public List<EstadoTicket> getEstadosTicket() { return estadosTicket; }
    public List<TipoCambio> getTiposCambio() { return tiposCambio; }

    public SessionManager getSessionManager() { return sessionManager; }
}
