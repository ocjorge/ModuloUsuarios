package mx.tecnm.toluca.usuarios.service;

import mx.tecnm.toluca.usuarios.dao.*; // Asegúrate de importar todos tus DAOs
import mx.tecnm.toluca.usuarios.model.dto.*;
import mx.tecnm.toluca.usuarios.model.entity.*;
import mx.tecnm.toluca.usuarios.security.JwtService;

import jakarta.ejb.Stateless; // Anotación EJB clave para gestión transaccional
import jakarta.inject.Inject; // Anotación CDI para inyección
import jakarta.transaction.Transactional; // Para métodos que modifican la DB, aunque @Stateless a menudo lo implica

import org.mindrot.jbcrypt.BCrypt; // Bcrypt standalone library

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless // Gestionado por Payara como un EJB, ofrece transacciones automáticas
public class UsuarioService {

    @Inject private UsuarioDAO usuarioDAO;
    @Inject private TipoUsuarioCatDAO tipoUsuarioCatDAO;
    @Inject private EstadoCuentaCatDAO estadoCuentaCatDAO;
    @Inject private RolInternoDAO rolInternoDAO;
    @Inject private ModuloDAO moduloDAO;             
    @Inject private PermisoDAO permisoDAO;           
    @Inject private RolPermisoDAO rolPermisoDAO;     
    @Inject private UsuarioPermisoDAO usuarioPermisoDAO; 
    @Inject private AccionClaveCatDAO accionClaveCatDAO; // Inyectar AccionClaveCatDAO
    
    // Si implementas los servicios de auditoría y correo, inyecta sus DAOs/Servicios aquí:
    // @Inject private MailService mailService;
    // @Inject private AuditoriaAccesoDAO auditoriaAccesoDAO;
    // @Inject private AuditoriaAccionesDAO auditoriaAccionesDAO;

    @Inject private JwtService jwtService; // Servicio para manejar JWTs

    // --- Lógica de Login (RF-013, RF-022) ---
    @Transactional // Asegura que las operaciones de DB (ej. actualizar ultima_sesion) sean transaccionales
    public LoginResponse authenticate(LoginRequest request) {
        Optional<Usuario> optionalUsuario = usuarioDAO.findByEmail(request.getEmail());

        if (optionalUsuario.isEmpty()) {
            // RF-036: Registrar intento de login fallido (usuario no existe)
            // auditoriaAccesoDAO.save(new AuditoriaAcceso(null, request.getEmail(), LocalDateTime.now(), "IP_ORIGEN", tipoEventoCatDAO.findByNombre("LOGIN_FALLIDO").get(), "Usuario no encontrado"));
            return new LoginResponse("error", "Usuario o contraseña inválidos.", null, null, null, null, null, null, null);
        }

        Usuario usuario = optionalUsuario.get();

        // Verificar estado de cuenta (RF-013)
        if (usuario.getEstadoCuenta().getNombreEstado().equals("Inactivo")) {
            // RF-036: Registrar intento de login fallido (cuenta inactiva)
            return new LoginResponse("error", "Su cuenta está inactiva. Contacte al administrador.", null, null, null, usuario.getTipoUsuario().getNombreTipo(), "Inactivo", null, null);
        }
        if (usuario.getEstadoCuenta().getNombreEstado().equals("Bloqueado")) {
            // RF-036: Registrar intento de login fallido (cuenta bloqueada)
            return new LoginResponse("error", "Su cuenta está bloqueada. Contacte al administrador.", null, null, null, usuario.getTipoUsuario().getNombreTipo(), "Bloqueado", null, null);
        }

        // Validar contraseña (RF-013, RNF-004)
        if (!BCrypt.checkpw(request.getPassword(), usuario.getPasswordHash())) {
            // Contraseña incorrecta
            // RF-032: Lógica para incrementar intentos fallidos y potencialmente bloquear cuenta
            // RF-036: Registrar intento de login fallido (contraseña incorrecta)
            return new LoginResponse("error", "Usuario o contraseña inválidos.", null, null, null, null, null, null, null);
        }

        // Login exitoso
        usuario.setUltimaSesion(LocalDateTime.now()); // RF-060: Actualizar última sesión
        usuarioDAO.save(usuario); // Persiste la actualización de ultimaSesion

        // RF-036: Registrar login exitoso (implementar en AuditoriaAccesoDAO)
        // auditoriaAccesoDAO.save(new AuditoriaAcceso(usuario.getIdUsuario(), usuario.getEmail(), LocalDateTime.now(), "IP_ORIGEN", tipoEventoCatDAO.findByNombre("LOGIN_EXITOSO").get(), "Login exitoso"));

        // RF-024: Obtener módulos a los que el usuario tiene acceso general
        List<String> accesoModulos = getModulosAccesoGeneral(usuario.getIdUsuario());

        String rolInterno = (usuario.getRolInterno() != null) ? usuario.getRolInterno().getNombreRol() : null;
        String userType = usuario.getTipoUsuario().getNombreTipo();
        String accountStatus = usuario.getEstadoCuenta().getNombreEstado();

        // Generar token JWT (RF-022, RF-025)
        //String jwtToken = jwtService.generateToken(usuario.getIdUsuario(), usuario.getEmail(), userType, accountStatus, rolInterno, accesoModulos);
        
        return null; // <---- no se que hacer!!!!!!!!!
        //return new LoginResponse("success", "Login exitoso.", jwtToken, usuario.getIdUsuario(), usuario.getEmail(),
        //                         userType, accountStatus, rolInterno, accesoModulos);
    }

    // --- Lógica de Registro de Usuario Interno (RF-001, RF-002, RF-057) ---
    @Transactional
    public RegistroResponse registerInternalUser(RegistroRequest request) {
        if (usuarioDAO.existsByEmail(request.getEmail())) {
            return new RegistroResponse("error", "El correo electrónico ya está en uso.", null, request.getEmail());
        }

        String dominioEmpresaConfigurado = "tienda.com"; // RF-057: Esto debería ser configurable (ej. desde una tabla de configuración)
        if (!request.getEmail().endsWith("@" + dominioEmpresaConfigurado)) {
             return new RegistroResponse("error", "El correo electrónico debe pertenecer al dominio de la empresa (@" + dominioEmpresaConfigurado + ").", null, request.getEmail());
        }

        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());

        TipoUsuarioCat tipoInterno = tipoUsuarioCatDAO.findByNombreTipo("Interno")
                                        .orElseThrow(() -> new RuntimeException("Error de configuración: Tipo de usuario 'Interno' no encontrado."));
        EstadoCuentaCat estadoActivo = estadoCuentaCatDAO.findByNombreEstado("Activo")
                                         .orElseThrow(() -> new RuntimeException("Error de configuración: Estado de cuenta 'Activo' no encontrado."));
        RolInterno rolInicial = rolInternoDAO.findByNombreRol(request.getRolInicial())
                                     .orElseThrow(() -> new RuntimeException("Error de configuración: Rol inicial '" + request.getRolInicial() + "' no encontrado."));

        Usuario nuevoUsuario = new Usuario(
            request.getEmail(),
            hashedPassword,
            request.getNombreCompleto(),
            request.getTelefono(),
            tipoInterno,
            estadoActivo,
            rolInicial,
            dominioEmpresaConfigurado // RF-057: Guardar el dominio de la empresa
        );

        Usuario savedUsuario = usuarioDAO.save(nuevoUsuario); // RF-002: INSERT en PostgreSQL

        // RF-004: Enviar correo de bienvenida (simulado o real con MailService)
        // if (mailService != null) { mailService.sendWelcomeEmail(savedUsuario.getEmail(), request.getPassword()); }

        return new RegistroResponse("success", "Usuario interno registrado exitosamente.", savedUsuario.getIdUsuario(), savedUsuario.getEmail());
    }
    
    // --- Cierre de Sesión (RF-073) ---
    public void invalidateToken(String token) {
        jwtService.blacklistToken(token); // RF-025: Agrega el token a la lista negra
        System.out.println("Token blacklisted: " + token);
        // RF-037: Registrar acción de cierre de sesión en auditoría
        // auditoriaAccionesDAO.save(new AuditoriaAccion(userId_from_token, "LOGOUT", "USUARIOS", ..., "Cierre de sesión"));
    }

    // --- Listar Usuarios (RF-034, RF-048, RF-049, RF-050, RF-051, RF-060) ---
    public PageResponse<UsuarioDTO> getAllUsuarios(int page, int size, String sortBy, String sortDir, String nombreCompleto, String email, String estadoCuenta, String rolInterno) {
        List<Usuario> usuarios = usuarioDAO.findAllPaginatedFiltered(page, size, sortBy, sortDir, nombreCompleto, email, estadoCuenta, rolInterno);
        long totalElements = usuarioDAO.countAllFiltered(nombreCompleto, email, estadoCuenta, rolInterno);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<UsuarioDTO> dtoList = usuarios.stream().map(this::convertToDto).collect(Collectors.toList());
        return new PageResponse<>(dtoList, page, size, totalElements, totalPages, (page >= totalPages - 1), (page == 0));
    }

    // --- Obtener Usuario por ID (RF-044) ---
    public Optional<UsuarioDTO> getUsuarioById(UUID id) {
        return usuarioDAO.findById(id).map(this::convertToDto);
    }

    // --- Actualizar Usuario (RF-034, RF-039) ---
    @Transactional
    public Optional<UsuarioDTO> updateUsuario(UUID id, UsuarioUpdateRequest request) {
        return usuarioDAO.findById(id).map(usuario -> {
            usuario.setNombreCompleto(request.getNombreCompleto());
            usuario.setTelefono(request.getTelefono());
            
            // RF-034: Permitir cambio de email, con validación de unicidad si cambia
            if (!usuario.getEmail().equals(request.getEmail())) {
                if (usuarioDAO.existsByEmail(request.getEmail())) {
                    throw new RuntimeException("El nuevo correo electrónico ya está en uso."); // Manejar esta excepción en el controlador
                }
                usuario.setEmail(request.getEmail());
            }

            // RF-034: Actualizar rol interno (si aplica)
            if (request.getRolInternoId() != null) {
                rolInternoDAO.findById(request.getRolInternoId()).ifPresent(usuario::setRolInterno);
            }
            
            // RF-034: Actualizar estado de cuenta
            if (request.getEstadoCuentaId() != null) {
                estadoCuentaCatDAO.findById(request.getEstadoCuentaId()).ifPresent(usuario::setEstadoCuenta);
            }
            
            // RF-039: Actualizar dirección de envío para clientes
            if (usuario.getTipoUsuario().getNombreTipo().equals("Cliente") && request.getDireccionEnvio() != null) {
                usuario.setDireccionEnvio(request.getDireccionEnvio());
            }

            usuario.setFechaUltimaActualizacion(LocalDateTime.now());
            Usuario updatedUsuario = usuarioDAO.save(usuario); // Persiste los cambios
            // RF-037: Registrar acción de edición en auditoría
            // auditoriaAccionesDAO.save(new AuditoriaAccion(id, "EDITAR_USUARIO", "USUARIOS", ..., detalles));
            return convertToDto(updatedUsuario);
        });
    }
    
    // --- Desactivar Usuario Lógicamente (RF-003) ---
    @Transactional
    public boolean deactivateUser(UUID id) {
        return usuarioDAO.findById(id).map(usuario -> {
            estadoCuentaCatDAO.findByNombreEstado("Inactivo").ifPresent(usuario::setEstadoCuenta); // Cambia el estado a "Inactivo"
            usuario.setFechaUltimaActualizacion(LocalDateTime.now());
            usuarioDAO.save(usuario); // Persiste el cambio de estado
            // RF-037: Registrar acción de desactivación en auditoría
            // RF-026: Notificar a otros módulos el cambio de estado
            return true;
        }).orElse(false);
    }

    // --- Métodos para obtener datos de Catálogos (RF-034, RF-035, detallesUsuarios.xhtml) ---
    public List<RolInterno> getAllRoles() { return rolInternoDAO.findAll(); }
    public List<EstadoCuentaCat> getAllEstadosCuenta() { return estadoCuentaCatDAO.findAll(); }
    
    // --- Métodos de apoyo (para RF-023 y RF-024) ---
    // Esta lógica debería ser más robusta, consultando las tablas de permisos.
    public List<String> getModulosAccesoGeneral(UUID userId) {
        Optional<Usuario> optionalUsuario = usuarioDAO.findById(userId);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            if (usuario.getTipoUsuario().getNombreTipo().equals("Interno")) {
                if (usuario.getRolInterno() != null) {
                    switch (usuario.getRolInterno().getNombreRol()) {
                        case "Gerente":
                            return List.of("ERP_CORE", "CRM_MODULE", "PROVEEDORES", "BANCOS", "USUARIOS");
                        case "Jefe":
                            return List.of("ERP_CORE", "CRM_MODULE", "PROVEEDORES");
                        case "Operador":
                            return List.of("ERP_CORE");
                    }
                }
            } else if (usuario.getTipoUsuario().getNombreTipo().equals("Proveedor")) {
                return List.of("PROVEEDORES_PORTAL");
            } else if (usuario.getTipoUsuario().getNombreTipo().equals("Cliente")) {
                return List.of("CLIENTES_PORTAL");
            }
        }
        return Collections.emptyList();
    }

    // --- Método auxiliar para convertir Entity a DTO ---
    private UsuarioDTO convertToDto(Usuario usuario) {
        // Asegúrate de que los objetos relacionados no sean null antes de llamar a getNombre...
        String tipoUsuarioNombre = (usuario.getTipoUsuario() != null) ? usuario.getTipoUsuario().getNombreTipo() : null;
        String estadoCuentaNombre = (usuario.getEstadoCuenta() != null) ? usuario.getEstadoCuenta().getNombreEstado() : null;
        String rolInternoNombre = (usuario.getRolInterno() != null) ? usuario.getRolInterno().getNombreRol() : null;

        return new UsuarioDTO(
            usuario.getIdUsuario(),
            usuario.getEmail(),
            usuario.getNombreCompleto(),
            usuario.getTelefono(),
            tipoUsuarioNombre,
            estadoCuentaNombre,
            rolInternoNombre,
            usuario.getUltimaSesion(),
            usuario.getFechaCreacion(),
            usuario.getDireccionEnvio(),
            usuario.getDominioEmpresa(),
            getModulosAccesoGeneral(usuario.getIdUsuario()) // Carga los módulos de acceso general
        );
    }
}