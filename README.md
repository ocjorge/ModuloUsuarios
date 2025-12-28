# 🚀 Módulo de Usuarios - Sistema de Gestión

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-10-purple?style=for-the-badge&logo=eclipse)
![JSF](https://img.shields.io/badge/JSF-4.0-blue?style=for-the-badge&logo=java)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3-7952B3?style=for-the-badge&logo=bootstrap)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=for-the-badge&logo=apache-maven)

Sistema web de gestión de usuarios desarrollado con Jakarta EE 10, JSF 4.0 y PostgreSQL para la materia de Programación Web del Tecnológico Nacional de México, Campus Toluca.

## 👥 Equipo de Desarrollo

### Desarrolladores

| Nombre | Rol | GitHub | Contribución Principal |
|--------|-----|--------|------------------------|
| **Jorge Ortiz Ceballos** | Desarrollador Full Stack | [![GitHub](https://img.shields.io/badge/GitHub-ocjorge-181717?style=flat-square&logo=github)](https://github.com/ocjorge) | Arquitectura backend, seguridad, lógica de negocio |
| **Brisa Hernández Reyes** | Desarrolladora Frontend | [![GitHub](https://img.shields.io/badge/GitHub-brisahernandezreyes12345-181717?style=flat-square&logo=github)](https://github.com/brisahernandezreyes12345) | Diseño UI/UX, vistas JSF, componentes Bootstrap |
| **Luis Carlos Romero Corral** | Desarrollador Backend & BD | [![GitHub](https://img.shields.io/badge/GitHub-kc0115-181717?style=flat-square&logo=github)](https://github.com/kc0115) | Modelo de datos, consultas SQL, reportes, auditoría |

### Institución
**Tecnológico Nacional de México, Campus Toluca**  
[![Sitio Web](https://img.shields.io/badge/Sitio_Web-TecNM_Toluca-0066CC?style=flat-square&logo=google-chrome)](https://toluca.tecnm.mx/)

---

## 📋 Características

- ✅ **Autenticación segura** con BCrypt y manejo de sesiones
- ✅ **CRUD completo** de usuarios con roles y permisos
- ✅ **Dashboard administrativo** con métricas en tiempo real
- ✅ **Gestión de tickets** con aprobación/rechazo
- ✅ **Reportes visuales** con gráficos Chart.js
- ✅ **Interfaz responsive** con Bootstrap 5.3
- ✅ **Auditoría** de accesos al sistema
- ✅ **Filtros avanzados** y búsqueda
- ✅ **Validación de formularios** con JSF

## 🏗️ Arquitectura

```bash
ModuloUsuarios/
├── src/
│   ├── main/
│   │   ├── java/mx/tecnm/toluca/usuarios/
│   │   │   ├── model/          # Entidades JPA
│   │   │   ├── service/        # Lógica de negocio
│   │   │   ├── web/           # Managed Beans JSF
│   │   │   ├── converter/      # Convertidores JSF
│   │   │   └── security/      # Filtros de seguridad
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       ├── resources/
│   │       └── *.xhtml        # Vistas JSF
└── pom.xml                    # Configuración Maven
```

## 📊 Módulos del Sistema

### 🔐 Autenticación y Seguridad
- Login con validación BCrypt
- Control de sesiones con `SessionManager`
- Filtro de seguridad `@WebFilter`
- Auditoría de accesos (login/logout)

### 👥 Gestión de Usuarios
- Crear, editar, eliminar usuarios
- Asignación de roles (Administrador, Usuario, etc.)
- Estados de cuenta (Activo/Inactivo)
- Asignación a módulos específicos
- Validación de username único

### 🎫 Sistema de Tickets
- Creación de tickets de modificación
- Estados: Pendiente, Aprobado, Rechazado
- Filtros por módulo, tipo y estado
- Visualización de datos JSON propuestos
- Aprobación/rechazo con confirmación

### 📈 Reportes y Dashboard
- Métricas de accesos (totales, exitosos, fallidos)
- Tickets pendientes
- Gráficos Chart.js (líneas y barras)
- Datos de últimos 7 días
- Exportación visual de estadísticas

### 🎨 Interfaz de Usuario
- Sidebar navegación
- Tarjetas informativas
- Tablas con filtros
- Formularios validados
- Responsive design
- Mensajes de confirmación

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 | Lenguaje base |
| **Jakarta EE** | 10.0 | Plataforma empresarial |
| **JSF (JavaServer Faces)** | 4.0.1 | Framework web MVC |
| **PostgreSQL** | 16 | Base de datos |
| **JPA (Hibernate)** | - | Persistencia ORM |
| **Bootstrap** | 5.3 | Framework CSS |
| **Chart.js** | 3.x | Gráficos JavaScript |
| **jBCrypt** | 0.4 | Hashing de contraseñas |
| **Jackson** | 2.12.6 | Procesamiento JSON |
| **Maven** | 3.9 | Gestión de dependencias |

## 📁 Estructura de Entidades

### Principales Entidades JPA
- **Usuario**: Gestión completa de usuarios del sistema
- **TicketRevision**: Solicitudes de cambios/modificaciones
- **AuditoriaAcceso**: Registro de accesos al sistema
- **Modulo**: Módulos del sistema (USR, VNT, etc.)
- **RolInterno**: Roles de usuarios
- **EstadoCuenta**: Estados de la cuenta (Activo/Inactivo)
- **TipoEvento**: Tipos de eventos de auditoría
- **TipoCambio**: Tipos de cambios en tickets

## 🔧 Configuración e Instalación

### Prerrequisitos
- Java JDK 17 o superior
- PostgreSQL 16
- Apache Tomcat 10+ o Payara 6+
- Maven 3.9+

### Pasos de Instalación

1. **Clonar repositorio**
```bash
git clone https://github.com/ocjorge/ModuloUsuarios.git
cd ModuloUsuarios
```

2. **Configurar base de datos**
```sql
CREATE DATABASE usuarios_db;
-- Ejecutar script SQL de creación de tablas
```

3. **Configurar `persistence.xml`**
```xml
<jta-data-source>jdbc/usuariosDS</jta-data-source>
```

4. **Compilar y empaquetar**
```bash
mvn clean package
```

5. **Desplegar en servidor**
- Copiar `target/ModuloUsuarios-1.0-SNAPSHOT.war` al directorio `webapps` de Tomcat

6. **Acceder a la aplicación**
```
http://localhost:8080/ModuloUsuarios-1.0-SNAPSHOT/
```

## 🚀 Características Técnicas Avanzadas

### Seguridad
- Hash BCrypt para contraseñas
- Filtro de seguridad a nivel aplicación
- Control de sesiones por bean
- Redirección automática a login
- Auditoría completa de accesos

### Persistencia
- JPA con PostgreSQL
- Relaciones @ManyToOne y @OneToMany
- Consultas optimizadas con JOIN FETCH
- Transacciones @Transactional
- UUID como claves primarias

### Interfaz de Usuario
- Componentes JSF estándar
- Bootstrap 5.3 para estilos
- Chart.js para visualización
- JavaScript para interactividad
- Diseño responsive

### Validaciones
- Required fields en formularios
- Validación de username único
- Confirmación para acciones críticas
- Mensajes de error/éxito contextuales

## 📱 Vistas Principales

1. **`login.xhtml`** - Página de autenticación
2. **`dashboard.xhtml`** - Panel principal con tarjetas
3. **`usuarios.xhtml`** - Listado y gestión de usuarios
4. **`usuario.xhtml`** - Formulario de creación/edición
5. **`tickets.xhtml`** - Gestión de tickets pendientes
6. **`ticket-detalle.xhtml`** - Detalle de ticket específico
7. **`reportes.xhtml`** - Reportes y gráficos estadísticos

## 🔐 Credenciales por Defecto

```properties
Usuario administrador:
- Username: admin
- Contraseña: admin123 (cambiar en producción)
```

## 📊 Scripts SQL de Ejemplo

```sql
-- Insertar usuario administrador inicial
INSERT INTO usuarios (username, contrasena, nombre_completo, correo_electronico, id_tipo_usuario)
VALUES ('admin', '$2a$10$...', 'Administrador', 'admin@sistema.com', 1);
```

## 🐛 Solución de Problemas Comunes

### Error: "No se puede conectar a la base de datos"
- Verificar configuración del DataSource
- Comprobar credenciales de PostgreSQL
- Validar que el servicio de BD esté corriendo

### Error: "Bean no encontrado"
- Verificar anotaciones @Named y @Inject
- Comprobar scope de los beans
- Validar configuración de CDI

### Error: "Página en blanco"
- Revisar logs del servidor
- Verificar sintaxis de archivos .xhtml
- Comprobar rutas de redirección

## 📈 Mejoras Futuras

- [ ] **Exportación a PDF/Excel** de reportes
- [ ] **Notificaciones en tiempo real** con WebSockets
- [ ] **API REST** para integraciones externas
- [ ] **Logs de auditoría** más detallados
- [ ] **Backup automático** de base de datos
- [ ] **Interfaz móvil** optimizada
- [ ] **Temas oscuro/claro** seleccionables
- [ ] **Recuperación de contraseña** por email

## 📄 Licencia y Reconocimientos

Este proyecto fue desarrollado para fines educativos como parte de la materia **Programación Web** del **Tecnológico Nacional de México, Instituto Tecnológico de Toluca* durante el período **Agosto-Diciembre 2025**.

**Materia:** Programación Web  
**Institución:** Tecnológico Nacional de México, Instituto Tecnológico de Toluca  
**Período:** Agosto - Diciembre 2025  
**Versión:** 1.0-SNAPSHOT

## 🤝 Contribuciones

Si deseas contribuir a este proyecto:

1. Haz fork del repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 🌟 Características Destacadas

- **Código limpio y mantenible** con separación de responsabilidades
- **Patrón MVC** estricto con JSF
- **Inyección de dependencias** con CDI
- **Manejo de excepciones** amigable al usuario
- **Documentación completa** en código
- **Configuración modular** fácil de extender

Para más información, contactar a los desarrolladores o revisar la documentación técnica completa en los repositorios individuales.
