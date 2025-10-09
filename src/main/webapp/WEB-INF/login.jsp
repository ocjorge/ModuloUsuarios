<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Inicio de Sesión</title>
    <style>
        :root { 
            --primary-color: #2c5aa0;
            --secondary-color: #34495e;
            --accent-color: #3498db;
            --success-color: #27ae60;
            --danger-color: #e74c3c;
            --warning-color: #f39c12;
            --light-bg: #f8f9fa;
            --dark-text: #2c3e50;
        }

        body {
            font-family: Arial, sans-serif;
            background: var(--light-bg);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            color: var(--dark-text);
        }

        .login-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.2);
            padding: 30px 40px;
            width: 350px;
            text-align: center;
        }

        h2 {
            margin-bottom: 25px;
            color: var(--primary-color);
        }

        .form-group {
            margin-bottom: 20px;
            text-align: left;
        }

        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        .btn {
            width: 100%;
            background: var(--primary-color);
            color: white;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
            font-size: 15px;
            transition: background 0.3s;
        }

        .btn:hover {
            background: var(--accent-color);
        }

        .error {
            color: var(--danger-color);
            margin-bottom: 10px;
            font-size: 14px;
        }

        .footer {
            margin-top: 15px;
            font-size: 13px;
            color: #555;
        }

    </style>
</head>
<body>
    <div class="login-container">
        <h2>Iniciar Sesión</h2>

        <% 
           String error = request.getParameter("error");
           if ("true".equals(error)) {
        %>
           <div class="error">Usuario o contraseña incorrectos</div>
        <% } %>

        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="username">Nombre de usuario</label>
                <input type="text" id="username" name="username" placeholder="Ingresa tu usuario" required>
            </div>

            <div class="form-group">
                <label for="password">Contraseña</label>
                <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>
            </div>

            <button type="submit" class="btn">INICIAR SESIÓN</button>
        </form>

        <div class="footer">
            &copy; 2025 Sistema de Usuarios - Todos los derechos reservados
        </div>
    </div>
</body>
</html>
