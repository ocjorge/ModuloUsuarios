package mx.tecnm.toluca.usuarios.dao;

import mx.tecnm.toluca.usuarios.model.entity.Usuario;
import mx.tecnm.toluca.usuarios.model.entity.EstadoCuentaCat; // Necesario para filtros
import mx.tecnm.toluca.usuarios.model.entity.RolInterno; // Necesario para filtros
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UsuarioDAO {

    @PersistenceContext(unitName = "pu_usuarios")
    private EntityManager em;

    public Optional<Usuario> findByEmail(String email) {
        try {
            return Optional.ofNullable(em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public Optional<Usuario> findById(UUID id) {
        return Optional.ofNullable(em.find(Usuario.class, id));
    }

    @Transactional
    public Usuario save(Usuario usuario) {
        if (usuario.getIdUsuario() == null || em.find(Usuario.class, usuario.getIdUsuario()) == null) {
            em.persist(usuario); // INSERT
        } else {
            em.merge(usuario); // UPDATE
        }
        return usuario;
    }

    @Transactional
    public void delete(Usuario usuario) {
        if (em.contains(usuario)) {
            em.remove(usuario);
        } else {
            em.remove(em.merge(usuario));
        }
    }

    public List<Usuario> findAll() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    // --- Métodos para paginación y filtrado (RF-048, RF-049, RF-050, RF-051) ---
    public List<Usuario> findAllPaginatedFiltered(int page, int size, String sortBy, String sortDir, String nombreCompleto, String email, String estadoCuenta, String rolInterno) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT u FROM Usuario u JOIN u.tipoUsuario t JOIN u.estadoCuenta e LEFT JOIN u.rolInterno r");
        List<String> conditions = new ArrayList<>();

        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            conditions.add("LOWER(u.nombreCompleto) LIKE :nombreCompleto");
        }
        if (email != null && !email.isEmpty()) {
            conditions.add("LOWER(u.email) LIKE :email");
        }
        if (estadoCuenta != null && !estadoCuenta.isEmpty()) {
            conditions.add("LOWER(e.nombreEstado) = :estadoCuenta");
        }
        if (rolInterno != null && !rolInterno.isEmpty()) {
            conditions.add("LOWER(r.nombreRol) = :rolInterno");
        }

        if (!conditions.isEmpty()) {
            jpqlBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        jpqlBuilder.append(" ORDER BY ");
        // RF-051: Ordenación
        switch (sortBy) {
            case "nombreCompleto": jpqlBuilder.append("u.nombreCompleto"); break;
            case "email": jpqlBuilder.append("u.email"); break;
            case "estadoCuenta": jpqlBuilder.append("e.nombreEstado"); break;
            case "rolInterno": jpqlBuilder.append("r.nombreRol"); break;
            case "ultimaSesion": jpqlBuilder.append("u.ultimaSesion"); break;
            case "fechaCreacion": jpqlBuilder.append("u.fechaCreacion"); break;
            default: jpqlBuilder.append("u.nombreCompleto"); break; // Default sort
        }
        jpqlBuilder.append(" ").append(sortDir.equalsIgnoreCase("desc") ? "DESC" : "ASC");

        TypedQuery<Usuario> query = em.createQuery(jpqlBuilder.toString(), Usuario.class);

        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            query.setParameter("nombreCompleto", "%" + nombreCompleto.toLowerCase() + "%");
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", "%" + email.toLowerCase() + "%");
        }
        if (estadoCuenta != null && !estadoCuenta.isEmpty()) {
            query.setParameter("estadoCuenta", estadoCuenta.toLowerCase());
        }
        if (rolInterno != null && !rolInterno.isEmpty()) {
            query.setParameter("rolInterno", rolInterno.toLowerCase());
        }

        query.setFirstResult(page * size); // Paginación
        query.setMaxResults(size); // Paginación

        return query.getResultList();
    }

    public long countAllFiltered(String nombreCompleto, String email, String estadoCuenta, String rolInterno) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT COUNT(u) FROM Usuario u JOIN u.tipoUsuario t JOIN u.estadoCuenta e LEFT JOIN u.rolInterno r");
        List<String> conditions = new ArrayList<>();

        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            conditions.add("LOWER(u.nombreCompleto) LIKE :nombreCompleto");
        }
        if (email != null && !email.isEmpty()) {
            conditions.add("LOWER(u.email) LIKE :email");
        }
        if (estadoCuenta != null && !estadoCuenta.isEmpty()) {
            conditions.add("LOWER(e.nombreEstado) = :estadoCuenta");
        }
        if (rolInterno != null && !rolInterno.isEmpty()) {
            conditions.add("LOWER(r.nombreRol) = :rolInterno");
        }

        if (!conditions.isEmpty()) {
            jpqlBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        TypedQuery<Long> query = em.createQuery(jpqlBuilder.toString(), Long.class);

        if (nombreCompleto != null && !nombreCompleto.isEmpty()) {
            query.setParameter("nombreCompleto", "%" + nombreCompleto.toLowerCase() + "%");
        }
        if (email != null && !email.isEmpty()) {
            query.setParameter("email", "%" + email.toLowerCase() + "%");
        }
        if (estadoCuenta != null && !estadoCuenta.isEmpty()) {
            query.setParameter("estadoCuenta", estadoCuenta.toLowerCase());
        }
        if (rolInterno != null && !rolInterno.isEmpty()) {
            query.setParameter("rolInterno", rolInterno.toLowerCase());
        }

        return query.getSingleResult();
    }
}