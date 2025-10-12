package mx.tecnm.toluca.usuarios.filter;

import mx.tecnm.toluca.usuarios.dao.UsuarioDAO;
import mx.tecnm.toluca.usuarios.model.entity.Usuario;
import mx.tecnm.toluca.usuarios.security.JwtService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Optional;

@Secured // Este filtro solo se aplicará a recursos o métodos marcados con @Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtRequestFilter implements ContainerRequestFilter {

    @Inject private JwtService jwtService;
    @Inject private UsuarioDAO usuarioDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            abortWithUnauthorized(requestContext, "Token de autorización es requerido o tiene formato inválido.");
            return;
        }

        String jwt = authorizationHeader.substring("Bearer ".length()).trim();
        String userEmail = jwtService.extractEmail(jwt);

        if (userEmail == null || jwtService.isTokenExpired(jwt) || jwtService.isTokenBlacklisted(jwt)) {
            abortWithUnauthorized(requestContext, "Token inválido, expirado o revocado.");
            return;
        }

        Optional<Usuario> userOptional = usuarioDAO.findByEmail(userEmail);
        if (userOptional.isEmpty() || !jwtService.validateToken(jwt, userOptional.get().getEmail())) {
            abortWithUnauthorized(requestContext, "Token inválido o usuario no coincide con el token.");
            return;
        }
        
        // Puedes guardar el usuario autenticado en las propiedades del contexto de la petición
        // para que los recursos puedan acceder a él:
        requestContext.setProperty("user", userOptional.get());
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .entity(message).build());
    }
}