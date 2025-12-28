package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "tipos_usuarios".
 * Se utiliza para clasificar los distintos tipos de usuarios
 * que pueden existir dentro del sistema.
 */
@Entity
@Table(name = "tipos_usuarios")
public class TipoUsuario {

    /**
     * Identificador único del tipo de usuario.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_usuario")
    private Integer id;

    /**
     * Nombre del tipo de usuario.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "nombre_tipo", nullable = false, unique = true)
    private String nombre;

    /**
     * Obtiene el identificador del tipo de usuario.
     * @return id del tipo de usuario
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del tipo de usuario.
     * @param id id del tipo de usuario
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del tipo de usuario.
     * @return nombre del tipo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del tipo de usuario.
     * @param nombre nombre del tipo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos TipoUsuario
     * basándose en su identificador.
     */
    @Override
    public boolean equals(Object o) {

        // Verifica si ambas referencias apuntan al mismo objeto
        if (this == o) return true;

        // Verifica si el objeto es nulo o de otra clase
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión del objeto para comparación
        TipoUsuario that = (TipoUsuario) o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(that.id);
    }

    /**
     * Sobrescritura del método hashCode.
     * Mantiene consistencia con el método equals.
     */
    @Override
    public int hashCode() {

        // Retorna el hash del id si existe, de lo contrario 0
        return id != null ? id.hashCode() : 0;
    }
}
