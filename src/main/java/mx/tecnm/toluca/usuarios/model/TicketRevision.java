package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

// Importaciones para serialización, fechas y UUID
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "tickets_revision".
 * Se utiliza para gestionar solicitudes de revisión o
 * modificación de información dentro del sistema.
 */
@Entity
@Table(name = "tickets_revision")
public class TicketRevision implements Serializable {

    /**
     * Identificador único del ticket de revisión.
     * Se genera automáticamente y se maneja como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ticket", columnDefinition = "uuid")
    private UUID id;

    /**
     * Datos propuestos para la revisión.
     * Se almacenan en formato JSON (jsonb en PostgreSQL)
     * representados como texto.
     */
    @Column(name = "datos_propuestos", nullable = false, columnDefinition = "jsonb")
    private String datosPropuestos; // JSON como texto

    /**
     * Fecha y hora en que se realizó la solicitud de revisión.
     * No permite valores nulos.
     */
    @Column(name = "fecha_solicitud", nullable = false)
    private OffsetDateTime fechaSolicitud;

    /**
     * Usuario que realizó la solicitud.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Módulo del sistema al que pertenece la solicitud.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    /**
     * Tipo de cambio solicitado (alta, modificación, baja, etc.).
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_tipo_cambio", nullable = false)
    private TipoCambio tipoCambio;

    /**
     * Estado actual del ticket de revisión.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoTicket estado;

    // ===== getters/setters =====

    /**
     * Obtiene el identificador del ticket.
     * @return UUID del ticket
     */
    public UUID getId() { 
        return id; 
    }

    /**
     * Establece el identificador del ticket.
     * @param id UUID del ticket
     */
    public void setId(UUID id) { 
        this.id = id; 
    }

    /**
     * Obtiene los datos propuestos para la revisión.
     * @return datos en formato JSON
     */
    public String getDatosPropuestos() { 
        return datosPropuestos; 
    }

    /**
     * Establece los datos propuestos para la revisión.
     * @param datosPropuestos datos en formato JSON
     */
    public void setDatosPropuestos(String datosPropuestos) { 
        this.datosPropuestos = datosPropuestos; 
    }

    /**
     * Obtiene la fecha y hora de la solicitud.
     * @return fecha y hora de la solicitud
     */
    public OffsetDateTime getFechaSolicitud() { 
        return fechaSolicitud; 
    }

    /**
     * Establece la fecha y hora de la solicitud.
     * @param fechaSolicitud fecha y hora
     */
    public void setFechaSolicitud(OffsetDateTime fechaSolicitud) { 
        this.fechaSolicitud = fechaSolicitud; 
    }

    /**
     * Obtiene el usuario solicitante.
     * @return usuario
     */
    public Usuario getUsuario() { 
        return usuario; 
    }

    /**
     * Establece el usuario solicitante.
     * @param usuario usuario que solicita el cambio
     */
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    /**
     * Obtiene el módulo relacionado con la solicitud.
     * @return módulo
     */
    public Modulo getModulo() { 
        return modulo; 
    }

    /**
     * Establece el módulo relacionado con la solicitud.
     * @param modulo módulo del sistema
     */
    public void setModulo(Modulo modulo) { 
        this.modulo = modulo; 
    }

    /**
     * Obtiene el tipo de cambio solicitado.
     * @return tipo de cambio
     */
    public TipoCambio getTipoCambio() { 
        return tipoCambio; 
    }

    /**
     * Establece el tipo de cambio solicitado.
     * @param tipoCambio tipo de cambio
     */
    public void setTipoCambio(TipoCambio tipoCambio) { 
        this.tipoCambio = tipoCambio; 
    }

    /**
     * Obtiene el estado actual del ticket.
     * @return estado del ticket
     */
    public EstadoTicket getEstado() { 
        return estado; 
    }

    /**
     * Establece el estado actual del ticket.
     * @param estado estado del ticket
     */
    public void setEstado(EstadoTicket estado) { 
        this.estado = estado; 
    }
}
