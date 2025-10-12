package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "acciones_clave_cat")
public class AccionClaveCat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accion_clave")
    private Integer idAccionClave;

    @Column(name = "nombre_accion_clave", unique = true, nullable = false)
    private String nombreAccionClave;

    public AccionClaveCat() {}
    public AccionClaveCat(String nombreAccionClave) { this.nombreAccionClave = nombreAccionClave; }
    public Integer getIdAccionClave() { return idAccionClave; }
    public void setIdAccionClave(Integer idAccionClave) { this.idAccionClave = idAccionClave; }
    public String getNombreAccionClave() { return nombreAccionClave; }
    public void setNombreAccionClave(String nombreAccionClave) { this.nombreAccionClave = nombreAccionClave; }
}