package mx.tecnm.toluca.usuarios.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

import mx.tecnm.toluca.usuarios.model.AuditoriaAccion;
import mx.tecnm.toluca.usuarios.model.AuditoriaAcceso;

@ApplicationScoped
public class ReportesService {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    public List<AuditoriaAcceso> listarAccesos() {
        return em.createQuery(
                "SELECT a FROM AuditoriaAcceso a ORDER BY a.fechaHora DESC",
                AuditoriaAcceso.class
        ).getResultList();
    }

    public List<AuditoriaAccion> listarAcciones() {
        return em.createQuery(
                "SELECT a FROM AuditoriaAccion a ORDER BY a.fechaHora DESC",
                AuditoriaAccion.class
        ).getResultList();
    }
}
