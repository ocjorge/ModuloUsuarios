package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "estados_cuenta".
 * Se utiliza para definir los distintos estados posibles
 * que puede tener una cuenta dentro del sistema.
 */
@Entity
@Table(name = "estados_cuenta")
public class EstadoCuenta {

    /**
     * Identificador único del estado de cuenta.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cuenta")
    private Integer id;

    /**
     * Nombre del estado de cuenta.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "nombre_estado", nullable = false, unique = true)
    private String nombre;

    /**
     * Obtiene el identificador del estado de cuenta.
     * @return id del estado de cuenta
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del estado de cuenta.
     * @param id id del estado de cuenta
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del estado de cuenta.
     * @return nombre del estado
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del estado de cuenta.
     * @param nombre nombre del estado
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Sobrescritura del método equals.
     * Se utiliza para comparar dos objetos EstadoCuenta
     * basándose en su identificador.
     */
    @Override
    public boolean equals(Object o) {

        // Verifica si ambos objetos son la misma instancia
        if (this == o) return true;

        // Verifica si el objeto es nulo o de una clase distinta
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión del objeto para comparación
        EstadoCuenta that = (EstadoCuenta) o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(that.id);
    }

    /**
     * Sobrescritura del método hashCode.
     * Garantiza consistencia con el método equals.
     */
    @Override
    public int hashCode() {

        // Retorna el hash del id si existe, de lo contrario 0
        return id != null ? id.hashCode() : 0;
    }
}
