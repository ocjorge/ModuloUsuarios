package mx.tecnm.toluca.usuarios.model;

import jakarta.persistence.*;

@Entity
@Table(name = "acciones")
public class Accion {

    @Id
    @Column(name = "id_accion")
    private Integer id;

    @Column(name = "nombre_accion", nullable = false)
    private String nombre;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
