package mx.tecnm.toluca.usuarios.controller;

import mx.tecnm.toluca.usuarios.model.dto.PageResponse;
import mx.tecnm.toluca.usuarios.model.dto.UsuarioDTO;
import mx.tecnm.toluca.usuarios.model.dto.UsuarioUpdateRequest;
import mx.tecnm.toluca.usuarios.model.entity.RolInterno;
import mx.tecnm.toluca.usuarios.model.entity.EstadoCuentaCat;
import mx.tecnm.toluca.usuarios.service.UsuarioService;
import mx.tecnm.toluca.usuarios.security.JwtService;
import mx.tecnm.toluca.usuarios.filter.Secured; // Importar la anotación Secured

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/users")
@Secured // Protege todos los métodos de este recurso por defecto
public class UsuarioResource {

    @Inject private UsuarioService usuarioService;
    @Inject private JwtService jwtService;

    @GET @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsuarios(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sortBy") @DefaultValue("nombreCompleto") String sortBy,
            @QueryParam("sortDir") @DefaultValue("asc") String sortDir,
            @QueryParam("nombreCompleto") String nombreCompleto,
            @QueryParam("email") String email,
            @QueryParam("estadoCuenta") String estadoCuenta,
            @QueryParam("rolInterno") String rolInterno,
            @Context HttpHeaders headers
    ) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
        String rolInternoClaim = jwtService.extractClaim(token, claims -> claims.get("rolInterno", String.class));

        if (!"Gerente".equals(rolInternoClaim) && !"Jefe".equals(rolInternoClaim)) {
             return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado. Se requiere rol de Gerente o Jefe.").build();
        }

        PageResponse<UsuarioDTO> usuarios = usuarioService.getAllUsuarios(page, size, sortBy, sortDir, nombreCompleto, email, estadoCuenta, rolInterno);
        return Response.ok(usuarios).build();
    }

    @GET @Path("/{id}") @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarioById(@PathParam("id") UUID id, @Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
        UUID requestingUserId = jwtService.extractUserId(token);
        String rolInternoClaim = jwtService.extractClaim(token, claims -> claims.get("rolInterno", String.class));

        if (!requestingUserId.equals(id) && !"Gerente".equals(rolInternoClaim) && !"Jefe".equals(rolInternoClaim)) {
             return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado. No tiene permiso para ver este perfil.").build();
        }

        return usuarioService.getUsuarioById(id)
                .map(userDTO -> Response.ok(userDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT @Path("/{id}") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
    public Response updateUsuario(@PathParam("id") UUID id, @Valid UsuarioUpdateRequest request, @Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
        UUID requestingUserId = jwtService.extractUserId(token);
        String rolInternoClaim = jwtService.extractClaim(token, claims -> claims.get("rolInterno", String.class));

        if (!"Gerente".equals(rolInternoClaim) && !requestingUserId.equals(id)) {
             return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado. No tiene permiso para editar este usuario.").build();
        }

        return usuarioService.updateUsuario(id, request)
                .map(userDTO -> Response.ok(userDTO).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    
    @PUT @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") UUID id, @Context HttpHeaders headers) {
        String token = headers.getHeaderString(HttpHeaders.AUTHORIZATION).substring("Bearer ".length()).trim();
        String rolInternoClaim = jwtService.extractClaim(token, claims -> claims.get("rolInterno", String.class));

        if (!"Gerente".equals(rolInternoClaim) && !"Jefe".equals(rolInternoClaim)) {
             return Response.status(Response.Status.FORBIDDEN).entity("Acceso denegado. Solo Gerentes o Jefes pueden desactivar usuarios.").build();
        }
        if (usuarioService.deactivateUser(id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET @Path("/roles") @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        List<RolInterno> roles = usuarioService.getAllRoles();
        return Response.ok(roles).build();
    }

    @GET @Path("/estadoscuenta") @Produces(MediaType.APPLICATION_JSON)
    public Response getAllEstadosCuenta() {
        List<EstadoCuentaCat> estados = usuarioService.getAllEstadosCuenta();
        return Response.ok(estados).build();
    }
}