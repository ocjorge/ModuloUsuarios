package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "modulos")
public class Modulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Size(min = 1, max = 50)
    @Column(name = "id_modulo")
    private String idModulo;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "prefijo_id", unique = true)
    private String prefijoId;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre_modulo", unique = true)
    private String nombreModulo;

    @OneToMany(mappedBy = "idModulo")
    private List<Usuario> usuariosList;

    @OneToMany(mappedBy = "idModulo")
    private List<AuditoriaAccion> auditoriaAccionList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModulo")
    private List<TicketRevision> ticketsRevisionList;

    public Modulo() {
    }

    public Modulo(String idModulo) {
        this.idModulo = idModulo;
    }

    // Getters y Setters
    public String getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(String idModulo) {
        this.idModulo = idModulo;
    }

    public String getPrefijoId() {
        return prefijoId;
    }

    public void setPrefijoId(String prefijoId) {
        this.prefijoId = prefijoId;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public List<Usuario> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
    }

    public List<AuditoriaAccion> getAuditoriaAccionList() {
        return auditoriaAccionList;
    }

    public void setAuditoriaAccionList(List<AuditoriaAccion> auditoriaAccionList) {
        this.auditoriaAccionList = auditoriaAccionList;
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
        hash += (idModulo != null ? idModulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Modulo)) {
            return false;
        }
        Modulo other = (Modulo) object;
        if ((this.idModulo == null && other.idModulo != null) || (this.idModulo != null && !this.idModulo.equals(other.idModulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modulo[id=" + idModulo + ", nombre=" + nombreModulo + "]";
    }
}