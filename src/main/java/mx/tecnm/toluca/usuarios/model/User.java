package mx.tecnm.toluca.usuarios.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String email;
    private String module;
    private String phone;
    private String status;
    private String role;
    private String regDate;
    private String lastSession;

    public User() {}

    public User(int id, String name, String email, String module, String phone, 
                String status, String role, String regDate, String lastSession) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.module = module;
        this.phone = phone;
        this.status = status;
        this.role = role;
        this.regDate = regDate;
        this.lastSession = lastSession;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
    public String getLastSession() { return lastSession; }
    public void setLastSession(String lastSession) { this.lastSession = lastSession; }
}