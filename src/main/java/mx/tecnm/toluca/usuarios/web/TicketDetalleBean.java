package mx.tecnm.toluca.usuarios.web;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.UUID;

import mx.tecnm.toluca.usuarios.model.TicketRevision;
import mx.tecnm.toluca.usuarios.service.TicketService;

@Named
@ViewScoped
public class TicketDetalleBean implements Serializable {

    @Inject
    private TicketService ticketService;

    private String idParam;
    private TicketRevision ticket;

    public void cargar() {
        if (idParam != null) {
            ticket = ticketService.buscarPorId(UUID.fromString(idParam));
        }
    }

    public TicketRevision getTicket() { return ticket; }

    public String getIdParam() { return idParam; }
    public void setIdParam(String idParam) { this.idParam = idParam; }
}
