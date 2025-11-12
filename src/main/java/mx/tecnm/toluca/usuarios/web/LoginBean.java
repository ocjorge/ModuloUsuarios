package mx.tecnm.toluca.usuarios.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import mx.tecnm.toluca.usuarios.model.Usuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@Named
@RequestScoped
public class LoginBean {

    private String username;
    private String password;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private SessionManager sessionManager;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() throws IOException {
        Usuario usuario = usuarioService.autenticar(username, password);
        if (usuario != null) {
            sessionManager.setUsuarioActual(usuario);
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuarios.xhtml");
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Credenciales inválidas", ""));
        }
    }
}