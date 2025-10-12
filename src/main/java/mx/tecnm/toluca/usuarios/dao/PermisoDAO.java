package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.Permiso;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PermisoDAO {
    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;

    public Optional<Permiso> findById(Integer id) {
        return Optional.ofNullable(em.find(Permiso.class, id));
    }
    
    public Optional<Permiso> findByNombrePermiso(String nombrePermiso) {
         try {
            return Optional.ofNullable(em.createQuery("SELECT p FROM Permiso p WHERE p.nombrePermiso = :nombrePermiso", Permiso.class)
                    .setParameter("nombrePermiso", nombrePermiso)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<Permiso> findAll() {
        return em.createQuery("SELECT p FROM Permiso p", Permiso.class).getResultList();
    }
    // Puedes añadir save, delete, etc.
}