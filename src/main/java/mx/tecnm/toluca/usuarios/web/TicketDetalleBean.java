package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import mx.tecnm.toluca.usuarios.model.TicketRevision;
import mx.tecnm.toluca.usuarios.service.TicketService;

@Named
@ViewScoped
public class TicketDetalleBean implements Serializable {

    @Inject
    private TicketService ticketService;

    @Inject
    private SessionManager sessionManager;

    private String idParam;            // ID recibido por URL
    private TicketRevision ticket;     // Ticket cargado
    private String jsonPretty;         // JSON embellecido

    @PostConstruct
    public void init() {
        jsonPretty = "{}";
        ticket = null;
    }

    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    public void cargar() {
        try {
            if (idParam == null || idParam.isBlank()) {
                jsonPretty = "{ \"error\": \"ID vacío\" }";
                return;
            }

            UUID id = UUID.fromString(idParam);
            ticket = ticketService.buscarPorId(id);

            if (ticket != null) {
                jsonPretty = formatJson(ticket.getDatosPropuestos());
            } else {
                jsonPretty = "{ \"error\": \"Ticket no encontrado\" }";
            }

        } catch (Exception ex) {
            jsonPretty = "{ \"error\": \"No se pudo cargar el ticket\" }";
            ticket = null;
        }
    }

    // ================================
    // FORMATEADOR ROBUSTO DE JSON
    // ================================
    private String formatJson(Object jsonRaw) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            if (jsonRaw == null) {
                return "{ \"error\": \"JSON vacío\" }";
            }

            // Si el JSON viene como String desde PostgreSQL
            if (jsonRaw instanceof String rawString) {
                Object jsonObj = mapper.readValue(rawString, Object.class);
                return mapper.writeValueAsString(jsonObj);
            }

            // Si viene como JSONObject desde org.json
            if (jsonRaw instanceof org.json.JSONObject jsonObj) {
                Object converted = mapper.readValue(jsonObj.toString(), Object.class);
                return mapper.writeValueAsString(converted);
            }

            // Si viene como Map o LinkedHashMap (muy común con JSONB)
            return mapper.writeValueAsString(jsonRaw);

        } catch (Exception e) {
            return "{ \"error\": \"JSON inválido\" }";
        }
    }

    // ================================
    // GETTERS Y SETTERS
    // ================================

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
