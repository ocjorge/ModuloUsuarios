package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "rol_permiso")
public class RolPermiso implements Serializable {

    @EmbeddedId // Indica que la PK es una clase embebida
    private RolPermisoPK id;

    @ManyToOne @MapsId("idRol") @JoinColumn(name = "id_rol") // Mapea la parte idRol de la PK
    private RolInterno rolInterno;

    @ManyToOne @MapsId("idPermiso") @JoinColumn(name = "id_permiso") // Mapea la parte idPermiso de la PK
    private Permiso permiso;

    @Column(name = "habilitado", nullable = false)
    private Boolean habilitado = true;

    public RolPermiso() {}
    public RolPermiso(RolInterno rolInterno, Permiso permiso, Boolean habilitado) {
        this.id = new RolPermisoPK(rolInterno.getIdRol(), permiso.getIdPermiso());
        this.rolInterno = rolInterno;
        this.permiso = permiso;
        this.habilitado = habilitado;
    }

    public RolPermisoPK getId() { return id; } public void setId(RolPermisoPK id) { this.id = id; }
    public RolInterno getRolInterno() { return rolInterno; } public void setRolInterno(RolInterno rolInterno) { this.rolInterno = rolInterno; }
    public Permiso getPermiso() { return permiso; } public void setPermiso(Permiso permiso) { this.permiso = permiso; }
    public Boolean getHabilitado() { return habilitado; } public void setHabilitado(Boolean habilitado) { this.habilitado = habilitado; }
}