package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import mx.tecnm.toluca.usuarios.model.TicketRevision;
import mx.tecnm.toluca.usuarios.service.TicketService;

@Named
@ViewScoped
public class TicketBean implements Serializable {

    @Inject
    private TicketService ticketService;

    private List<TicketRevision> tickets;

    private String moduloFiltro;
    private Integer estadoFiltro;
    private Integer tipoCambioFiltro;

    @PostConstruct
    public void init() {
        try {
            cargarTickets();
        } catch (Exception ex) {
            tickets = Collections.emptyList();
            System.err.println("ERROR en TicketBean.init(): " + ex.getMessage());
        }
    }

    public void cargarTickets() {
        tickets = ticketService.listarTickets(moduloFiltro, estadoFiltro, tipoCambioFiltro);
    }

    public List<TicketRevision> getTickets() { return tickets; }

    public String getModuloFiltro() { return moduloFiltro; }
    public void setModuloFiltro(String moduloFiltro) { this.moduloFiltro = moduloFiltro; }

    public Integer getEstadoFiltro() { return estadoFiltro; }
    public void setEstadoFiltro(Integer estadoFiltro) { this.estadoFiltro = estadoFiltro; }

    public Integer getTipoCambioFiltro() { return tipoCambioFiltro; }
    public void setTipoCambioFiltro(Integer tipoCambioFiltro) { this.tipoCambioFiltro = tipoCambioFiltro; }
}
