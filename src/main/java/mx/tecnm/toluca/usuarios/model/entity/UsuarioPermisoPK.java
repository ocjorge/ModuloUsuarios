package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UsuarioPermisoPK implements Serializable {

    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Column(name = "id_permiso")
    private Integer idPermiso;

    public UsuarioPermisoPK() {}
    public UsuarioPermisoPK(UUID idUsuario, Integer idPermiso) {
        this.idUsuario = idUsuario;
        this.idPermiso = idPermiso;
    }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioPermisoPK that = (UsuarioPermisoPK) o;
        return Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idPermiso, that.idPermiso);
    }

    @Override
public int hashCode() {
        return Objects.hash(idUsuario, idPermiso);
    }
}