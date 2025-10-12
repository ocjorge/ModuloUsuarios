package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.AccionClaveCat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AccionClaveCatDAO {
    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;
    
    public Optional<AccionClaveCat> findById(Integer id) {
        return Optional.ofNullable(em.find(AccionClaveCat.class, id));
    }
    
    public Optional<AccionClaveCat> findByNombreAccionClave(String nombreAccionClave) {
        try {
            return Optional.ofNullable(em.createQuery("SELECT a FROM AccionClaveCat a WHERE a.nombreAccionClave = :nombreAccionClave", AccionClaveCat.class)
                    .setParameter("nombreAccionClave", nombreAccionClave)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public List<AccionClaveCat> findAll() {
        return em.createQuery("SELECT a FROM AccionClaveCat a", AccionClaveCat.class).getResultList();
    }
    // agregar save, delete, etc. si necesitas CRUD para acciones clave
}