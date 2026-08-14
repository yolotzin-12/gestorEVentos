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

    <style>
        /* Estilos para los colores de las validaciones */
        .cumplido { color: #198754; transition: color 0.3s ease; }
        .no-cumplido { color: #dc3545; transition: color 0.3s ease; }
    </style>
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center min-vh-100">

    <div class="card p-4 shadow-sm tarjeta-personalizada">
        <div class="card-body text-center">

            <img src="img/utez.png" alt="Logo SRAE" class="mb-4" style="max-height: 100px;">

            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger d-flex align-items-center py-2 text-start" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div class="small">
                    <%= request.getAttribute("error") %>
                </div>
            </div>
            <% } %>

            <form id="formularioRecuperar" action="restablecer" method="POST">

                <input type="hidden" name="token" value="<%= token %>">

                <div class="mb-3 text-start">
                    <label for="contraNew" class="form-label fw-bold label-formulario">Nueva Contraseña:</label>
                    <div class="input-group">
                        <input type="password" name="nuevaContra" class="form-control input-formulario" id="contraNew" placeholder="Tu nueva contraseña:" required>
                        <button class="btn btn-outline-secondary toggle-password" type="button" data-target="contraNew">
                            <i class="bi bi-eye-slash"></i>
                        </button>
                    </div>
                </div>

                <ul id="listaRequisitos" class="text-start" style="display: none; list-style-type: none; padding-left: 5px; font-size: 0.9em; margin-bottom: 15px;">
                    <li id="req-longitud" class="no-cumplido mb-1"><i class="bi bi-x-circle-fill me-1"></i> Mínimo 8 caracteres</li>
                    <li id="req-mayuscula" class="no-cumplido mb-1"><i class="bi bi-x-circle-fill me-1"></i> Una letra mayúscula</li>
                    <li id="req-minuscula" class="no-cumplido mb-1"><i class="bi bi-x-circle-fill me-1"></i> Una letra minúscula</li>
                    <li id="req-numero" class="no-cumplido mb-1"><i class="bi bi-x-circle-fill me-1"></i> Un número</li>
                </ul>

                <div class="mb-4 text-start">
                    <label for="confirmarContra" class="form-label fw-bold label-formulario">Confirmar Contraseña:</label>
                    <div class="input-group">
                        <input type="password" name="confirmarContra" class="form-control input-formulario" id="confirmarContra" placeholder="Confirma tu contraseña:" required>
                        <button class="btn btn-outline-secondary toggle-password" type="button" data-target="confirmarContra">
                            <i class="bi bi-eye-slash"></i>
                        </button>
                    </div>
                    <div id="errorCoincidencia" class="text-danger mt-2" style="display: none; font-size: 0.85em;">
                        Las contraseñas no coinciden.
                    </div>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-ingresar text-white fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center" style="background-color: #008a62; border: none;">
                        <i class="bi bi-check-lg me-2" style="font-size: 1.2rem;"></i> Guardar contraseña nueva
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>
<script src="js/recuperarcontra.js"></script>

</body>
</html>