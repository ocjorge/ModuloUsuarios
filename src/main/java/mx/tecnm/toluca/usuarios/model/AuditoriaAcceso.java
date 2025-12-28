package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA (persistencia)
import jakarta.persistence.*;

// Importaciones para serialización y manejo de fechas y UUID
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "auditoria_accesos".
 * Se utiliza para registrar eventos de acceso o acciones
 * realizadas por los usuarios en el sistema.
 */
@Entity
@Table(name="auditoria_accesos")
public class AuditoriaAcceso implements Serializable {

    /**
     * Identificador único del registro de auditoría.
     * Se genera automáticamente y se maneja como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_auditoria", columnDefinition="uuid")
    private UUID id;

    /**
     * Fecha y hora en que ocurrió el evento.
     * No permite valores nulos.
     */
    @Column(name="fecha_hora", nullable=false)
    private OffsetDateTime fechaHora;

    /**
     * Usuario asociado al evento de auditoría.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional=false)
    @JoinColumn(name="id_usuario", nullable=false)
    private Usuario usuario;

    /**
     * Tipo de evento registrado (login, logout, error, etc.).
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne(optional=false)
    @JoinColumn(name="id_tipo_evento", nullable=false)
    private TipoEvento tipoEvento;

    /**
     * Obtiene el identificador del registro de auditoría.
     * @return UUID del registro
     */
    public UUID getId() { 
        return id; 
    }

    /**
     * Establece el identificador del registro de auditoría.
     * @param id UUID del registro
     */
    public void setId(UUID id) { 
        this.id=id; 
    }

    /**
     * Obtiene la fecha y hora del evento.
     * @return fecha y hora del evento
     */
    public OffsetDateTime getFechaHora() { 
        return fechaHora; 
    }

    /**
     * Establece la fecha y hora del evento.
     * @param fechaHora fecha y hora del evento
     */
    public void setFechaHora(OffsetDateTime fechaHora){ 
        this.fechaHora=fechaHora; 
    }

    /**
     * Obtiene el usuario asociado al evento.
     * @return usuario
     */
    public Usuario getUsuario(){ 
        return usuario; 
    }

    /**
     * Establece el usuario asociado al evento.
     * @param usuario usuario que generó el evento
     */
    public void setUsuario(Usuario usuario){ 
        this.usuario=usuario; 
    }

    /**
     * Obtiene el tipo de evento registrado.
     * @return tipo de evento
     */
    public TipoEvento getTipoEvento(){ 
        return tipoEvento; 
    }

    /**
     * Establece el tipo de evento registrado.
     * @param tipoEvento tipo de evento
     */
    public void setTipoEvento(TipoEvento tipoEvento){ 
        this.tipoEvento=tipoEvento; 
    }
}
