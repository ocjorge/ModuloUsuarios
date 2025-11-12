package mx.tecnm.toluca.usuarios.web;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import mx.tecnm.toluca.usuarios.model.Usuario;

@Named
@SessionScoped
public class SessionManager implements Serializable {

    private Usuario usuarioActual;

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean isAutenticado() {
        return usuarioActual != null;
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