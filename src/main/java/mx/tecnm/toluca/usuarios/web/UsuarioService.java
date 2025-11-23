

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mx.tecnm.toluca.usuarios.model.*;
import org.mindrot.jbcrypt.BCrypt;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UsuarioService {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    // -----------------------------
    // AUTENTICACIÓN
    // -----------------------------
    public Usuario autenticar(String username, String plainPassword) {

        List<Usuario> resultados = em.createQuery(
                "SELECT u FROM Usuario u WHERE lower(u.username) = :username", Usuario.class)
                .setParameter("username", username.toLowerCase())
                .getResultList();

        if (resultados.isEmpty()) return null;

        Usuario candidato = resultados.get(0);

        System.out.println("DEBUG hash BD = [" + candidato.getContrasena() + "]");
        System.out.println("DEBUG BCrypt = " +
                BCrypt.checkpw(plainPassword, candidato.getContrasena()));

        if (BCrypt.checkpw(plainPassword, candidato.getContrasena())) {
            candidato.setUltimaSesion(OffsetDateTime.now());
            actualizar(candidato, false);
            return candidato;
        }

        return null;
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // -----------------------------
    // CRUD
    // -----------------------------
    public Usuario buscarPorId(UUID id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listarUsuarios() {
        return em.createQuery(
                "SELECT u FROM Usuario u " +
                        "LEFT JOIN FETCH u.tipoUsuario " +
                        "LEFT JOIN FETCH u.rolInterno " +
                        "LEFT JOIN FETCH u.estadoCuenta " +
                        "LEFT JOIN FETCH u.modulo ORDER BY u.nombreCompleto", Usuario.class)
                .getResultList();
    }

    public List<TipoUsuario> listarTiposUsuario() {
        return em.createQuery("SELECT t FROM TipoUsuario t ORDER BY t.nombre", TipoUsuario.class)
                .getResultList();
    }

    public List<RolInterno> listarRoles() {
        return em.createQuery("SELECT r FROM RolInterno r ORDER BY r.nombre", RolInterno.class)
                .getResultList();
    }

    public List<EstadoCuenta> listarEstadosCuenta() {
        return em.createQuery("SELECT e FROM EstadoCuenta e ORDER BY e.nombre", EstadoCuenta.class)
                .getResultList();
    }

    public List<Modulo> listarModulos() {
        return em.createQuery("SELECT m FROM Modulo m ORDER BY m.nombre", Modulo.class)
                .getResultList();
    }

    @Transactional
    public Usuario guardarNuevo(Usuario usuario, String plainPassword) {
        usuario.setContrasena(hashPassword(plainPassword));
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(OffsetDateTime.now());
        }
        em.persist(usuario);
        return usuario;
    }

    @Transactional
    public Usuario actualizar(Usuario usuario, boolean rehashPassword) {
        if (rehashPassword) {
            usuario.setContrasena(hashPassword(usuario.getContrasena()));
        }
        return em.merge(usuario);
    }

    @Transactional
    public void eliminar(UUID id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }
}
