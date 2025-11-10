package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets_revision")
public class TicketRevision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_ticket", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID idTicket;

    @NotNull
    @Column(name = "datos_propuestos", columnDefinition = "JSONB")
    private String datosPropuestos;

    @NotNull
    @Column(name = "fecha_solicitud", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime fechaSolicitud;

    @NotNull
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @NotNull
    @JoinColumn(name = "id_modulo", referencedColumnName = "id_modulo")
    @ManyToOne(optional = false)
    private Modulo idModulo;

    @NotNull
    @JoinColumn(name = "id_tipo_cambio", referencedColumnName = "id_tipo_cambio")
    @ManyToOne(optional = false)
    private TipoCambio idTipoCambio;

    @NotNull
    @JoinColumn(name = "id_estado", referencedColumnName = "id_estado")
    @ManyToOne(optional = false)
    private EstadoTicket idEstado;

    public TicketRevision() {
        this.fechaSolicitud = OffsetDateTime.now();
    }

    public TicketRevision(String datosPropuestos, Usuario idUsuario, Modulo idModulo, TipoCambio idTipoCambio, EstadoTicket idEstado) {
        this();
        this.datosPropuestos = datosPropuestos;
        this.idUsuario = idUsuario;
        this.idModulo = idModulo;
        this.idTipoCambio = idTipoCambio;
        this.idEstado = idEstado;
    }

    // Getters y Setters
    public UUID getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }

    public String getDatosPropuestos() {
        return datosPropuestos;
    }

    public void setDatosPropuestos(String datosPropuestos) {
        this.datosPropuestos = datosPropuestos;
    }

    public OffsetDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(OffsetDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Modulo getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Modulo idModulo) {
        this.idModulo = idModulo;
    }

    public TipoCambio getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(TipoCambio idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public EstadoTicket getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(EstadoTicket idEstado) {
        this.idEstado = idEstado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTicket != null ? idTicket.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TicketRevision)) {
            return false;
        }
        TicketRevision other = (TicketRevision) object;
        if ((this.idTicket == null && other.idTicket != null) || (this.idTicket != null && !this.idTicket.equals(other.idTicket))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TicketRevision[id=" + idTicket + "]";
    }
}