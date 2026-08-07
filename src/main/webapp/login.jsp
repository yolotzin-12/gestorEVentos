<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center min-vh-100">

    <div class="card p-4 shadow-sm tarjeta-personalizada">
        <div class="card-body text-center">

            <img src="img/logo.png" alt="Logo SRAE" class="mb-4" style="max-height: 100px;">

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger d-flex align-items-center py-2 text-start" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div class="small">
                    <%= request.getAttribute("error") %>
                </div>
            </div>
            <% } %>

            <% if (request.getAttribute("mensaje") != null) { %>
            <div class="alert alert-success d-flex align-items-center py-2 text-start" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <div class="small">
                    <%= request.getAttribute("mensaje") %>
                </div>
            </div>
            <% } %>

            <form action="login" method="POST">

                <div class="mb-3 text-start">
                    <label for="email" class="form-label fw-bold label-formulario">Correo Electrónico:</label>
                    <input type="email" name="email" class="form-control input-formulario" id="email" placeholder="Tu correo electrónico:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="contra" class="form-label fw-bold label-formulario">Contraseña:</label>
                    <input type="password" name="contra" class="form-control input-formulario" id="contra" placeholder="Tu contraseña:" required>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-ingresar text-white fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
                        <i class="bi bi-cursor me-2" style="font-size: 1.2rem;"></i> Iniciar Sesión
                    </button>
                </div>

                <div class="text-center mt-3">
                    <a href="registro.jsp" class="text-decoration-none">¿No tienes cuenta? Regístrate</a>
                </div>
                <div class="text-center mt-2">
                    <a href="recuperarContra.jsp" class="text-decoration-none">Recuperar contraseña</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>