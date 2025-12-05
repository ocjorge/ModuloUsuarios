package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name="auditoria_accesos")
public class AuditoriaAcceso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id_auditoria", columnDefinition="uuid")
    private UUID id;

    @Column(name="fecha_hora", nullable=false)
    private OffsetDateTime fechaHora;

    @ManyToOne(optional=false)
    @JoinColumn(name="id_usuario", nullable=false)
    private Usuario usuario;

    @ManyToOne(optional=false)
    @JoinColumn(name="id_tipo_evento", nullable=false)
    private TipoEvento tipoEvento;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id=id; }

    public OffsetDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(OffsetDateTime fechaHora){ this.fechaHora=fechaHora; }

    public Usuario getUsuario(){ return usuario; }
    public void setUsuario(Usuario usuario){ this.usuario=usuario; }

    public TipoEvento getTipoEvento(){ return tipoEvento; }
    public void setTipoEvento(TipoEvento tipoEvento){ this.tipoEvento=tipoEvento; }
}
