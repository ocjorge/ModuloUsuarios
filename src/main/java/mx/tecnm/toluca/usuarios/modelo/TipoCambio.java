package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tipos_cambios")
public class TipoCambio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cambio")
    private Integer idTipoCambio;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nombre_cambio", unique = true)
    private String nombreCambio;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoCambio")
    private List<TicketRevision> ticketsRevisionList;

    public TipoCambio() {
    }

    public TipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    // Getters y Setters
    public Integer getIdTipoCambio() {
        return idTipoCambio;
    }

    public void setIdTipoCambio(Integer idTipoCambio) {
        this.idTipoCambio = idTipoCambio;
    }

    public String getNombreCambio() {
        return nombreCambio;
    }

    public void setNombreCambio(String nombreCambio) {
        this.nombreCambio = nombreCambio;
    }

    public List<TicketRevision> getTicketsRevisionList() {
        return ticketsRevisionList;
    }

    public void setTicketsRevisionList(List<TicketRevision> ticketsRevisionList) {
        this.ticketsRevisionList = ticketsRevisionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCambio != null ? idTipoCambio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoCambio)) {
            return false;
        }
        TipoCambio other = (TipoCambio) object;
        if ((this.idTipoCambio == null && other.idTipoCambio != null) || (this.idTipoCambio != null && !this.idTipoCambio.equals(other.idTipoCambio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoCambio[id=" + idTipoCambio + ", nombre=" + nombreCambio + "]";
    }
}