<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Usuarios</title>
  <style>
    :root {
      --color-primario: #2c5aa0;
      --color-secundario: #34495e;
      --color-acento: #3498db;
      --color-exito: #27ae60;
      --color-peligro: #e74c3c;
      --color-advertencia: #f39c12;
      --luz-bg: #f8f9fa;
      --texto-oscuro: #2c3e50;
    }
    body { margin: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: var(--luz-bg); color: var(--texto-oscuro); }
    .header { background-color: var(--color-primario); color: white; padding: 15px 30px; display: flex; align-items: center; justify-content: space-between; }
    .header h1 { margin: 0; font-size: 1.5em; }
    .header-right { display: flex; align-items: center; gap: 15px; }
    .header-right span { font-size: 0.95em; }
    .header-right .btn-logout { background-color: var(--color-peligro); color: white; border: none; padding: 8px 16px; border-radius: 6px; font-weight: bold; cursor: pointer; text-transform: uppercase; transition: background-color 0.3s ease; }
    .header-right .btn-logout:hover { background-color: #c0392b; }
    .container { max-width: 1200px; margin: 30px auto; padding: 30px; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1); }
    h2 { color: var(--color-secundario); text-align: center; margin-bottom: 20px; }
    .buttons-nav { display: flex; gap: 15px; margin-bottom: 20px; }
    .buttons-nav button { padding: 10px 20px; border: none; border-radius: 6px; font-weight: bold; text-transform: uppercase; cursor: pointer; color: white; transition: background-color 0.3s ease, transform 0.2s ease; }
    .buttons-nav .new-user { background-color: var(--color-exito); }
    .buttons-nav .new-user:hover { background-color: #1e8449; }
    .buttons-nav .filter { background-color: var(--color-acento); }
    .buttons-nav .filter:hover { background-color: #2d80c4; }
    table { width: 100%; border-collapse: collapse; background-color: #fff; border-radius: 8px; overflow: hidden; }
    th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
    th { background-color: var(--luz-bg); font-weight: 600; text-transform: uppercase; font-size: 0.9em; }
    tr:hover { background-color: #f1f7fe; }
    .actions { display: flex; gap: 8px; }
    .actions button { padding: 6px 10px; border: none; border-radius: 5px; cursor: pointer; font-size: 0.9em; color: white; text-transform: uppercase; }
    .actions .edit { background-color: var(--color-acento); }
    .actions .edit:hover { background-color: #2d80c4; }
    .actions .delete { background-color: var(--color-peligro); }
    .actions .delete:hover { background-color: #c0392b; }
    .badge { padding: 4px 10px; border-radius: 12px; color: white; font-size: 0.85em; font-weight: bold; text-align: center; }
    .activo { background-color: var(--color-exito); }
    .inactivo { background-color: var(--color-peligro); }
  </style>
</head>
<body>
  <div class="header">
    <h1>Usuarios</h1>
    <div class="header-right">
      <span>Bienvenido, ${sessionScope.nombreUsuario}</span>
      <form action="LogoutServlet" method="post" style="display:inline;">
        <button class="btn-logout">Cerrar Sesión</button>
      </form>
    </div>
  </div>

  <div class="container">
    <h2>Lista de Usuarios</h2>

    <div class="buttons-nav">
      <button class="new-user" onclick="window.location.href='nuevoUsuario.jsp'">+ Nuevo Usuario</button>
      <button class="filter">&#128269; Filtrar</button>
    </div>

    <table>
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Correo</th>
          <th>Módulo</th>
          <th>Teléfono</th>
          <th>Estado</th>
          <th>Rol</th>
          <th>Acciones</th>
        </tr>
      </thead>

      <tbody>
        <%-- 
          Más adelante aquí irán tus resultados de PostgreSQL:
          Ejemplo con JSTL:
          <c:forEach var="usuario" items="${listaUsuarios}">
            <tr>
              <td>${usuario.nombre}</td>
              <td>${usuario.correo}</td>
              <td>${usuario.modulo}</td>
              <td>${usuario.telefono}</td>
              <td><span class="badge ${usuario.estado == 'Activo' ? 'activo' : 'inactivo'}">${usuario.estado}</span></td>
              <td>${usuario.rol}</td>
              <td class="actions">
                <button class="edit" onclick="location.href='editarUsuario.jsp?id=${usuario.id}'">Editar</button>
                <button class="delete" onclick="location.href='EliminarUsuarioServlet?id=${usuario.id}'">Eliminar</button>
              </td>
            </tr>
          </c:forEach>
        --%>
      </tbody>
    </table>
  </div>
</body>
</html>
