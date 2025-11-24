package mx.tecnm.toluca.usuarios.web.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@FacesConverter(value = "entityByIdConverter", managed = true)
public class EntityByIdConverter implements Converter<Object> {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent cmp, String value) {
        if (value == null || value.isBlank()) return null;

        String className = (String) cmp.getAttributes().get("entityClass");
        if (className == null) return null;

        try {
            Class<?> clazz = Class.forName(className);
            return em.find(clazz, Integer.valueOf(value));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent cmp, Object obj) {
        try {
            return obj != null
                ? obj.getClass().getMethod("getId").invoke(obj).toString()
                : "";
        } catch (Exception e) {
            return "";
        }
    }
}
