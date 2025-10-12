package mx.tecnm.toluca.usuarios.model.dto;
import java.time.LocalDateTime; import java.util.List; import java.util.UUID;
public class UsuarioDTO {
    private UUID idUsuario; private String email; private String nombreCompleto; private String telefono;
    private String tipoUsuario; private String estadoCuenta; private String rolInterno;
    private LocalDateTime ultimaSesion; private LocalDateTime fechaCreacion; private String direccionEnvio; private String dominioEmpresa;
    private List<String> accesoModulos;
    public UsuarioDTO() {}
    public UsuarioDTO(UUID idUsuario, String email, String nombreCompleto, String telefono, String tipoUsuario, String estadoCuenta, String rolInterno, LocalDateTime ultimaSesion, LocalDateTime fechaCreacion, String direccionEnvio, String dominioEmpresa, List<String> accesoModulos) {
        this.idUsuario = idUsuario; this.email = email; this.nombreCompleto = nombreCompleto; this.telefono = telefono;
        this.tipoUsuario = tipoUsuario; this.estadoCuenta = estadoCuenta; this.rolInterno = rolInterno;
        this.ultimaSesion = ultimaSesion; this.fechaCreacion = fechaCreacion; this.direccionEnvio = direccionEnvio; this.dominioEmpresa = dominioEmpresa;
        this.accesoModulos = accesoModulos;
    }
    public UUID getIdUsuario() { return idUsuario; } public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getNombreCompleto() { return nombreCompleto; } public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; } public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTipoUsuario() { return tipoUsuario; } public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public String getEstadoCuenta() { return estadoCuenta; } public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
    public String getRolInterno() { return rolInterno; } public void setRolInterno(String rolInterno) { this.rolInterno = rolInterno; }
    public LocalDateTime getUltimaSesion() { return ultimaSesion; } public void setUltimaSesion(LocalDateTime ultimaSesion) { this.ultimaSesion = ultimaSesion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; } public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getDireccionEnvio() { return direccionEnvio; } public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public String getDominioEmpresa() { return dominioEmpresa; } public void setDominioEmpresa(String dominioEmpresa) { this.dominioEmpresa = dominioEmpresa; }
    public List<String> getAccesoModulos() { return accesoModulos; } public void setAccesoModulos(List<String> accesoModulos) { this.accesoModulos = accesoModulos; }
}