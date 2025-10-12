package mx.tecnm.toluca.usuarios.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Define la ruta base para todos tus recursos REST. Tus endpoints serán /api/...
@ApplicationPath("api")
public class JakartaRestConfiguration extends Application {
    // Payara descubrirá automáticamente recursos JAX-RS (@Path) y Providers (@Provider)
    // Clases manualmente si no se usa @Provider o si hay problemas de descubrimiento.
    // @Override
    // public Set<Class<?>> getClasses() {
    //     Set<Class<?>> resources = new java.util.HashSet<>();
    //     resources.add(mx.tecnm.toluca.usuarios.controller.AuthResource.class);
    //     resources.add(mx.tecnm.toluca.usuarios.controller.UsuarioResource.class);
    //     resources.add(mx.tecnm.toluca.usuarios.filter.JwtRequestFilter.class); // Registra el filtro
    //     return resources;
    // }
}