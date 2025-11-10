package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "restricciones_horario")
public class RestriccionHorario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_restriccion")
    private Integer idRestriccion;

    @NotNull
    @Min(1) @Max(7) // Lunes=1, Domingo=7
    @Column(name = "dia_semana")
    private Integer diaSemana;

    @NotNull
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuario idUsuario;

    public RestriccionHorario() {
    }

    public RestriccionHorario(Integer idRestriccion) {
        this.idRestriccion = idRestriccion;
    }

    // Getters y Setters
    public Integer getIdRestriccion() {
        return idRestriccion;
    }

    public void setIdRestriccion(Integer idRestriccion) {
        this.idRestriccion = idRestriccion;
    }

    public Integer getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(Integer diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRestriccion != null ? idRestriccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RestriccionHorario)) {
            return false;
        }
        RestriccionHorario other = (RestriccionHorario) object;
        if ((this.idRestriccion == null && other.idRestriccion != null) || (this.idRestriccion != null && !this.idRestriccion.equals(other.idRestriccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RestriccionHorario[id=" + idRestriccion + ", dia=" + diaSemana + "]";
    }
}