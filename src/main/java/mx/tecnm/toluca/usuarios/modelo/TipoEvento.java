package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tipos_evento")
public class TipoEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_evento")
    private Integer idTipoEvento;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_evento", unique = true)
    private String nombreEvento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoEvento")
    private List<AuditoriaAcceso> auditoriaAccesoList;

    public TipoEvento() {
    }

    public TipoEvento(Integer idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    // Getters y Setters
    public Integer getIdTipoEvento() {
        return idTipoEvento;
    }

    public void setIdTipoEvento(Integer idTipoEvento) {
        this.idTipoEvento = idTipoEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public List<AuditoriaAcceso> getAuditoriaAccesoList() {
        return auditoriaAccesoList;
    }

    public void setAuditoriaAccesoList(List<AuditoriaAcceso> auditoriaAccesoList) {
        this.auditoriaAccesoList = auditoriaAccesoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoEvento != null ? idTipoEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoEvento)) {
            return false;
        }
        TipoEvento other = (TipoEvento) object;
        if ((this.idTipoEvento == null && other.idTipoEvento != null) || (this.idTipoEvento != null && !this.idTipoEvento.equals(other.idTipoEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoEvento[id=" + idTipoEvento + ", nombre=" + nombreEvento + "]";
    }
}
