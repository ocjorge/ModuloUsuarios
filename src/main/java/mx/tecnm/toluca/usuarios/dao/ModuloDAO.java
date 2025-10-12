package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.Modulo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ModuloDAO {
    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;

    public Optional<Modulo> findByCodigoModulo(String codigoModulo) {
        try {
            return Optional.ofNullable(em.find(Modulo.class, codigoModulo));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Modulo> findAll() {
        return em.createQuery("SELECT m FROM Modulo m", Modulo.class).getResultList();
    }
    // Puedes añadir save, delete si necesitas CRUD para módulos
}