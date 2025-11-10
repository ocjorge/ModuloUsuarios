package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria_acciones")
public class AuditoriaAccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_auditoria", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID idAuditoria;

    @NotNull
    @Column(name = "fecha_hora", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime fechaHora;

    @Column(name = "detalles_cambio", columnDefinition = "JSONB")
    private String detallesCambio;

    @NotNull
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @JoinColumn(name = "id_modulo", referencedColumnName = "id_modulo")
    @ManyToOne
    private Modulo idModulo;

    @JoinColumn(name = "id_accion", referencedColumnName = "id_accion")
    @ManyToOne
    private Accion idAccion;

    @Size(max = 36)
    @Column(name = "id_registro_afectado")
    private String idRegistroAfectado;

    public AuditoriaAccion() {
        this.fechaHora = OffsetDateTime.now();
    }

    public AuditoriaAccion(Usuario idUsuario) {
        this();
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public UUID getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(UUID idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public OffsetDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(OffsetDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDetallesCambio() {
        return detallesCambio;
    }

    public void setDetallesCambio(String detallesCambio) {
        this.detallesCambio = detallesCambio;
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

    public Accion getIdAccion() {
        return idAccion;
    }

    public void setIdAccion(Accion idAccion) {
        this.idAccion = idAccion;
    }

    public String getIdRegistroAfectado() {
        return idRegistroAfectado;
    }

    public void setIdRegistroAfectado(String idRegistroAfectado) {
        this.idRegistroAfectado = idRegistroAfectado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuditoria != null ? idAuditoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AuditoriaAccion)) {
            return false;
        }
        AuditoriaAccion other = (AuditoriaAccion) object;
        if ((this.idAuditoria == null && other.idAuditoria != null) || (this.idAuditoria != null && !this.idAuditoria.equals(other.idAuditoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditoriaAccion[id=" + idAuditoria + "]";
    }
}