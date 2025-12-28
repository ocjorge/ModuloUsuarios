package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "modulos".
 * Se utiliza para definir los distintos módulos
 * que componen el sistema.
 */
@Entity
@Table(name = "modulos")
public class Modulo {

    /**
     * Identificador único del módulo.
     * Se mapea directamente a la columna "id_modulo".
     */
    @Id
    @Column(name = "id_modulo")
    private String id;

    /**
     * Prefijo identificador del módulo.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "prefijo_id", nullable = false, unique = true)
    private String prefijo;

    /**
     * Nombre descriptivo del módulo.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "nombre_modulo", nullable = false, unique = true)
    private String nombre;
    
    /**
     * Obtiene el identificador del módulo.
     * @return id del módulo
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador del módulo.
     * @param id id del módulo
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el prefijo del módulo.
     * @return prefijo del módulo
     */
    public String getPrefijo() {
        return prefijo;
    }

    /**
     * Establece el prefijo del módulo.
     * @param prefijo prefijo del módulo
     */
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * Obtiene el nombre del módulo.
     * @return nombre del módulo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del módulo.
     * @param nombre nombre del módulo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos Modulo
     * basándose en su identificador.
     */
    @Override
    public boolean equals(Object o) {

        // Verifica si ambas referencias apuntan al mismo objeto
        if (this == o) return true;

        // Verifica si el objeto es nulo o de otra clase
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión del objeto para comparación
        Modulo modulo = (Modulo) o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(modulo.id);
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
