package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA (Java Persistence API)
import jakarta.persistence.*;

// Importaciones para serialización y manejo de fechas
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Entidad JPA que representa la tabla "auditoria_acciones".
 * Esta clase se utiliza para registrar las acciones realizadas
 * por los usuarios sobre distintos módulos del sistema.
 */
@Entity
@Table(name = "auditoria_acciones")
public class AuditoriaAccion implements Serializable {

    /**
     * Identificador único del registro de auditoría.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accion_registro")
    private Long id;

    /**
     * Usuario que realizó la acción.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Módulo del sistema donde se realizó la acción.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    /**
     * Acción realizada (crear, actualizar, eliminar, etc.).
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne
    @JoinColumn(name = "id_accion", nullable = false)
    private Accion accion;

    /**
     * Identificador del registro afectado por la acción.
     * Puede almacenar el ID lógico o físico del registro.
     */
    @Column(name = "id_registro_afectado")
    private String idRegistroAfectado;

    /**
     * Detalles de los cambios realizados.
     * Se almacena en formato JSON (jsonb en PostgreSQL).
     */
    @Column(name = "detalles_cambio", columnDefinition = "jsonb")
    private String detallesCambio;

    /**
     * Fecha y hora en que se realizó la acción.
     * No permite valores nulos.
     */
    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora;

    /**
     * Obtiene el identificador del registro de auditoría.
     * @return id del registro
     */
    public Long getId() { 
        return id; 
    }

    /**
     * Obtiene el usuario que realizó la acción.
     * @return usuario
     */
    public Usuario getUsuario() { 
        return usuario; 
    }

    /**
     * Obtiene el módulo donde se realizó la acción.
     * @return módulo
     */
    public Modulo getModulo() { 
        return modulo; 
    }

    /**
     * Obtiene la acción realizada.
     * @return acción
     */
    public Accion getAccion() { 
        return accion; 
    }

    /**
     * Obtiene el identificador del registro afectado.
     * @return id del registro afectado
     */
    public String getIdRegistroAfectado() { 
        return idRegistroAfectado; 
    }

    /**
     * Obtiene los detalles del cambio realizado.
     * @return detalles en formato JSON
     */
    public String getDetallesCambio() { 
        return detallesCambio; 
    }

    /**
     * Obtiene la fecha y hora del evento.
     * @return fecha y hora
     */
    public OffsetDateTime getFechaHora() { 
        return fechaHora; 
    }

    /**
     * Establece el usuario que realizó la acción.
     * @param usuario usuario responsable
     */
    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    /**
     * Establece el módulo donde ocurrió la acción.
     * @param modulo módulo del sistema
     */
    public void setModulo(Modulo modulo) { 
        this.modulo = modulo; 
    }

    /**
     * Establece la acción realizada.
     * @param accion acción ejecutada
     */
    public void setAccion(Accion accion) { 
        this.accion = accion; 
    }

    /**
     * Establece el identificador del registro afectado.
     * @param x id del registro afectado
     */
    public void setIdRegistroAfectado(String x) { 
        this.idRegistroAfectado = x; 
    }

    /**
     * Establece los detalles del cambio realizado.
     * @param d detalles en formato JSON
     */
    public void setDetallesCambio(String d) { 
        this.detallesCambio = d; 
    }

    /**
     * Establece la fecha y hora del evento.
     * @param f fecha y hora
     */
    public void setFechaHora(OffsetDateTime f) { 
        this.fechaHora = f; 
    }
}
