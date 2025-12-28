package mx.tecnm.toluca.usuarios.web;

// Importación para ejecutar lógica después de la construcción del bean
import jakarta.annotation.PostConstruct;

// Alcance del bean a nivel de vista
import jakarta.faces.view.ViewScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para E/S y serialización
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

// Importaciones de Jackson para manejo y formateo de JSON
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

// Importaciones del modelo y servicio
import mx.tecnm.toluca.usuarios.model.TicketRevision;
import mx.tecnm.toluca.usuarios.service.TicketService;

/**
 * Bean de respaldo para la vista de detalle de un ticket.
 * Se encarga de cargar el ticket por ID recibido en la URL
 * y de mostrar los datos propuestos en formato JSON legible.
 */
@Named
@ViewScoped
public class TicketDetalleBean implements Serializable {

    /**
     * Servicio de tickets.
     * Se utiliza para buscar el ticket en la base de datos.
     */
    @Inject
    private TicketService ticketService;

    /**
     * Administrador de sesión.
     * Se utiliza para verificar que el usuario esté autenticado.
     */
    @Inject
    private SessionManager sessionManager;

    /**
     * Identificador del ticket recibido por parámetro en la URL.
     */
    private String idParam;            // ID recibido por URL

    /**
     * Ticket cargado desde la base de datos.
     */
    private TicketRevision ticket;     // Ticket cargado

    /**
     * Representación JSON embellecida de los datos propuestos.
     */
    private String jsonPretty;         // JSON embellecido

    /**
     * Método ejecutado automáticamente después de crear el bean.
     * Inicializa los valores por defecto.
     */
    @PostConstruct
    public void init() {

        // Valor por defecto del JSON
        jsonPretty = "{}";

        // No hay ticket cargado inicialmente
        ticket = null;
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
     * Carga el ticket usando el ID recibido por la URL.
     * Convierte y formatea los datos propuestos a JSON legible.
     */
    public void cargar() {

        try {
            // Valida que el parámetro ID exista
            if (idParam == null || idParam.isBlank()) {
                jsonPretty = "{ \"error\": \"ID vacío\" }";
                return;
            }

            // Convierte el ID a UUID
            UUID id = UUID.fromString(idParam);

            // Busca el ticket en la base de datos
            ticket = ticketService.buscarPorId(id);

            // Si el ticket existe, formatea su JSON
            if (ticket != null) {
                jsonPretty = formatJson(ticket.getDatosPropuestos());
            } else {
                jsonPretty = "{ \"error\": \"Ticket no encontrado\" }";
            }

        } catch (Exception ex) {

            // Manejo de cualquier error durante la carga
            jsonPretty = "{ \"error\": \"No se pudo cargar el ticket\" }";
            ticket = null;
        }
    }

    // ============================================================
    // FORMATEADOR DE JSON UNIVERSAL (FUNCIONA PARA JSONB / String)
    // ============================================================

    /**
     * Convierte cualquier representación de JSON en una cadena
     * embellecida (pretty print).
     *
     * Funciona tanto para:
     * - JSON almacenado como String
     * - JSONB retornado como Map / LinkedHashMap
     *
     * @param jsonRaw objeto o String que representa JSON
     * @return JSON formateado o mensaje de error
     */
    private String formatJson(Object jsonRaw) {

        try {
            // Configura el mapper de Jackson
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Si el JSON es nulo
            if (jsonRaw == null) {
                return "{ \"error\": \"JSON vacío\" }";
            }

            // Caso 1: JSON almacenado como String
            if (jsonRaw instanceof String rawString) {

                // Convierte el String a objeto JSON
                Object jsonObj = mapper.readValue(rawString, Object.class);

                // Regresa el JSON embellecido
                return mapper.writeValueAsString(jsonObj);
            }

            // Caso 2: JSONB retornado como Map u otro objeto
            return mapper.writeValueAsString(jsonRaw);

        } catch (Exception e) {

            // Manejo de JSON inválido
            return "{ \"error\": \"JSON inválido\" }";
        }
    }

    // ============================================================
    // GETTERS / SETTERS
    // ============================================================

    /**
     * @return parámetro ID recibido por URL
     */
    public String getIdParam() {
        return idParam;
    }

    /**
     * Establece el parámetro ID recibido por URL.
     *
     * @param idParam id del ticket
     */
    public void setIdParam(String idParam) {
        this.idParam = idParam;
    }

    /**
     * @return ticket cargado
     */
    public TicketRevision getTicket() {
        return ticket;
    }

    /**
     * @return JSON formateado de los datos propuestos
     */
    public String getJsonPretty() {
        return jsonPretty;
    }

    /**
     * @return SessionManager actual
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
