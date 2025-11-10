

package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "estados_ticket")
public class EstadoTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_estado", unique = true)
    private String nombreEstado;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEstado")
    private List<TicketRevision> ticketsRevisionList;

    public EstadoTicket() {
    }

    public EstadoTicket(Integer idEstado) {
        this.idEstado = idEstado;
    }

    // Getters y Setters
    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
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
        hash += (idEstado != null ? idEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoTicket)) {
            return false;
        }
        EstadoTicket other = (EstadoTicket) object;
        if ((this.idEstado == null && other.idEstado != null) || (this.idEstado != null && !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EstadoTicket[id=" + idEstado + ", nombre=" + nombreEstado + "]";
    }
}