package mx.tecnm.toluca.usuarios.dao;
import mx.tecnm.toluca.usuarios.model.entity.TipoUsuarioCat;
import jakarta.enterprise.context.ApplicationScoped; import jakarta.persistence.EntityManager; import jakarta.persistence.NoResultException; import jakarta.persistence.PersistenceContext;
import java.util.List; import java.util.Optional;
@ApplicationScoped
public class TipoUsuarioCatDAO {
    @PersistenceContext(unitName = "pu_usuarios") private EntityManager em;
    public Optional<TipoUsuarioCat> findByNombreTipo(String nombreTipo) {
        try { return Optional.ofNullable(em.createQuery("SELECT t FROM TipoUsuarioCat t WHERE t.nombreTipo = :nombreTipo", TipoUsuarioCat.class).setParameter("nombreTipo", nombreTipo).getSingleResult()); }
        catch (NoResultException e) { return Optional.empty(); }
    }
    public List<TipoUsuarioCat> findAll() { return em.createQuery("SELECT t FROM TipoUsuarioCat t", TipoUsuarioCat.class).getResultList(); }
}