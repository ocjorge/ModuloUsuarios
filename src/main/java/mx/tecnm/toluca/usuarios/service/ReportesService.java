package mx.tecnm.toluca.usuarios.service;

// Importaciones para el manejo de contexto de la aplicación
import jakarta.enterprise.context.ApplicationScoped;

// Importaciones para JPA
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Importación para manejo de listas
import java.util.List;

// Importación de las entidades del modelo
import mx.tecnm.toluca.usuarios.model.*;

/**
 * Servicio de reportes.
 * Se encarga de obtener información estadística
 * relacionada con accesos y tickets del sistema.
 */
@ApplicationScoped
public class ReportesService {

    /**
     * EntityManager inyectado mediante PersistenceContext.
     * Se utiliza para ejecutar consultas JPQL y SQL nativo.
     */
    @PersistenceContext(unitName="UsuariosPU")
    private EntityManager em;

    /**
     * Obtiene el total de accesos registrados en el sistema.
     * @return número total de accesos
     */
    public long totalAccesos() {
        return em.createQuery(
                "SELECT COUNT(a) FROM AuditoriaAcceso a", 
                Long.class)
                 .getSingleResult();
    }

    /**
     * Obtiene el total de accesos exitosos.
     * Se considera acceso exitoso cuando el tipo de evento es 1 (Login).
     * @return número de accesos exitosos
     */
    public long accesosExitosos() { // tipo_evento = 1 (Login)
        return em.createQuery(
            "SELECT COUNT(a) FROM AuditoriaAcceso a WHERE a.tipoEvento.id = 1", 
            Long.class)
            .getSingleResult();
    }

    /**
     * Obtiene el total de accesos fallidos.
     * Se considera acceso fallido cuando el tipo de evento es 3 (Acceso Denegado).
     * @return número de accesos fallidos
     */
    public long accesosFallidos() { // tipo_evento = 3 (Acceso Denegado)
        return em.createQuery(
            "SELECT COUNT(a) FROM AuditoriaAcceso a WHERE a.tipoEvento.id = 3", 
            Long.class)
            .getSingleResult();
    }

    /**
     * Obtiene el total de tickets pendientes.
     * Se consideran pendientes aquellos con estado 1.
     * @return número de tickets pendientes
     */
    public long ticketsPendientes() { // estado_ticket = 1 (Pendiente)
        return em.createQuery(
            "SELECT COUNT(t) FROM TicketRevision t WHERE t.estado.id = 1", 
            Long.class)
            .getSingleResult();
    }

    /**
     * Obtiene el número de accesos por día de los últimos 7 días.
     * Se utiliza una consulta SQL nativa para agrupar por fecha.
     *
     * @return lista de arreglos Object[], donde:
     *         [0] = día (String)
     *         [1] = total de accesos (Long)
     */
    // Accesos por día (últimos 7 días)
    public List<Object[]> accesosUltimos7Dias() {
        return em.createNativeQuery("""
            SELECT to_char(fecha_hora, 'YYYY-MM-DD') AS dia, count(*) 
            FROM auditoria_accesos
            WHERE fecha_hora >= now() - interval '7 days'
            GROUP BY dia
            ORDER BY dia
        """).getResultList();
    }

    /**
     * Obtiene el número de tickets agrupados por estado.
     * Utiliza JPQL con JOIN para acceder al nombre del estado.
     *
     * @return lista de arreglos Object[], donde:
     *         [0] = nombre del estado (String)
     *         [1] = total de tickets (Long)
     */
    // Tickets por estado
    public List<Object[]> ticketsPorEstado() {
        return em.createQuery("""
            SELECT e.nombre, COUNT(t)
            FROM TicketRevision t JOIN t.estado e
            GROUP BY e.nombre
            ORDER BY e.nombre
        """, Object[].class).getResultList();
    }
}
