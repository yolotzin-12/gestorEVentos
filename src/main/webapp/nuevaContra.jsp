<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String token = (String) request.getAttribute("token");
    if (token == null || token.isEmpty()) {
        token = request.getParameter("token");
    }
    if (token == null) {
        token = "";
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva Contraseña - SRAE</title>

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

            <form action="restablecer" method="POST">

                <input type="hidden" name="token" value="<%= token %>">

                <div class="mb-3 text-start">
                    <label for="nuevaContra" class="form-label fw-bold label-formulario">Nueva Contraseña:</label>
                    <input type="password" name="nuevaContra" class="form-control input-formulario" id="nuevaContra" placeholder="Tu nueva contraseña:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="confirmarContra" class="form-label fw-bold label-formulario">Confirmar Contraseña:</label>
                    <input type="password" name="confirmarContra" class="form-control input-formulario" id="confirmarContra" placeholder="Confirma tu contraseña:" required>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-primary fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
                        <i class="bi bi-check-lg me-2" style="font-size: 1.2rem;"></i> Guardar contraseña nueva
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>