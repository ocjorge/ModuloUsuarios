package mx.tecnm.toluca.usuarios.web.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import mx.tecnm.toluca.usuarios.model.TipoUsuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

@FacesConverter(value = "tipoUsuarioConverter", managed = true)
public class TipoUsuarioConverter implements Converter<TipoUsuario> {

    @Inject
    private UsuarioService usuarioService;

    @Override
    public TipoUsuario getAsObject(FacesContext fc, UIComponent uic, String id) {
        if (id == null || id.isBlank()) return null;
        return usuarioService.buscarTipoUsuarioPorId(Integer.valueOf(id));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, TipoUsuario obj) {
        return obj != null ? obj.getId().toString() : "";
    }
}
