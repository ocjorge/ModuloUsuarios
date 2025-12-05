package mx.tecnm.toluca.usuarios.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import mx.tecnm.toluca.usuarios.model.*;

@ApplicationScoped
public class ReportesService {

    @PersistenceContext(unitName="UsuariosPU")
    private EntityManager em;

    public long totalAccesos() {
        return em.createQuery("SELECT COUNT(a) FROM AuditoriaAcceso a", Long.class)
                 .getSingleResult();
    }

    public long accesosExitosos() { // tipo_evento = 1 (Login)
        return em.createQuery(
            "SELECT COUNT(a) FROM AuditoriaAcceso a WHERE a.tipoEvento.id = 1", Long.class)
            .getSingleResult();
    }

    public long accesosFallidos() { // tipo_evento = 3 (Acceso Denegado)
        return em.createQuery(
            "SELECT COUNT(a) FROM AuditoriaAcceso a WHERE a.tipoEvento.id = 3", Long.class)
            .getSingleResult();
    }

    public long ticketsPendientes() { // estado_ticket = 1 (Pendiente)
        return em.createQuery(
            "SELECT COUNT(t) FROM TicketRevision t WHERE t.estado.id = 1", Long.class)
            .getSingleResult();
    }

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
