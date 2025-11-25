package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import mx.tecnm.toluca.usuarios.model.TicketRevision;
import mx.tecnm.toluca.usuarios.service.TicketService;

@Named
@ViewScoped
public class TicketDetalleBean implements Serializable {

    @Inject
    private TicketService ticketService;

    @Inject
    private SessionManager sessionManager;

    private String idParam;                 // Recibido por URL
    private TicketRevision ticket;          // Ticket cargado

    // JSON embellecido
    private String jsonPretty;

    @PostConstruct
    public void init() {
        // Evita errores si la vista se carga primero
        ticket = null;
        jsonPretty = "{}";
    }

    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    public void cargar() {
        try {
            if (idParam == null || idParam.isBlank())
                return;

            UUID id = UUID.fromString(idParam);
            ticket = ticketService.buscarPorId(id);

            if (ticket != null) {
                jsonPretty = formatJson(ticket.getDatosPropuestos());
            }

        } catch (Exception e) {
            ticket = null; // evita errores en la vista
        }
    }

    private String formatJson(String raw) {
        try {
            // Usamos la librería interna de Java
            var json = new org.json.JSONObject(raw);
            return json.toString(4); // pretty print
        } catch (Exception e) {
            return raw; // si no es JSON válido, se muestra crudo
        }
    }

    // GETTERS y SETTERS
    public String getIdParam() {
        return idParam;
    }

    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    public TicketRevision getTicket() {
        return ticket;
    }

    public String getJsonPretty() {
        return jsonPretty;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
