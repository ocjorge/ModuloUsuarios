package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipos_cambios")
public class TipoCambio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cambio")
    private Integer id;

    @Column(name = "nombre_cambio", nullable = false, unique = true)
    private String nombre;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof TipoCambio)) return false;
        TipoCambio t=(TipoCambio)o;
        return id!=null && id.equals(t.id);
    }
    @Override public int hashCode(){ return id!=null?id.hashCode():0; }
}
