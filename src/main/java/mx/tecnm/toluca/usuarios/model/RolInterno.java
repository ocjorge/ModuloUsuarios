package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "roles_internos".
 * Se utiliza para definir los distintos roles internos
 * que pueden asignarse dentro del sistema.
 */
@Entity
@Table(name = "roles_internos")
public class RolInterno {

    /**
     * Identificador único del rol interno.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer id;

    /**
     * Nombre del rol interno.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "nombre_rol", nullable = false, unique = true)
    private String nombre;

    /**
     * Obtiene el identificador del rol interno.
     * @return id del rol
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del rol interno.
     * @param id id del rol
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol interno.
     * @return nombre del rol
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del rol interno.
     * @param nombre nombre del rol
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos RolInterno
     * basándose en su identificador.
     */
    @Override
    public boolean equals(Object o) {

        // Verifica si ambas referencias apuntan al mismo objeto
        if (this == o) return true;

        // Verifica si el objeto es nulo o de una clase distinta
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión del objeto para comparación
        RolInterno that = (RolInterno) o;

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
