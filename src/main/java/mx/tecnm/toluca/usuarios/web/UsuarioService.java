package mx.tecnm.toluca.usuarios.web;

// Alcance del bean a nivel aplicación (instancia única compartida)
import jakarta.enterprise.context.ApplicationScoped;

// Importaciones para JPA
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Importación para transacciones
import jakarta.transaction.Transactional;

// Importación de las entidades del modelo
import mx.tecnm.toluca.usuarios.model.*;

// Librería BCrypt para hash y verificación de contraseñas
import org.mindrot.jbcrypt.BCrypt;

// Importaciones para fechas, colecciones y UUID
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de usuarios.
 * Contiene lógica de autenticación y operaciones CRUD relacionadas con Usuario.
 *
 * Nota: Aunque está en el paquete web, funciona como capa de servicio
 * ya que usa EntityManager y transacciones.
 */
@ApplicationScoped
public class UsuarioService {

    /**
     * EntityManager inyectado por JPA.
     * Se utiliza para ejecutar consultas y operaciones CRUD.
     */
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    // -----------------------------
    // AUTENTICACIÓN
    // -----------------------------

    /**
     * Autentica un usuario usando username y contraseña en texto plano.
     * Busca el usuario por username (ignorando mayúsculas/minúsculas),
     * y valida la contraseña con BCrypt.
     *
     * @param username nombre de usuario ingresado
     * @param plainPassword contraseña en texto plano ingresada
     * @return Usuario autenticado si la contraseña es correcta; null si falla
     */
    public Usuario autenticar(String username, String plainPassword) {

        // Consulta al usuario por username (normalizando a minúsculas)
        List<Usuario> resultados = em.createQuery(
                "SELECT u FROM Usuario u WHERE lower(u.username) = :username", Usuario.class)
                .setParameter("username", username.toLowerCase())
                .getResultList();

        // Si no hay resultados, no existe el usuario
        if (resultados.isEmpty()) {
            return null;
        }

        // Se toma el primer resultado (username debería ser único)
        Usuario candidato = resultados.get(0);

        // Logs de depuración: muestran hash y resultado del check (solo debug)
        System.out.println("DEBUG hash BD = [" + candidato.getContrasena() + "]");
        System.out.println("DEBUG BCrypt = "
                + BCrypt.checkpw(plainPassword, candidato.getContrasena()));

        // Si la contraseña coincide con el hash almacenado
        if (BCrypt.checkpw(plainPassword, candidato.getContrasena())) {

            // Actualiza la fecha/hora de última sesión
            candidato.setUltimaSesion(OffsetDateTime.now());

            // Persistencia del cambio (sin rehashear)
            actualizar(candidato, false);

            // Retorna el usuario autenticado
            return candidato;
        }

        // Si la contraseña no coincide, autenticación fallida
        return null;
    }

    /**
     * Genera un hash BCrypt a partir de una contraseña en texto plano.
     *
     * @param plainPassword contraseña en texto plano
     * @return hash BCrypt
     */
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // -----------------------------
    // CRUD
    // -----------------------------

    /**
     * Busca un usuario por su UUID.
     *
     * @param id UUID del usuario
     * @return Usuario encontrado o null
     */
    public Usuario buscarPorId(UUID id) {
        return em.find(Usuario.class, id);
    }

    /**
     * Lista todos los usuarios del sistema, cargando relaciones principales
     * para evitar problemas de carga perezosa (lazy) en la vista.
     *
     * @return lista de usuarios ordenada por nombre completo
     */
    public List<Usuario> listarUsuarios() {
        return em.createQuery(
                "SELECT u FROM Usuario u "
                + "LEFT JOIN FETCH u.tipoUsuario "
                + "LEFT JOIN FETCH u.rolInterno "
                + "LEFT JOIN FETCH u.estadoCuenta "
                + "LEFT JOIN FETCH u.modulo ORDER BY u.nombreCompleto", Usuario.class)
                .getResultList();
    }

    /**
     * Lista todos los tipos de usuario.
     */
    public List<TipoUsuario> listarTiposUsuario() {
        return em.createQuery("SELECT t FROM TipoUsuario t ORDER BY t.nombre", TipoUsuario.class)
                .getResultList();
    }

    /**
     * Lista todos los roles internos.
     */
    public List<RolInterno> listarRoles() {
        return em.createQuery("SELECT r FROM RolInterno r ORDER BY r.nombre", RolInterno.class)
                .getResultList();
    }

    /**
     * Lista todos los estados de cuenta.
     */
    public List<EstadoCuenta> listarEstadosCuenta() {
        return em.createQuery("SELECT e FROM EstadoCuenta e ORDER BY e.nombre", EstadoCuenta.class)
                .getResultList();
    }

    /**
     * Lista todos los módulos del sistema.
     */
    public List<Modulo> listarModulos() {
        return em.createQuery("SELECT m FROM Modulo m ORDER BY m.nombre", Modulo.class)
                .getResultList();
    }

    /**
     * Guarda un nuevo usuario.
     * Hashea la contraseña y asigna fecha de creación si no existe.
     *
     * @param usuario usuario a persistir
     * @param plainPassword contraseña en texto plano
     * @return usuario persistido
     */
    @Transactional
    public Usuario guardarNuevo(Usuario usuario, String plainPassword) {

        // Asigna contraseña hasheada antes de persistir
        usuario.setContrasena(hashPassword(plainPassword));

        // Si no hay fecha de creación, se asigna ahora
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(OffsetDateTime.now());
        }

        // Inserta el usuario en la base de datos
        em.persist(usuario);

        return usuario;
    }

    /**
     * Actualiza un usuario existente.
     * Si rehashPassword es true, vuelve a hashear la contraseña contenida
     * en el campo usuario.getContrasena().
     *
     * @param usuario usuario a actualizar
     * @param rehashPassword indica si se debe rehashear la contraseña
     * @return usuario actualizado (merge)
     */
    @Transactional
    public Usuario actualizar(Usuario usuario, boolean rehashPassword) {

        // Rehashea la contraseña solo si se indicó explícitamente
        if (rehashPassword) {
            usuario.setContrasena(hashPassword(usuario.getContrasena()));
        }

        // Realiza merge para sincronizar cambios
        return em.merge(usuario);
    }

    /**
     * Elimina un usuario por UUID.
     *
     * @param id UUID del usuario a eliminar
     */
    @Transactional
    public void eliminar(UUID id) {

        // Busca el usuario antes de eliminarlo
        Usuario usuario = em.find(Usuario.class, id);

        // Si existe, se elimina
        if (usuario != null) {
            em.remove(usuario);
        }
    }

    // ===================== BUSCAR POR ID ======================

    /**
     * Busca un TipoUsuario por su id (Integer).
     *
     * @param id id del tipo de usuario
     * @return TipoUsuario o null si id es null
     */
    public TipoUsuario buscarTipoUsuarioPorId(Integer id) {
        return (id == null) ? null : em.find(TipoUsuario.class, id);
    }

    /**
     * Busca un RolInterno por su id (Integer).
     *
     * @param id id del rol
     * @return RolInterno o null si id es null
     */
    public RolInterno buscarRolPorId(Integer id) {
        return (id == null) ? null : em.find(RolInterno.class, id);
    }

    /**
     * Busca un EstadoCuenta por su id (Integer).
     *
     * @param id id del estado de cuenta
     * @return EstadoCuenta o null si id es null
     */
    public EstadoCuenta buscarEstadoCuentaPorId(Integer id) {
        return (id == null) ? null : em.find(EstadoCuenta.class, id);
    }

    /**
     * Busca un Modulo por su id (String).
     *
     * @param id identificador del módulo
     * @return Modulo encontrado (puede ser null si no existe)
     */
    public Modulo buscarModuloPorId(String id) {
        return em.find(Modulo.class, id);
    }

    /**
     * Busca un usuario por su username (ignorando mayúsculas/minúsculas).
     *
     * @param username nombre de usuario
     * @return usuario encontrado o null si no existe / parámetro inválido
     */
    public Usuario buscarPorUsername(String username) {

        // Validación básica del parámetro
        if (username == null || username.isBlank()) return null;

        // Consulta por username normalizado a minúsculas
        List<Usuario> lista = em.createQuery(
                "SELECT u FROM Usuario u WHERE LOWER(u.username) = :username",
                Usuario.class
        )
        .setParameter("username", username.toLowerCase())
        .getResultList();

        // Retorna el primer usuario si existe
        return lista.isEmpty() ? null : lista.get(0);
    }

}
