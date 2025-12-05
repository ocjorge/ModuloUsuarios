package mx.tecnm.toluca.usuarios.service;

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

    // ----------- AUTENTICACIÓN -----------------
    public Usuario autenticar(String username, String plainPassword) {

    if (username == null || plainPassword == null) return null;

    username = username.trim().toLowerCase();
    plainPassword = plainPassword.trim();

  List<Usuario> resultados = em.createQuery(
        "SELECT u FROM Usuario u " +
        "LEFT JOIN FETCH u.modulo m " +
        "WHERE lower(u.username) = :username " +
        "AND m.id = 'USR' " +        // 👈 espacio al final
        "AND u.estadoCuenta.id = 1", // 👈 estado activo
        Usuario.class)
    .setParameter("username", username)
    .getResultList();


    if (resultados.isEmpty()) {
        System.out.println(">>> Usuario no pertenece al módulo USR");
        return null;
    }

    Usuario candidato = resultados.get(0);

    if (candidato.getContrasena() == null) return null;

    boolean ok = BCrypt.checkpw(plainPassword, candidato.getContrasena());

    System.out.println(">>> LOGIN BCrypt.checkpw = " + ok);

    if (ok) {
        candidato.setUltimaSesion(OffsetDateTime.now());
        return candidato;
    }

    return null;
}


    public String debugHash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // ---------------- BUSCAR --------------------
    public Usuario buscarPorId(UUID id) {
        return em.find(Usuario.class, id);
    }

    public TipoUsuario buscarTipoUsuarioPorId(UUID id) {
        return id == null ? null : em.find(TipoUsuario.class, id);
    }

    public RolInterno buscarRolPorId(UUID id) {
        return id == null ? null : em.find(RolInterno.class, id);
    }

    public EstadoCuenta buscarEstadoCuentaPorId(UUID id) {
        return id == null ? null : em.find(EstadoCuenta.class, id);
    }

    public Modulo buscarModuloPorId(String id) {
        return id == null ? null : em.find(Modulo.class, id);
    }

    // ---------------- LISTADOS --------------------
    public List<Usuario> listarUsuarios() {
        return em.createQuery(
                "SELECT u FROM Usuario u "
                + "LEFT JOIN FETCH u.tipoUsuario "
                + "LEFT JOIN FETCH u.rolInterno "
                + "LEFT JOIN FETCH u.estadoCuenta "
                + "LEFT JOIN FETCH u.modulo "
                + "ORDER BY u.nombreCompleto",
                Usuario.class
        ).getResultList();
    }

    public List<TipoUsuario> listarTiposUsuario() {
        return em.createQuery(
                "SELECT t FROM TipoUsuario t ORDER BY t.nombre",
                TipoUsuario.class
        ).getResultList();
    }

    public List<RolInterno> listarRoles() {
        return em.createQuery(
                "SELECT r FROM RolInterno r ORDER BY r.nombre",
                RolInterno.class
        ).getResultList();
    }

    public List<EstadoCuenta> listarEstadosCuenta() {
        return em.createQuery(
                "SELECT e FROM EstadoCuenta e ORDER BY e.nombre",
                EstadoCuenta.class
        ).getResultList();
    }

    public List<Modulo> listarModulos() {
        return em.createQuery(
                "SELECT m FROM Modulo m ORDER BY m.nombre",
                Modulo.class
        ).getResultList();
    }

    // ---------------- GUARDA / ACTUALIZA --------------------
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

    // ---------------- ELIMINAR --------------------
    @Transactional
    public void eliminar(UUID id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    public Object buscarEntidadGenerica(UUID id) {
        return em.find(Object.class, id);
    }

    public TipoUsuario buscarTipoUsuarioPorId(Integer id) {
        return em.find(TipoUsuario.class, id);
    }

    public RolInterno buscarRolPorId(Integer id) {
        return em.find(RolInterno.class, id);
    }

    public EstadoCuenta buscarEstadoCuentaPorId(Integer id) {
        return em.find(EstadoCuenta.class, id);
    }

    public Modulo buscarModuloPorId(Integer id) {
        return em.find(Modulo.class, id);
    }

   
    public Usuario buscarPorUsername(String username) {
        if (username == null) {
            return null;
        }

        List<Usuario> lista = em.createQuery(
                "SELECT u FROM Usuario u WHERE lower(u.username) = :u",
                Usuario.class)
                .setParameter("u", username.toLowerCase())
                .getResultList();

        return lista.isEmpty() ? null : lista.get(0);
    }

}
