package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipos_evento")
public class TipoEvento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_tipo_evento")
    private Integer id;

    @Column(name="nombre_evento", nullable=false, unique=true)
    private String nombre;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id=id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre){ this.nombre=nombre; }

    @Override public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof TipoEvento)) return false;
        TipoEvento t=(TipoEvento)o;
        return id!=null && id.equals(t.id);
    }
    @Override public int hashCode(){ return id!=null?id.hashCode():0; }
}
