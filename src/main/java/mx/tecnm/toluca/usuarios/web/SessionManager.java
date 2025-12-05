package mx.tecnm.toluca.usuarios.web;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import mx.tecnm.toluca.usuarios.model.Usuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@Named
@SessionScoped
public class SessionManager implements Serializable {

    private UUID usuarioId;

    private Usuario usuarioActual;

    @Inject
    private UsuarioService usuarioService;

    public void establecerSesion(Usuario usuario) {
        this.usuarioId = usuario.getId();
        this.usuarioActual = usuario;
    }

    public Usuario getUsuarioActual() {
        if (usuarioId == null) return null;

        // Recarga cada vez que se usa -> evita datos viejos
        usuarioActual = usuarioService.buscarPorId(usuarioId);

        return usuarioActual;
    }

    public boolean isAutenticado() {
        return usuarioId != null;
    }

    public void cerrarSesion() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
    }

    public void redirigirSiNoAutenticado() throws IOException {
        if (!isAutenticado()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        }
    }

    public void redirigirSiAutenticado() throws IOException {
        if (isAutenticado()) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("usuarios.xhtml");
        }
    }
}
