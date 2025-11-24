package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipos_cambios")
public class TipoCambio implements Serializable {

    @Id
    @Column(name = "id_tipo_cambio")
    private Integer id;

    @Column(name = "nombre_cambio", nullable = false)
    private String nombre;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
