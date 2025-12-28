package mx.tecnm.toluca.usuarios.service;

// Alcance del servicio a nivel aplicación
import jakarta.enterprise.context.ApplicationScoped;

// Importaciones para JPA
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Importación para manejo de transacciones
import jakarta.transaction.Transactional;

// Importación de entidades del modelo
import mx.tecnm.toluca.usuarios.model.*;

// Importación para cifrado de contraseñas con BCrypt
import org.mindrot.jbcrypt.BCrypt;

// Importaciones para fechas, colecciones y UUID
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de la gestión de usuarios.
 * Incluye autenticación, búsquedas, listados,
 * alta, actualización y eliminación de usuarios.
 */
@ApplicationScoped
public class UsuarioService {

    /**
     * EntityManager inyectado para interactuar
     * con la base de datos mediante JPA.
     */
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    // ----------- AUTENTICACIÓN -----------------

    /**
     * Autentica a un usuario a partir de su username y contraseña en texto plano.
     * Realiza validaciones, consulta el usuario activo del módulo USR
     * y compara la contraseña usando BCrypt.
     *
     * @param username nombre de usuario
     * @param plainPassword contraseña en texto plano
     * @return Usuario autenticado o null si falla la autenticación
     */
    public Usuario autenticar(String username, String plainPassword) {

        // Validación básica de parámetros
        if (username == null || plainPassword == null) return null;

        // Normaliza username y contraseña
        username = username.trim().toLowerCase();
        plainPassword = plainPassword.trim();

        // Consulta del usuario con JOIN al módulo
        List<Usuario> resultados = em.createQuery(
                "SELECT u FROM Usuario u " +
                "LEFT JOIN FETCH u.modulo m " +
                "WHERE lower(u.username) = :username " +
                "AND m.id = 'USR' " +        // Módulo permitido
                "AND u.estadoCuenta.id = 1", // Estado activo
                Usuario.class)
            .setParameter("username", username)
            .getResultList();

        // Si no hay resultados, el usuario no pertenece al módulo
        if (resultados.isEmpty()) {
            System.out.println(">>> Usuario no pertenece al módulo USR");
            return null;
        }

        // Se toma el primer resultado
        Usuario candidato = resultados.get(0);

        // Si no tiene contraseña, no se puede autenticar
        if (candidato.getContrasena() == null) return null;

        // Verifica la contraseña usando BCrypt
        boolean ok = BCrypt.checkpw(plainPassword, candidato.getContrasena());

        System.out.println(">>> LOGIN BCrypt.checkpw = " + ok);

        // Si la contraseña es correcta, actualiza última sesión
        if (ok) {
            candidato.setUltimaSesion(OffsetDateTime.now());
            return candidato;
        }

        return null;
    }

    /**
     * Método auxiliar para generar un hash BCrypt
     * a partir de una contraseña en texto plano.
     *
     * @param plain contraseña en texto plano
     * @return hash BCrypt
     */
    public String debugHash(String plain) {
        return BCrypt.hashpw(plain, BCrypt.gensalt());
    }

    /**
     * Genera el hash BCrypt de una contraseña.
     *
     * @param plainPassword contraseña en texto plano
     * @return contraseña cifrada
     */
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // ---------------- BUSCAR --------------------

    /**
     * Busca un usuario por su UUID.
     *
     * @param id identificador del usuario
     * @return usuario o null si no existe
     */
    public Usuario buscarPorId(UUID id) {
        return em.find(Usuario.class, id);
    }

    /**
     * Busca un tipo de usuario por UUID.
     */
    public TipoUsuario buscarTipoUsuarioPorId(UUID id) {
        return id == null ? null : em.find(TipoUsuario.class, id);
    }

    /**
     * Busca un rol interno por UUID.
     */
    public RolInterno buscarRolPorId(UUID id) {
        return id == null ? null : em.find(RolInterno.class, id);
    }

    /**
     * Busca un estado de cuenta por UUID.
     */
    public EstadoCuenta buscarEstadoCuentaPorId(UUID id) {
        return id == null ? null : em.find(EstadoCuenta.class, id);
    }

    /**
     * Busca un módulo por su identificador String.
     */
    public Modulo buscarModuloPorId(String id) {
        return id == null ? null : em.find(Modulo.class, id);
    }

    // ---------------- LISTADOS --------------------

    /**
     * Lista todos los usuarios del sistema,
     * cargando sus relaciones principales con JOIN FETCH.
     *
     * @return lista de usuarios ordenada por nombre completo
     */
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

    /**
     * Lista todos los tipos de usuario.
     */
    public List<TipoUsuario> listarTiposUsuario() {
        return em.createQuery(
                "SELECT t FROM TipoUsuario t ORDER BY t.nombre",
                TipoUsuario.class
        ).getResultList();
    }

    /**
     * Lista todos los roles internos.
     */
    public List<RolInterno> listarRoles() {
        return em.createQuery(
                "SELECT r FROM RolInterno r ORDER BY r.nombre",
                RolInterno.class
        ).getResultList();
    }

    /**
     * Lista todos los estados de cuenta.
     */
    public List<EstadoCuenta> listarEstadosCuenta() {
        return em.createQuery(
                "SELECT e FROM EstadoCuenta e ORDER BY e.nombre",
                EstadoCuenta.class
        ).getResultList();
    }

    /**
     * Lista todos los módulos del sistema.
     */
    public List<Modulo> listarModulos() {
        return em.createQuery(
                "SELECT m FROM Modulo m ORDER BY m.nombre",
                Modulo.class
        ).getResultList();
    }

    // ---------------- GUARDA / ACTUALIZA --------------------

    /**
     * Guarda un nuevo usuario en la base de datos.
     * Genera el hash de la contraseña y asigna la fecha de creación.
     *
     * @param usuario usuario a guardar
     * @param plainPassword contraseña en texto plano
     * @return usuario persistido
     */
    @Transactional
    public Usuario guardarNuevo(Usuario usuario, String plainPassword) {

        // Genera y asigna el hash de la contraseña
        usuario.setContrasena(hashPassword(plainPassword));

        // Asigna fecha de creación si no existe
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(OffsetDateTime.now());
        }

        em.persist(usuario);
        return usuario;
    }

    /**
     * Actualiza un usuario existente.
     * Permite rehashear la contraseña si se indica.
     *
     * @param usuario usuario a actualizar
     * @param rehashPassword indica si se debe volver a cifrar la contraseña
     * @return usuario actualizado
     */
    @Transactional
    public Usuario actualizar(Usuario usuario, boolean rehashPassword) {

        if (rehashPassword) {
            usuario.setContrasena(hashPassword(usuario.getContrasena()));
        }

        return em.merge(usuario);
    }

    // ---------------- ELIMINAR --------------------

    /**
     * Elimina un usuario por su UUID.
     *
     * @param id identificador del usuario
     */
    @Transactional
    public void eliminar(UUID id) {
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    /**
     * Búsqueda genérica de entidad por UUID.
     */
    public Object buscarEntidadGenerica(UUID id) {
        return em.find(Object.class, id);
    }

    /**
     * Búsqueda de tipo de usuario por Integer.
     */
    public TipoUsuario buscarTipoUsuarioPorId(Integer id) {
        return em.find(TipoUsuario.class, id);
    }

    /**
     * Búsqueda de rol interno por Integer.
     */
    public RolInterno buscarRolPorId(Integer id) {
        return em.find(RolInterno.class, id);
    }

    /**
     * Búsqueda de estado de cuenta por Integer.
     */
    public EstadoCuenta buscarEstadoCuentaPorId(Integer id) {
        return em.find(EstadoCuenta.class, id);
    }

    /**
     * Búsqueda de módulo por Integer.
     */
    public Modulo buscarModuloPorId(Integer id) {
        return em.find(Modulo.class, id);
    }

    /**
     * Busca un usuario por su username.
     *
     * @param username nombre de usuario
     * @return usuario encontrado o null
     */
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
