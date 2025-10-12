package mx.tecnm.toluca.usuarios.model.dto;
import java.util.List; import java.util.UUID;
public class LoginResponse {
    private String status; private String message; private String token; private UUID userId; private String email;
    private String userType; private String accountStatus; private String rolInterno; private List<String> accesoModulos;
    public LoginResponse(String status, String message, String token, UUID userId, String email, String userType, String accountStatus, String rolInterno, List<String> accesoModulos) {
        this.status = status; this.message = message; this.token = token; this.userId = userId; this.email = email;
        this.userType = userType; this.accountStatus = accountStatus; this.rolInterno = rolInterno; this.accesoModulos = accesoModulos;
    }
    public String getStatus() { return status; } public String getMessage() { return message; } public String getToken() { return token; }
    public UUID getUserId() { return userId; } public String getEmail() { return email; } public String getUserType() { return userType; }
    public String getAccountStatus() { return accountStatus; } public String getRolInterno() { return rolInterno; }
    public List<String> getAccesoModulos() { return accesoModulos; }
}