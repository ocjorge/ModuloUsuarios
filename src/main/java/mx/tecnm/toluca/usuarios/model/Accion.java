package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para el uso de JPA (Java Persistence API)
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "acciones" en la base de datos.
 * Esta clase se utiliza para mapear registros de la tabla a objetos Java.
 */
@Entity
@Table(name = "acciones")
public class Accion {

    /**
     * Identificador único de la acción.
     * Se marca con @Id para indicar que es la clave primaria.
     */
    @Id
    @Column(name = "id_accion")
    private Integer id;

    /**
     * Nombre de la acción.
     * Se mapea a la columna "nombre_accion" y no permite valores nulos.
     */
    @Column(name = "nombre_accion", nullable = false)
    private String nombre;

    /**
     * Obtiene el identificador de la acción.
     * @return id de la acción
     */
    public Integer getId() { 
        return id; 
    }

    /**
     * Obtiene el nombre de la acción.
     * @return nombre de la acción
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el identificador de la acción.
     * @param id nuevo id de la acción
     */
    public void setId(Integer id) { 
        this.id = id; 
    }

    /**
     * Establece el nombre de la acción.
     * @param nombre nuevo nombre de la acción
     */
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
}
