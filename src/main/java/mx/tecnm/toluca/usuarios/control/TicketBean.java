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
import java.util.List;
import java.util.UUID;
import mx.tecnm.toluca.usuarios.modelo.EstadoTicket;
import mx.tecnm.toluca.usuarios.modelo.TicketRevision;
import mx.tecnm.toluca.usuarios.modelo.Usuario;
import mx.tecnm.toluca.usuarios.modelo.Modulo;
import mx.tecnm.toluca.usuarios.modelo.TipoCambio;
import mx.tecnm.toluca.usuarios.modelo.Accion;
import mx.tecnm.toluca.usuarios.modelo.AuditoriaAccion;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import java.io.StringReader;
import mx.tecnm.toluca.usuarios.modelo.EstadoCuenta;
import mx.tecnm.toluca.usuarios.modelo.RolInterno;


@Named("ticketBean")
@ViewScoped
public class TicketBean implements Serializable {

    @Inject
    private EntityManager em;
    
    @Inject
    private LoginBean loginBean;

    private List<TicketRevision> tickets;
    private TicketRevision selectedTicket;
    private String filterState; 
    private UUID ticketIdToLoad; // Para recibir el ID del ticket desde JavaScript/f:param
    private String ticketJsonOutput; // Nueva propiedad para contener el JSON del ticket

    private List<Modulo> modulos;
    private List<TipoCambio> tiposCambio;
    private List<EstadoTicket> estadosTicket;

    @PostConstruct
    public void init() {
        loadTickets();
        loadDropdowns();
    }
    
    // En TicketBean.java - agregar este método:
    public void loadTicketForReview(UUID ticketId) {
        this.ticketIdToLoad = ticketId;
        selectTicketForReview(); // Llama al método existente
    }

    public void loadTickets() {
        String jpql = "SELECT t FROM TicketRevision t JOIN FETCH t.idUsuario JOIN FETCH t.idModulo JOIN FETCH t.idTipoCambio JOIN FETCH t.idEstado";
        if (filterState != null && !filterState.isEmpty() && !"Todos".equals(filterState)) {
            jpql += " WHERE t.idEstado.nombreEstado = :estado";
        }
        jpql += " ORDER BY t.fechaSolicitud DESC";

        TypedQuery<TicketRevision> query = em.createQuery(jpql, TicketRevision.class);
        if (filterState != null && !filterState.isEmpty() && !"Todos".equals(filterState)) {
            query.setParameter("estado", filterState);
        }
        tickets = query.getResultList();
    }

    public void loadDropdowns() {
        modulos = em.createQuery("SELECT m FROM Modulo m", Modulo.class).getResultList();
        tiposCambio = em.createQuery("SELECT tc FROM TipoCambio tc", TipoCambio.class).getResultList();
        estadosTicket = em.createQuery("SELECT et FROM EstadoTicket et", EstadoTicket.class).getResultList();
    }

    public void filterTicketsByState(String state) {
        this.filterState = state;
        loadTickets();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Filtro aplicado", "Tickets filtrados por estado: " + state));
    }

    // Este método se llamará desde el f:ajax del botón "Revisar"
    // y su valor se asignará a ticketJsonOutput para que JS lo lea.
    @Transactional
    public String selectTicketForReview() {
        if (ticketIdToLoad != null) {
            selectedTicket = em.find(TicketRevision.class, ticketIdToLoad);
            if (selectedTicket != null) {
                JsonObjectBuilder jsonBuilder = Json.createObjectBuilder()
                    .add("idTicket", selectedTicket.getIdTicket().toString())
                    .add("tipoCambioNombre", selectedTicket.getIdTipoCambio().getNombreCambio())
                    .add("moduloNombre", selectedTicket.getIdModulo().getNombreModulo())
                    .add("fechaSolicitud", selectedTicket.getFechaSolicitud().toLocalDate().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE))
                    .add("estadoNombre", selectedTicket.getIdEstado().getNombreEstado())
                    .add("usuarioSolicitanteNombre", selectedTicket.getIdUsuario().getNombreCompleto())
                    .add("usuarioSolicitanteEmail", selectedTicket.getIdUsuario().getCorreoElectronico())
                    .add("datosPropuestos", selectedTicket.getDatosPropuestos()); // Asegúrate de que esto sea un String JSON válido.

                this.ticketJsonOutput = jsonBuilder.build().toString(); // Asigna el JSON string a la propiedad
                return this.ticketJsonOutput;
            }
        }
        this.ticketJsonOutput = null; // En caso de error o no encontrar ticket
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo cargar el ticket."));
        return null;
    }

    private JsonObject parseDatosPropuestos(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return Json.createObjectBuilder().build(); 
        }
        try (JsonReader reader = Json.createReader(new StringReader(jsonString))) {
            return reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return Json.createObjectBuilder().add("errorParsing", e.getMessage()).build();
        }
    }

    @Transactional
    public void approveTicket() {
        if (selectedTicket == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado ningún ticket."));
            return;
        }

        try {
            EstadoTicket estadoAprobado = em.find(EstadoTicket.class, 2);
            selectedTicket.setIdEstado(estadoAprobado);
            
            JsonObject detalles = parseDatosPropuestos(selectedTicket.getDatosPropuestos());
            if (detalles != null && detalles.containsKey("id_usuario_afectado")) {
                UUID idUsuarioAfectado = UUID.fromString(detalles.getString("id_usuario_afectado"));
                Usuario userAfectado = em.find(Usuario.class, idUsuarioAfectado);
                
                if (userAfectado != null) {
                    if (detalles.containsKey("estado_propuesto_id")) { 
                        Integer estadoPropuestoId = detalles.getInt("estado_propuesto_id");
                        userAfectado.setIdEstadoCuenta(em.find(EstadoCuenta.class, estadoPropuestoId));
                    }
                    if (detalles.containsKey("rol_propuesto_id")) {
                        Integer rolPropuestoId = detalles.getInt("rol_propuesto_id");
                        userAfectado.setIdRol(em.find(RolInterno.class, rolPropuestoId));
                    }
                    // Aquí se podrían aplicar otros cambios como nombre, teléfono, dirección si el JSON los contiene.
                    if (detalles.containsKey("nombre_completo")) {
                        userAfectado.setNombreCompleto(detalles.getString("nombre_completo"));
                    }
                     if (detalles.containsKey("telefono")) {
                        userAfectado.setTelefono(detalles.getString("telefono"));
                    }
                     if (detalles.containsKey("direccion")) {
                        userAfectado.setDireccion(detalles.getString("direccion"));
                    }
                    em.merge(userAfectado);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Usuario afectado por el ticket no encontrado, solo se aprobó el ticket."));
                }
            }

            em.merge(selectedTicket);
            
            if (loginBean.getCurrentUser() != null) {
                AuditoriaAccion auditoria = new AuditoriaAccion(loginBean.getCurrentUser());
                auditoria.setIdModulo(selectedTicket.getIdModulo());
                auditoria.setIdAccion(em.find(Accion.class, 4)); // Acción 'Aprobar' (ID 4)
                auditoria.setIdRegistroAfectado(selectedTicket.getIdTicket().toString());
                auditoria.setDetallesCambio("{\"accion\": \"Aprobación de ticket\", \"ticket_id\": \"" + selectedTicket.getIdTicket() + "\", \"tipo_cambio\": \"" + selectedTicket.getIdTipoCambio().getNombreCambio() + "\"}");
                em.persist(auditoria);
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Ticket " + selectedTicket.getIdTicket() + " aprobado y cambios aplicados."));
            loadTickets();
            selectedTicket = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al aprobar ticket", "No se pudo aprobar el ticket: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Transactional
    public void rejectTicket(String rejectionReason) {
        if (selectedTicket == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado ningún ticket."));
            return;
        }
        if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Debe proporcionar un motivo para el rechazo."));
            return;
        }

        try {
            EstadoTicket estadoRechazado = em.find(EstadoTicket.class, 3);
            selectedTicket.setIdEstado(estadoRechazado);
            
            JsonObject currentDetailsJson = parseDatosPropuestos(selectedTicket.getDatosPropuestos());
            JsonObject newDetailsJson = Json.createObjectBuilder(currentDetailsJson)
                                            .add("motivo_rechazo", rejectionReason.trim())
                                            .build();
            selectedTicket.setDatosPropuestos(newDetailsJson.toString());

            em.merge(selectedTicket);
            
            if (loginBean.getCurrentUser() != null) {
                AuditoriaAccion auditoria = new AuditoriaAccion(loginBean.getCurrentUser());
                auditoria.setIdModulo(selectedTicket.getIdModulo());
                auditoria.setIdAccion(em.find(Accion.class, 5)); // Acción 'Rechazar' (ID 5)
                auditoria.setIdRegistroAfectado(selectedTicket.getIdTicket().toString());
                auditoria.setDetallesCambio("{\"accion\": \"Rechazo de ticket\", \"ticket_id\": \"" + selectedTicket.getIdTicket() + "\", \"motivo\": \"" + rejectionReason.trim() + "\"}");
                em.persist(auditoria);
            }

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Ticket " + selectedTicket.getIdTicket() + " rechazado."));
            loadTickets();
            selectedTicket = null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error al rechazar ticket", "No se pudo rechazar el ticket: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    // --- Getters y Setters ---
    public List<TicketRevision> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketRevision> tickets) {
        this.tickets = tickets;
    }

    public TicketRevision getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(TicketRevision selectedTicket) {
        this.selectedTicket = selectedTicket;
    }

    public String getFilterState() {
        return filterState;
    }

    public void setFilterState(String filterState) {
        this.filterState = filterState;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public List<TipoCambio> getTiposCambio() {
        return tiposCambio;
    }

    public List<EstadoTicket> getEstadosTicket() {
        return estadosTicket;
    }
    
    public UUID getTicketIdToLoad() {
        return ticketIdToLoad;
    }

    public void setTicketIdToLoad(UUID ticketIdToLoad) {
        this.ticketIdToLoad = ticketIdToLoad;
    }

    public String getTicketJsonOutput() {
        return ticketJsonOutput;
    }

    public void setTicketJsonOutput(String ticketJsonOutput) {
        this.ticketJsonOutput = ticketJsonOutput;
    }

    public String getShortenedDetails(String jsonDetails) {
        if (jsonDetails == null || jsonDetails.length() < 50) {
            return jsonDetails;
        }
        return jsonDetails.substring(0, 47) + "...";
    }
}