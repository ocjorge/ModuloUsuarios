package mx.tecnm.toluca.usuarios.controller;
import mx.tecnm.toluca.usuarios.model.dto.*;
import mx.tecnm.toluca.usuarios.service.UsuarioService;
import mx.tecnm.toluca.usuarios.filter.Secured; // Importar la anotación Secured
import jakarta.inject.Inject; import jakarta.validation.Valid; import jakarta.ws.rs.*; import jakarta.ws.rs.core.Context; import jakarta.ws.rs.core.HttpHeaders; import jakarta.ws.rs.core.MediaType; import jakarta.ws.rs.core.Response;
@Path("/auth")
public class AuthResource {
    @Inject private UsuarioService usuarioService;
    
    @POST @Path("/login") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginRequest request) {
        LoginResponse response = usuarioService.authenticate(request);
        if ("success".equals(response.getStatus())) { return Response.ok(response).build(); }
        else {
            if ("Su cuenta está inactiva. Contacte al administrador.".equals(response.getMessage()) || "Su cuenta está bloqueada. Contacte al administrador.".equals(response.getMessage())) {
                return Response.status(Response.Status.FORBIDDEN).entity(response).build(); }
            return Response.status(Response.Status.UNAUTHORIZED).entity(response).build(); }
    }
    
    @POST @Path("/logout") @Produces(MediaType.APPLICATION_JSON)
    @Secured // Protege el endpoint de logout para asegurar que solo tokens válidos puedan intentar invalidarse
    public Response logout(@Context HttpHeaders headers) {
        String authorizationHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
        String jwt = authorizationHeader.substring("Bearer ".length()).trim();
        usuarioService.invalidateToken(jwt); 
        return Response.ok(new LoginResponse("success", "Sesión cerrada exitosamente. Token invalidado.", null, null, null, null, null, null, null)).build();
    }

    @POST @Path("/register/internal") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public Response registerInternal(@Valid RegistroRequest request) {
        RegistroResponse response = usuarioService.registerInternalUser(request);
        if ("success".equals(response.getStatus())) { return Response.status(Response.Status.CREATED).entity(response).build(); }
        else { return Response.status(Response.Status.BAD_REQUEST).entity(response).build(); }
    }
}