package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

// Importación para permitir la serialización del objeto
import java.io.Serializable;

/**
 * Entidad JPA que representa la tabla "estados_ticket".
 * Se utiliza para definir los distintos estados posibles
 * que puede tener un ticket dentro del sistema.
 */
@Entity
@Table(name = "estados_ticket")
public class EstadoTicket implements Serializable {

    /**
     * Identificador único del estado del ticket.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer id;

    /**
     * Nombre del estado del ticket.
     * No permite valores nulos y debe ser único.
     */
    @Column(name = "nombre_estado", nullable = false, unique = true)
    private String nombre;

    /**
     * Obtiene el identificador del estado del ticket.
     * @return id del estado
     */
    public Integer getId() { 
        return id; 
    }

    /**
     * Establece el identificador del estado del ticket.
     * @param id id del estado
     */
    public void setId(Integer id) { 
        this.id = id; 
    }

    /**
     * Obtiene el nombre del estado del ticket.
     * @return nombre del estado
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre del estado del ticket.
     * @param nombre nombre del estado
     */
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos EstadoTicket
     * utilizando su identificador.
     */
    @Override 
    public boolean equals(Object o){

        // Verifica si ambas referencias apuntan al mismo objeto
        if(this == o) return true;

        // Verifica que el objeto sea de tipo EstadoTicket
        if(!(o instanceof EstadoTicket)) return false;

        // Conversión del objeto para comparación
        EstadoTicket e = (EstadoTicket)o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(e.id);
    }

    /**
     * Sobrescritura del método hashCode.
     * Mantiene consistencia con el método equals.
     */
    @Override 
    public int hashCode(){ 
        return id != null ? id.hashCode() : 0; 
    }
}
