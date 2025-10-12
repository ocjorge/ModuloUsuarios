package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "usuario_permiso")
public class UsuarioPermiso implements Serializable {

    @EmbeddedId
    private UsuarioPermisoPK id;

    @ManyToOne @MapsId("idUsuario") @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne @MapsId("idPermiso") @JoinColumn(name = "id_permiso")
    private Permiso permiso;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = true;

    public UsuarioPermiso() {}
    public UsuarioPermiso(Usuario usuario, Permiso permiso, Boolean habilitado) {
        this.id = new UsuarioPermisoPK(usuario.getIdUsuario(), permiso.getIdPermiso());
        this.usuario = usuario;
        this.permiso = permiso;
        this.habilitado = habilitado;
    }

    public UsuarioPermisoPK getId() { return id; } public void setId(UsuarioPermisoPK id) { this.id = id; }
    public Usuario getUsuario() { return usuario; } public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Permiso getPermiso() { return permiso; } public void setPermiso(Permiso permiso) { this.permiso = permiso; }
    public Boolean getHabilitado() { return habilitado; } public void setHabilitado(Boolean habilitado) { this.habilitado = habilitado; }
}