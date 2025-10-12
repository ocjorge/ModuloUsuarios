package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects; // Necesario si mantienes equals/hashCode

@Entity
@Table(name = "roles_internos")
public class RolInterno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre_rol", unique = true, nullable = false)
    private String nombreRol;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "puede_revisar_nosql", nullable = false)
    private Boolean puedeRevisarNosql = false;

    public RolInterno() {}
    public RolInterno(String nombreRol, String descripcion, Boolean puedeRevisarNosql) {
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.puedeRevisarNosql = puedeRevisarNosql;
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }
    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Boolean getPuedeRevisarNosql() { return puedeRevisarNosql; }
    public void setPuedeRevisarNosql(Boolean puedeRevisarNosql) { this.puedeRevisarNosql = puedeRevisarNosql; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolInterno that = (RolInterno) o;
        return Objects.equals(idRol, that.idRol);
    }
    @Override
    public int hashCode() { return Objects.hash(idRol); }
}