package mx.tecnm.toluca.usuarios.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import mx.tecnm.toluca.usuarios.modelo.UsuarioInfo;

@Stateless // Esto convierte la clase en un EJB (Enterprise Java Bean)
public class UsuarioService {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    public List<UsuarioInfo> listarUsuarios() {
        // Hacemos una consulta JPQL (parecido a SQL pero con clases)
        return em.createQuery("SELECT u FROM UsuarioInfo u", UsuarioInfo.class).getResultList();
    }
}