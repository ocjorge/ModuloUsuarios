package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets_revision")
public class TicketRevision implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_ticket", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "datos_propuestos", columnDefinition = "jsonb", nullable = false)
    private String datosPropuestos;

    @Column(name = "fecha_solicitud", nullable = false)
    private OffsetDateTime fechaSolicitud;

    // ---------- Relaciones ----------
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @ManyToOne
    @JoinColumn(name = "id_tipo_cambio", nullable = false)
    private TipoCambio tipoCambio;

    @ManyToOne
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoTicket estado;

    // --------- Getters/Setters ----------
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDatosPropuestos() { return datosPropuestos; }
    public void setDatosPropuestos(String datosPropuestos) { this.datosPropuestos = datosPropuestos; }

    public OffsetDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(OffsetDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Modulo getModulo() { return modulo; }
    public void setModulo(Modulo modulo) { this.modulo = modulo; }

    public TipoCambio getTipoCambio() { return tipoCambio; }
    public void setTipoCambio(TipoCambio tipoCambio) { this.tipoCambio = tipoCambio; }

    public EstadoTicket getEstado() { return estado; }
    public void setEstado(EstadoTicket estado) { this.estado = estado; }
}
