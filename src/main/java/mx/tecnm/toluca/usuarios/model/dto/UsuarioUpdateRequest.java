package mx.tecnm.toluca.usuarios.model.dto;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank;
public class UsuarioUpdateRequest {
    @NotBlank(message = "El nombre completo es obligatorio.") private String nombreCompleto;
    @NotBlank(message = "El teléfono es obligatorio.") private String telefono;
    @NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "El formato del correo electrónico es inválido.") private String email;
    private Integer rolInternoId; private Integer estadoCuentaId; private String direccionEnvio;
    public String getNombreCompleto() { return nombreCompleto; } public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; } public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public Integer getRolInternoId() { return rolInternoId; } public void setRolInternoId(Integer rolInternoId) { this.rolInternoId = rolInternoId; }
    public Integer getEstadoCuentaId() { return estadoCuentaId; } public void setEstadoCuentaId(Integer estadoCuentaId) { this.estadoCuentaId = estadoCuentaId; }
    public String getDireccionEnvio() { return direccionEnvio; } public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
}