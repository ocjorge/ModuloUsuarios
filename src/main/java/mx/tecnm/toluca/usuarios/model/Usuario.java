package mx.tecnm.toluca.usuarios.model;

// Importaciones necesarias para JPA
import jakarta.persistence.*;

// Importaciones para serialización, fechas y UUID
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidad JPA que representa la tabla "usuarios".
 * Se utiliza para almacenar la información principal
 * de los usuarios del sistema.
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    /**
     * Identificador único del usuario.
     * Se genera automáticamente y se maneja como UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario", columnDefinition = "uuid")
    private UUID id;

    /*
     * Identificador de control del usuario.
     * Este campo se obtiene directamente de la base de datos
     * y no puede ser insertado ni actualizado desde JPA.
     */
//    @Column(name="id_control")
// private Integer idControl;
    
    @Column(name = "id_control", insertable = false, updatable = false)
    private Integer idControl;

    /**
     * Correo electrónico del usuario.
     * Debe ser único y no permitir valores nulos.
     */
    @Column(name = "correo_electronico", nullable = false, unique = true)
    private String correoElectronico;

    /**
     * Contraseña del usuario.
     * Se almacena en la base de datos como texto.
     */
    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    /**
     * Nombre completo del usuario.
     * No permite valores nulos.
     */
    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    /**
     * Teléfono del usuario.
     * Campo opcional.
     */
    @Column(name = "telefono")
    private String telefono;

    /**
     * Dirección del usuario.
     * Campo opcional.
     */
    @Column(name = "direccion")
    private String direccion;

    /**
     * Fecha y hora de la última sesión del usuario.
     */
    @Column(name = "ultima_sesion")
    private OffsetDateTime ultimaSesion;

    /**
     * Fecha y hora de creación del usuario.
     */
    @Column(name = "fecha_creacion")
    private OffsetDateTime fechaCreacion;

    /**
     * Tipo de usuario (administrador, usuario normal, etc.).
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    /**
     * Rol interno asignado al usuario.
     * Relación ManyToOne opcional.
     */
    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RolInterno rolInterno;

    /**
     * Estado actual de la cuenta del usuario.
     * Relación ManyToOne obligatoria.
     */
    @ManyToOne
    @JoinColumn(name = "id_estado_cuenta", nullable = false)
    private EstadoCuenta estadoCuenta;

    /**
     * Módulo al que pertenece el usuario.
     * Relación ManyToOne opcional.
     */
    @ManyToOne
    @JoinColumn(name = "id_modulo")
    private Modulo modulo;

    /**
     * Nombre de usuario para autenticación.
     * Debe ser único.
     */
    @Column(name = "username", unique = true)
    private String username;

    /**
     * Obtiene el identificador del usuario.
     * @return UUID del usuario
     */
    public UUID getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     * @param id UUID del usuario
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador de control del usuario.
     * @return id de control
     */
    public Integer getIdControl() {
        return idControl;
    }

    /**
     * Establece el identificador de control del usuario.
     * @param idControl id de control
     */
    public void setIdControl(Integer idControl) {
        this.idControl = idControl;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * @return correo electrónico
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Establece el correo electrónico del usuario.
     * @param correoElectronico correo electrónico
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * Obtiene la contraseña del usuario.
     * @return contraseña
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña del usuario.
     * @param contrasena contraseña
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el nombre completo del usuario.
     * @return nombre completo
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el nombre completo del usuario.
     * @param nombreCompleto nombre completo
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Obtiene el teléfono del usuario.
     * @return teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del usuario.
     * @param telefono teléfono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del usuario.
     * @return dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     * @param direccion dirección
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la fecha de la última sesión.
     * @return fecha de última sesión
     */
    public OffsetDateTime getUltimaSesion() {
        return ultimaSesion;
    }

    /**
     * Establece la fecha de la última sesión.
     * @param ultimaSesion fecha de última sesión
     */
    public void setUltimaSesion(OffsetDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    /**
     * Obtiene la fecha de creación del usuario.
     * @return fecha de creación
     */
    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha de creación del usuario.
     * @param fechaCreacion fecha de creación
     */
    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el tipo de usuario.
     * @return tipo de usuario
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Establece el tipo de usuario.
     * @param tipoUsuario tipo de usuario
     */
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene el rol interno del usuario.
     * @return rol interno
     */
    public RolInterno getRolInterno() {
        return rolInterno;
    }

    /**
     * Establece el rol interno del usuario.
     * @param rolInterno rol interno
     */
    public void setRolInterno(RolInterno rolInterno) {
        this.rolInterno = rolInterno;
    }

    /**
     * Obtiene el estado de la cuenta del usuario.
     * @return estado de cuenta
     */
    public EstadoCuenta getEstadoCuenta() {
        return estadoCuenta;
    }

    /**
     * Establece el estado de la cuenta del usuario.
     * @param estadoCuenta estado de cuenta
     */
    public void setEstadoCuenta(EstadoCuenta estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    /**
     * Obtiene el módulo asociado al usuario.
     * @return módulo
     */
    public Modulo getModulo() {
        return modulo;
    }

    /**
     * Establece el módulo asociado al usuario.
     * @param modulo módulo
     */
    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sobrescritura del método equals.
     * Permite comparar dos objetos Usuario
     * basándose en su identificador.
     */
    @Override
    public boolean equals(Object o) {

        // Verifica si ambas referencias apuntan al mismo objeto
        if (this == o) return true;

        // Verifica si el objeto es nulo o de otra clase
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión del objeto para comparación
        Usuario usuario = (Usuario) o;

        // Compara los identificadores si no son nulos
        return id != null && id.equals(usuario.id);
    }

    /**
     * Sobrescritura del método hashCode.
     * Mantiene consistencia con el método equals.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
