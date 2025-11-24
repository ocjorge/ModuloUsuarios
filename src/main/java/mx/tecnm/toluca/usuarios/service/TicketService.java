package mx.tecnm.toluca.usuarios.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.UUID;

import mx.tecnm.toluca.usuarios.model.TicketRevision;

@ApplicationScoped
public class TicketService {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    public TicketRevision buscarPorId(UUID id) {
        return em.find(TicketRevision.class, id);
    }

    public List<TicketRevision> listarTickets(String modulo, Integer estado, Integer tipoCambio) {

        String sql = """
            SELECT t FROM TicketRevision t
            WHERE (:modulo IS NULL OR t.modulo.id = :modulo)
              AND (:estado IS NULL OR t.estado.id = :estado)
              AND (:tipo IS NULL OR t.tipoCambio.id = :tipo)
            ORDER BY t.fechaSolicitud DESC
            """;

        return em.createQuery(sql, TicketRevision.class)
                .setParameter("modulo", modulo)
                .setParameter("estado", estado)
                .setParameter("tipo", tipoCambio)
                .getResultList();
    }
}
