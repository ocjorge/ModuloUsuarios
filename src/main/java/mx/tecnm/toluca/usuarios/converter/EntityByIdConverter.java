package mx.tecnm.toluca.usuarios.converter;

// Importaciones necesarias para trabajar con JSF
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

// Importación del paquete de modelos (no se usa directamente aquí,
// pero suele incluir entidades relacionadas con el converter)
import mx.tecnm.toluca.usuarios.model.*;

/**
 * Converter genérico para convertir valores String (generalmente IDs)
 * a objetos del tipo UUID y viceversa dentro del ciclo de vida de JSF.
 *
 * Este converter es útil cuando se manejan entidades identificadas
 * por UUID en formularios JSF (por ejemplo, en <h:selectOneMenu>).
 */
@FacesConverter(value = "entityByIdConverter", managed = true)
public class EntityByIdConverter implements Converter<Object> {

    /**
     * Convierte un valor String proveniente de la vista (UI)
     * hacia un objeto Java.
     *
     * @param context   contexto actual de JSF
     * @param component componente de la interfaz que usa el converter
     * @param value     valor recibido desde la vista (normalmente un String)
     * @return          objeto convertido (UUID) o null si el valor no es válido
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        // Si el valor es nulo o está vacío, no se puede convertir
        if (value == null || value.isBlank()) return null;

        try {
            // Intenta convertir el String a un UUID
            return java.util.UUID.fromString(value);
        } catch (Exception e) {
            // Si ocurre cualquier error (formato inválido, etc.)
            // se retorna null para evitar excepciones en JSF
            return null;
        }
    }

    /**
     * Convierte un objeto Java hacia su representación String
     * para ser mostrada o enviada a la vista.
     *
     * @param context   contexto actual de JSF
     * @param component componente de la interfaz que usa el converter
     * @param value     objeto a convertir (normalmente UUID)
     * @return          representación String del objeto o cadena vacía
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        // Si el objeto es nulo, se regresa una cadena vacía
        if (value == null) return "";

        // Convierte el objeto a String (UUID → String)
        return value.toString();
    }
}
