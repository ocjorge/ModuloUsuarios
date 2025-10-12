package mx.tecnm.toluca.usuarios.model.dto;
import jakarta.validation.constraints.Email; import jakarta.validation.constraints.NotBlank; import jakarta.validation.constraints.Pattern; import jakarta.validation.constraints.Size;
public class RegistroRequest {
    @NotBlank(message = "El correo electrónico es obligatorio.") @Email(message = "El formato del correo electrónico es inválido.") private String email;
    @NotBlank(message = "La contraseña es obligatoria.") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial.")
    private String password;
    @NotBlank(message = "El nombre completo es obligatorio.") private String nombreCompleto;
    @NotBlank(message = "El teléfono es obligatorio.") private String telefono;
    private String rolInicial = "Operador";
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getNombreCompleto() { return nombreCompleto; } public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getTelefono() { return telefono; } public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRolInicial() { return rolInicial; } public void setRolInicial(String rolInicial) { this.rolInicial = rolInicial; }
}