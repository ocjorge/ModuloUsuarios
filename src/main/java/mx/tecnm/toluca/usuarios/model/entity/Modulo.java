package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "modulos")
public class Modulo implements Serializable {
    @Id
    @Column(name = "codigo_modulo")
    private String codigoModulo;

    @Column(name = "nombre_modulo", nullable = false)
    private String nombreModulo;

    @Column(name = "descripcion")
    private String descripcion;

    public Modulo() {}
    public Modulo(String codigoModulo, String nombreModulo, String descripcion) {
        this.codigoModulo = codigoModulo;
        this.nombreModulo = nombreModulo;
        this.descripcion = descripcion;
    }

    public String getCodigoModulo() { return codigoModulo; }
    public void setCodigoModulo(String codigoModulo) { this.codigoModulo = codigoModulo; }
    public String getNombreModulo() { return nombreModulo; }
    public void setNombreModulo(String nombreModulo) { this.nombreModulo = nombreModulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}