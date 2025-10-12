package mx.tecnm.toluca.usuarios.model.dto;
import java.util.UUID;
public class RegistroResponse {
    private String status; private String message; private UUID userId; private String email;
    public RegistroResponse(String status, String message, UUID userId, String email) { this.status = status; this.message = message; this.userId = userId; this.email = email; }
    public String getStatus() { return status; } public String getMessage() { return message; }
    public UUID getUserId() { return userId; } public String getEmail() { return email; }
}