package mx.tecnm.toluca.usuarios.converter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import mx.tecnm.toluca.usuarios.service.UsuarioService;
import java.util.UUID;

@FacesConverter(value = "entityByIdConverter", managed = true)
@ApplicationScoped
public class EntityByIdConverter implements Converter<Object> {

    @Inject
    private UsuarioService usuarioService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String id) {
        if (id == null || id.isBlank()) return null;

        try {
            return usuarioService.buscarEntidadGenerica(UUID.fromString(id));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object entity) {
        if (entity == null) return "";
        try {
            var method = entity.getClass().getMethod("getId");
            Object value = method.invoke(entity);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
    }
}
