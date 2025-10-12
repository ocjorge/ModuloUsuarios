package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "permisos")
public class Permiso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(name = "nombre_permiso", unique = true, nullable = false)
    private String nombrePermiso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_modulo", nullable = false)
    private Modulo modulo;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER para facilitar la visualización del nombre de la acción
    @JoinColumn(name = "id_accion_clave", nullable = false)
    private AccionClaveCat accionClave;

    @Column(name = "descripcion")
    private String descripcion;

    public Permiso() {}
    // Constructor, getters y setters
    public Integer getIdPermiso() { return idPermiso; } public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }
    public String getNombrePermiso() { return nombrePermiso; } public void setNombrePermiso(String nombrePermiso) { this.nombrePermiso = nombrePermiso; }
    public Modulo getModulo() { return modulo; } public void setModulo(Modulo modulo) { this.modulo = modulo; }
    public AccionClaveCat getAccionClave() { return accionClave; } public void setAccionClave(AccionClaveCat accionClave) { this.accionClave = accionClave; }
    public String getDescripcion() { return descripcion; } public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}