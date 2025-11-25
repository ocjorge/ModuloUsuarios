package mx.tecnm.toluca.usuarios.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import mx.tecnm.toluca.usuarios.model.*;

@ApplicationScoped
public class TicketService {

    @PersistenceContext(unitName="UsuariosPU")
    private EntityManager em;

    public List<TicketRevision> listarTodos() {
        return em.createQuery(
            "SELECT t FROM TicketRevision t " +
            "JOIN FETCH t.usuario " +
            "JOIN FETCH t.modulo " +
            "JOIN FETCH t.tipoCambio " +
            "JOIN FETCH t.estado " +
            "ORDER BY t.fechaSolicitud DESC", TicketRevision.class)
            .getResultList();
    }

    public List<TicketRevision> listarFiltrados(String moduloId, Integer estadoId, Integer tipoCambioId) {
        String jpql =
            "SELECT t FROM TicketRevision t " +
            "JOIN FETCH t.usuario " +
            "JOIN FETCH t.modulo " +
            "JOIN FETCH t.tipoCambio " +
            "JOIN FETCH t.estado " +
            "WHERE 1=1 ";

        if (moduloId != null && !moduloId.isBlank()) jpql += " AND t.modulo.id = :moduloId ";
        if (estadoId != null) jpql += " AND t.estado.id = :estadoId ";
        if (tipoCambioId != null) jpql += " AND t.tipoCambio.id = :tipoCambioId ";

        var q = em.createQuery(jpql + " ORDER BY t.fechaSolicitud DESC", TicketRevision.class);

        if (moduloId != null && !moduloId.isBlank()) q.setParameter("moduloId", moduloId);
        if (estadoId != null) q.setParameter("estadoId", estadoId);
        if (tipoCambioId != null) q.setParameter("tipoCambioId", tipoCambioId);

        return q.getResultList();
    }

    public TicketRevision buscarPorId(UUID id) {
        return em.find(TicketRevision.class, id);
    }

    public List<Modulo> listarModulos() {
        return em.createQuery("SELECT m FROM Modulo m ORDER BY m.nombre", Modulo.class)
                 .getResultList();
    }

    public List<EstadoTicket> listarEstadosTicket() {
        return em.createQuery("SELECT e FROM EstadoTicket e ORDER BY e.nombre", EstadoTicket.class)
                 .getResultList();
    }

    public List<TipoCambio> listarTiposCambio() {
        return em.createQuery("SELECT tc FROM TipoCambio tc ORDER BY tc.nombre", TipoCambio.class)
                 .getResultList();
    }

    @Transactional
    public void cambiarEstado(UUID idTicket, Integer nuevoEstadoId) {
        TicketRevision t = em.find(TicketRevision.class, idTicket);
        if (t == null) return;
        EstadoTicket estado = em.find(EstadoTicket.class, nuevoEstadoId);
        t.setEstado(estado);
        em.merge(t);
    }

    public long contarPendientes() {
        return em.createQuery(
            "SELECT COUNT(t) FROM TicketRevision t WHERE t.estado.id = 1", Long.class)
            .getSingleResult();
    }
}
