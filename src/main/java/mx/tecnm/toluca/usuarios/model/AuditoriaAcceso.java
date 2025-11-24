package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria_accesos")
public class AuditoriaAcceso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acceso")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_evento", nullable = false)
    private TipoEvento tipoEvento;

    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora;

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public TipoEvento getTipoEvento() { return tipoEvento; }
    public OffsetDateTime getFechaHora() { return fechaHora; }

    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setTipoEvento(TipoEvento tipoEvento) { this.tipoEvento = tipoEvento; }
    public void setFechaHora(OffsetDateTime fechaHora) { this.fechaHora = fechaHora; }
}
