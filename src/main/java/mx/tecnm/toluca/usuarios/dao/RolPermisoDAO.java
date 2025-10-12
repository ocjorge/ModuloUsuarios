package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.RolPermiso;
import mx.tecnm.toluca.usuarios.model.entity.RolPermisoPK; // PK compuesta
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RolPermisoDAO {
    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;

    public Optional<RolPermiso> findById(RolPermisoPK id) {
        return Optional.ofNullable(em.find(RolPermiso.class, id));
    }

    @Transactional
    public RolPermiso save(RolPermiso rolPermiso) {
        em.merge(rolPermiso); // Usa merge para insertar o actualizar
        return rolPermiso;
    }

    public List<RolPermiso> findAll() {
        return em.createQuery("SELECT rp FROM RolPermiso rp", RolPermiso.class).getResultList();
    }
    // Más métodos según necesidad
}