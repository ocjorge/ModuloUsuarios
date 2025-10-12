package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.UsuarioPermiso;
import mx.tecnm.toluca.usuarios.model.entity.UsuarioPermisoPK; // PK compuesta
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioPermisoDAO {
    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;

    public Optional<UsuarioPermiso> findById(UsuarioPermisoPK id) {
        return Optional.ofNullable(em.find(UsuarioPermiso.class, id));
    }

    @Transactional
    public UsuarioPermiso save(UsuarioPermiso usuarioPermiso) {
        em.merge(usuarioPermiso);
        return usuarioPermiso;
    }

    public List<UsuarioPermiso> findAll() {
        return em.createQuery("SELECT up FROM UsuarioPermiso up", UsuarioPermiso.class).getResultList();
    }
    // Más métodos según necesidad
}