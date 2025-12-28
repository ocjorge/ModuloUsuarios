package mx.tecnm.toluca.usuarios.web.converters;

// Importaciones necesarias para JSF Converter
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

// Importación para inyección de dependencias
import jakarta.inject.Inject;

// Importaciones del modelo y servicio
import mx.tecnm.toluca.usuarios.model.TipoUsuario;
import mx.tecnm.toluca.usuarios.service.UsuarioService;

/**
 * Converter JSF para la entidad TipoUsuario.
 * Permite convertir entre el ID (String) usado en la vista
 * y el objeto TipoUsuario utilizado en el backend.
 */
@FacesConverter(value = "tipoUsuarioConverter", managed = true)
public class TipoUsuarioConverter implements Converter<TipoUsuario> {

    /**
     * Servicio de usuarios.
     * Se utiliza para buscar el TipoUsuario por su ID.
     */
    @Inject
    private UsuarioService usuarioService;

    /**
     * Convierte el valor String proveniente de la vista
     * (normalmente el ID del TipoUsuario) a un objeto TipoUsuario.
     *
     * @param fc  contexto JSF
     * @param uic componente de la interfaz
     * @param id  identificador recibido desde la vista
     * @return    objeto TipoUsuario o null si el valor es inválido
     */
    @Override
    public TipoUsuario getAsObject(FacesContext fc, UIComponent uic, String id) {

        // Si el valor es nulo o vacío, no se puede convertir
        if (id == null || id.isBlank()) return null;

        // Convierte el ID a Integer y busca la entidad correspondiente
        return usuarioService.buscarTipoUsuarioPorId(Integer.valueOf(id));
    }

    /**
     * Convierte un objeto TipoUsuario a su representación String
     * (normalmente el ID) para ser utilizada en la vista JSF.
     *
     * @param fc  contexto JSF
     * @param uic componente de la interfaz
     * @param obj objeto TipoUsuario
     * @return    ID del TipoUsuario como String o cadena vacía
     */
    @Override
    public String getAsString(FacesContext fc, UIComponent uic, TipoUsuario obj) {

        // Si el objeto no es nulo, retorna su ID como String
        return obj != null ? obj.getId().toString() : "";
    }
}
