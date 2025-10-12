package mx.tecnm.toluca.usuarios.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    // PostgreSQL puede generar UUIDs por defecto (gen_random_uuid()), o JPA puede generarlos.
    // Si PostgreSQL lo genera, no se necesita @GeneratedValue(generator = "UUID").
    // Para JPA 3.0+, GenerationType.UUID a veces es soportado. Si no, usa @PrePersist.
    @GeneratedValue(generator = "UUID") // O usa GenerationType.UUID si tu JPA lo soporta directamente.
    @Column(name = "id_usuario", updatable = false, nullable = false)
    private UUID idUsuario;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_usuario", nullable = false)
    private TipoUsuarioCat tipoUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_cuenta", nullable = false)
    private EstadoCuentaCat estadoCuenta;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_ultima_actualizacion")
    private LocalDateTime fechaUltimaActualizacion;

    @Column(name = "ultima_sesion")
    private LocalDateTime ultimaSesion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_interno_id")
    private RolInterno rolInterno;

    @Column(name = "direccion_envio", columnDefinition = "jsonb")
    private String direccionEnvio;

    @Column(name = "id_entidad_externa", unique = true)
    private String idEntidadExterna;

    @Column(name = "dominio_empresa")
    private String dominioEmpresa;

    public Usuario() {
        // No inicializar fechas aquí si @PrePersist se encarga de ellas.
        // Para PostgreSQL con DEFAULT NOW() en DDL, no necesitas inicializar.
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.idUsuario == null) {
            this.idUsuario = UUID.randomUUID(); // Asegura UUID si no es generado por DB default
        }
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.fechaUltimaActualizacion == null) {
            this.fechaUltimaActualizacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaUltimaActualizacion = LocalDateTime.now();
    }

    // --- Constructores manuales ---
    public Usuario(String email, String passwordHash, String nombreCompleto, String telefono,
                   TipoUsuarioCat tipoUsuario, EstadoCuentaCat estadoCuenta, RolInterno rolInterno, String dominioEmpresa) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.estadoCuenta = estadoCuenta;
        this.rolInterno = rolInterno;
        this.dominioEmpresa = dominioEmpresa;
    }
    // Constructor para registro externo (proveedor)
    public Usuario(String email, String passwordHash, String nombreCompleto, String telefono,
                   TipoUsuarioCat tipoUsuario, EstadoCuentaCat estadoCuenta, String idEntidadExterna) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.estadoCuenta = estadoCuenta;
        this.idEntidadExterna = idEntidadExterna;
    }
    // Constructor para registro externo (cliente con dirección)
    public Usuario(String email, String passwordHash, String nombreCompleto, String telefono,
                   TipoUsuarioCat tipoUsuario, EstadoCuentaCat estadoCuenta, String idEntidadExterna, String direccionEnvio) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
        this.estadoCuenta = estadoCuenta;
        this.idEntidadExterna = idEntidadExterna;
        this.direccionEnvio = direccionEnvio;
    }
    
    // --- Getters y Setters ---
    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public TipoUsuarioCat getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuarioCat tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public EstadoCuentaCat getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(EstadoCuentaCat estadoCuenta) { this.estadoCuenta = estadoCuenta; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaUltimaActualizacion() { return fechaUltimaActualizacion; }
    public void setFechaUltimaActualizacion(LocalDateTime fechaUltimaActualizacion) { this.fechaUltimaActualizacion = fechaUltimaActualizacion; }
    public LocalDateTime getUltimaSesion() { return ultimaSesion; }
    public void setUltimaSesion(LocalDateTime ultimaSesion) { this.ultimaSesion = ultimaSesion; }
    public RolInterno getRolInterno() { return rolInterno; }
    public void setRolInterno(RolInterno rolInterno) { this.rolInterno = rolInterno; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public String getIdEntidadExterna() { return idEntidadExterna; }
    public void setIdEntidadExterna(String idEntidadExterna) { this.idEntidadExterna = idEntidadExterna; }
    public String getDominioEmpresa() { return dominioEmpresa; }
    public void setDominioEmpresa(String dominioEmpresa) { this.dominioEmpresa = dominioEmpresa; }
}