package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable // Indica que esta clase es parte de la clave primaria de otra entidad
public class RolPermisoPK implements Serializable {

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "id_permiso")
    private Integer idPermiso;

    public RolPermisoPK() {}
    public RolPermisoPK(Integer idRol, Integer idPermiso) {
        this.idRol = idRol;
        this.idPermiso = idPermiso;
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolPermisoPK that = (RolPermisoPK) o;
        return Objects.equals(idRol, that.idRol) &&
               Objects.equals(idPermiso, that.idPermiso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRol, idPermiso);
    }
}