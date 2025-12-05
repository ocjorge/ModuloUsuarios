package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "estados_ticket")
public class EstadoTicket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer id;

    @Column(name = "nombre_estado", nullable = false, unique = true)
    private String nombre;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof EstadoTicket)) return false;
        EstadoTicket e=(EstadoTicket)o;
        return id!=null && id.equals(e.id);
    }
    @Override public int hashCode(){ return id!=null?id.hashCode():0; }
}
