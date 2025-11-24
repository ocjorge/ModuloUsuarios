package mx.tecnm.toluca.usuarios.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import mx.tecnm.toluca.usuarios.model.*;

@FacesConverter(value = "entityByIdConverter", managed = true)
public class EntityByIdConverter implements Converter<Object> {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;

        try {
            return java.util.UUID.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        return value.toString();
    }
}
