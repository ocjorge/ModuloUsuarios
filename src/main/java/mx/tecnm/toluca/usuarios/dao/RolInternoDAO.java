package mx.tecnm.toluca.usuarios.dao;
import mx.tecnm.toluca.usuarios.model.entity.RolInterno;
import jakarta.enterprise.context.ApplicationScoped; import jakarta.persistence.EntityManager; import jakarta.persistence.NoResultException; import jakarta.persistence.PersistenceContext;
import java.util.List; import java.util.Optional;
@ApplicationScoped
public class RolInternoDAO {
    @PersistenceContext(unitName = "pu_usuarios") private EntityManager em;
    public Optional<RolInterno> findByNombreRol(String nombreRol) {
        try { return Optional.ofNullable(em.createQuery("SELECT r FROM RolInterno r WHERE r.nombreRol = :nombreRol", RolInterno.class).setParameter("nombreRol", nombreRol).getSingleResult()); }
        catch (NoResultException e) { return Optional.empty(); }
    }
    public Optional<RolInterno> findById(Integer id) { return Optional.ofNullable(em.find(RolInterno.class, id)); }
    public List<RolInterno> findAll() { return em.createQuery("SELECT r FROM RolInterno r", RolInterno.class).getResultList(); }
}