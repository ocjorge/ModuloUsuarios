package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "vista_usuarios_info") // Mapeamos la VISTA de tu base de datos
public class UsuarioInfo implements Serializable {

    @Id
    @Column(name = "id_usuario")
    private UUID id;

    @Column(name = "nombre_completo")
    private String nombre;

    @Column(name = "correo_electronico")
    private String correo;

    @Column(name = "nombre_modulo")
    private String modulo;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "nombre_estado")
    private String estado;

    @Column(name = "nombre_rol")
    private String rol;

    // Constructor vacío (obligatorio para JPA)
    public UsuarioInfo() {
    }

    // Getters (Necesarios para que el JSP pueda leer los datos)
    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getModulo() { return modulo; }
    public String getTelefono() { return telefono; }
    public String getEstado() { return estado; }
    public String getRol() { return rol; }
}