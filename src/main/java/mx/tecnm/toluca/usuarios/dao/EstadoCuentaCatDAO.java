package mx.tecnm.toluca.usuarios.dao;
import mx.tecnm.toluca.usuarios.model.entity.EstadoCuentaCat;
import jakarta.enterprise.context.ApplicationScoped; import jakarta.persistence.EntityManager; import jakarta.persistence.NoResultException; import jakarta.persistence.PersistenceContext;
import java.util.List; import java.util.Optional;
@ApplicationScoped
public class EstadoCuentaCatDAO {
    @PersistenceContext(unitName = "pu_usuarios") private EntityManager em;
    public Optional<EstadoCuentaCat> findByNombreEstado(String nombreEstado) {
        try { return Optional.ofNullable(em.createQuery("SELECT e FROM EstadoCuentaCat e WHERE e.nombreEstado = :nombreEstado", EstadoCuentaCat.class).setParameter("nombreEstado", nombreEstado).getSingleResult()); }
        catch (NoResultException e) { return Optional.empty(); }
    }
    public Optional<EstadoCuentaCat> findById(Integer id) { return Optional.ofNullable(em.find(EstadoCuentaCat.class, id)); }
    public List<EstadoCuentaCat> findAll() { return em.createQuery("SELECT e FROM EstadoCuentaCat e", EstadoCuentaCat.class).getResultList(); }
}