package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "estados_cuenta_cat")
public class EstadoCuentaCat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta")
    private Integer idEstadoCuenta;

    @Column(name = "nombre_estado", unique = true, nullable = false)
    private String nombreEstado;

    public EstadoCuentaCat() {}
    public EstadoCuentaCat(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public Integer getIdEstadoCuenta() { return idEstadoCuenta; }
    public void setIdEstadoCuenta(Integer idEstadoCuenta) { this.idEstadoCuenta = idEstadoCuenta; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
}