package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria_acciones")
public class AuditoriaAccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accion_registro")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @ManyToOne
    @JoinColumn(name = "id_accion", nullable = false)
    private Accion accion;

    @Column(name = "id_registro_afectado")
    private String idRegistroAfectado;

    @Column(name = "detalles_cambio", columnDefinition = "jsonb")
    private String detallesCambio;

    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora;

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public Modulo getModulo() { return modulo; }
    public Accion getAccion() { return accion; }
    public String getIdRegistroAfectado() { return idRegistroAfectado; }
    public String getDetallesCambio() { return detallesCambio; }
    public OffsetDateTime getFechaHora() { return fechaHora; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setModulo(Modulo modulo) { this.modulo = modulo; }
    public void setAccion(Accion accion) { this.accion = accion; }
    public void setIdRegistroAfectado(String x) { this.idRegistroAfectado = x; }
    public void setDetallesCambio(String d) { this.detallesCambio = d; }
    public void setFechaHora(OffsetDateTime f) { this.fechaHora = f; }
}
