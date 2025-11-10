package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "estados_cuenta")
public class EstadoCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta")
    private Integer idEstadoCuenta;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_estado", unique = true)
    private String nombreEstado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstadoCuenta")
    private List<Usuario> usuariosList;

    public EstadoCuenta() {
    }

    public EstadoCuenta(Integer idEstadoCuenta) {
        this.idEstadoCuenta = idEstadoCuenta;
    }

    // Getters y Setters
    public Integer getIdEstadoCuenta() {
        return idEstadoCuenta;
    }

    public void setIdEstadoCuenta(Integer idEstadoCuenta) {
        this.idEstadoCuenta = idEstadoCuenta;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public List<Usuario> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoCuenta != null ? idEstadoCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoCuenta)) {
            return false;
        }
        EstadoCuenta other = (EstadoCuenta) object;
        if ((this.idEstadoCuenta == null && other.idEstadoCuenta != null) || (this.idEstadoCuenta != null && !this.idEstadoCuenta.equals(other.idEstadoCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstadoCuenta[id=" + idEstadoCuenta + ", nombre=" + nombreEstado + "]";
    }
}

