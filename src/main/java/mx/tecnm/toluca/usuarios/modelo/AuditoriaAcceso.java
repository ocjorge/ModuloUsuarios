package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria_accesos")
public class AuditoriaAcceso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_auditoria", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID idAuditoria;

    @NotNull
    @Column(name = "fecha_hora", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime fechaHora;

    @NotNull
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @NotNull
    @JoinColumn(name = "id_tipo_evento", referencedColumnName = "id_tipo_evento")
    @ManyToOne(optional = false)
    private TipoEvento idTipoEvento;

    public AuditoriaAcceso() {
        this.fechaHora = OffsetDateTime.now();
    }

    public AuditoriaAcceso(Usuario idUsuario, TipoEvento idTipoEvento) {
        this();
        this.idUsuario = idUsuario;
        this.idTipoEvento = idTipoEvento;
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

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoEvento getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(TipoEvento idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuditoria != null ? idAuditoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AuditoriaAcceso)) {
            return false;
        }
        AuditoriaAcceso other = (AuditoriaAcceso) object;
        if ((this.idAuditoria == null && other.idAuditoria != null) || (this.idAuditoria != null && !this.idAuditoria.equals(other.idAuditoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditoriaAcceso[id=" + idAuditoria + "]";
    }
}
