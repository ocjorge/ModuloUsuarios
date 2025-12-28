package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

// Importación para permitir la serialización del objeto
import java.io.Serializable;

/**
 * Entidad JPA que representa la tabla "tipos_evento".
 * Se utiliza para clasificar los distintos tipos de
 * eventos que pueden registrarse en el sistema
 * (por ejemplo: login, logout, error, acceso, etc.).
 */
@Entity
@Table(name = "tipos_evento")
public class TipoEvento implements Serializable {

    /**
     * Identificador único del tipo de evento.
     * Se genera automáticamente mediante una estrategia IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_tipo_evento")
    private Integer id;

    /**
     * Nombre descriptivo del tipo de evento.
     * No permite valores nulos y debe ser único.
     */
    @Column(name="nombre_evento", nullable=false, unique=true)
    private String nombre;

    /**
     * Obtiene el identificador del tipo de evento.
     * @return id del tipo de evento
     */
    public Integer getId() { 
        return id; 
    }

    /**
     * Establece el identificador del tipo de evento.
     * @param id id del tipo de evento
     */
    public void setId(Integer id) { 
        this.id = id; 
    }

    /**
     * Obtiene el nombre del tipo de evento.
     * @return nombre del evento
     */
    public String getNombre() { 
        return nombre; 
    }

    /**
     * Establece el nombre del tipo de evento.
     * @param nombre nombre del evento
     */
    public void setNombre(String nombre){ 
        this.nombre = nombre; 
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos TipoEvento
     * utilizando su identificador.
     */
    @Override 
    public boolean equals(Object o){

        // Verifica si ambas referencias apuntan al mismo objeto
        if(this == o) return true;

        // Verifica que el objeto sea de tipo TipoEvento
        if(!(o instanceof TipoEvento)) return false;

        // Conversión del objeto para comparación
        TipoEvento t = (TipoEvento)o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(t.id);
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
