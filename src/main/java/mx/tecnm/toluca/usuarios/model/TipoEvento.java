package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipos_evento")
public class TipoEvento {

    @Id
    @Column(name = "id_tipo_evento")
    private Integer id;

    @Column(name = "nombre_evento", nullable = false)
    private String nombre;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
