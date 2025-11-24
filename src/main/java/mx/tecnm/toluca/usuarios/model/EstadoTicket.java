package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "estados_ticket")
public class EstadoTicket implements Serializable {

    @Id
    @Column(name = "id_estado")
    private Integer id;

    @Column(name = "nombre_estado", nullable = false)
    private String nombre;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
