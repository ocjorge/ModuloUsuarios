package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tipos_usuario_cat")
public class TipoUsuarioCat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario")
    private Integer idTipoUsuario;

    @Column(name = "nombre_tipo", unique = true, nullable = false)
    private String nombreTipo;

    public TipoUsuarioCat() {}
    public TipoUsuarioCat(String nombreTipo) { this.nombreTipo = nombreTipo; }
    public Integer getIdTipoUsuario() { return idTipoUsuario; }
    public void setIdTipoUsuario(Integer idTipoUsuario) { this.idTipoUsuario = idTipoUsuario; }
    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }
}