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
<div class="container d-flex flex-column justify-content-center align-items-center min-vh-100">
    <img src="img/utez.png" alt="Logo SRAE" class="mb-4" style="max-height: 150px;">

    <div class="card p-4 shadow-lg tarjeta-personalizada">
        <div class="card-body text-center px-4">
            <input type="hidden" id="serverError"
                   value="<%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>">
            <input type="hidden" id="serverMensaje"
                   value="<%= request.getAttribute("mensaje") != null ? request.getAttribute("mensaje") : "" %>">
            <div id="alertasContainer"></div>
            <form action="login" method="POST">
                <div class="mb-3 text-start">
                    <label for="email" class="form-label fw-bold label-formulario mb-1">Correo electrónico</label>
                    <input type="email" name="email" class="form-control input-formulario py-2 px-3" id="email"
                           placeholder="Correo electrónico:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="contra" class="form-label fw-bold label-formulario mb-1">Contraseña</label>
                    <div class="input-group">
                        <input type="password" name="contra" class="form-control input-formulario py-2 px-3" id="contra"
                               placeholder="Contraseña:" required>
                        <button class="btn btn-outline-secondary btn-ver-password" type="button" onclick="togglePassword('contra', this)">
                            <i class="bi bi-eye-fill"></i>
                        </button>
                    </div>
                </div>

                <div class="text-center mb-4 mt-2">
                    <a href="recuperarContra.jsp" class="text-decoration-none enlace-oscuro fs-6">¿Olvidaste tu contraseña?</a>
                </div>

                <div class="text-center mb-4">
                    <button type="submit"
                            class="btn btn-ingresar text-white fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
                        <i class="bi bi-box-arrow-in-right me-2" style="font-size: 1.3rem;"></i> Iniciar sesión
                    </button>
                </div>

                <div class="text-center mt-2">
                    <span class="fw-bold" style="color: #1b365d;">¿No tienes cuenta?</span>
                    <a href="registro.jsp" class="text-decoration-none enlace-oscuro" style="color: #2895d3;">Regístrate aquí</a>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/validaciones.js"></script>
<script src="js/utilidades.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>