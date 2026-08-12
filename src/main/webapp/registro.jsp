<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center min-vh-100">

    <div class="card p-4 shadow-sm tarjeta-personalizada">
        <div class="card-body text-center">

            <div class="d-flex flex-column align-items-center mb-4">
                <img src="img/LOGOOO.png" alt="Logo SRAE" style="max-height: 150px;">
            </div>

            <input type="hidden" id="serverErrorRegistro" value="${error}">

            <c:if test="${not empty mensaje}">
                <div class="alert alert-info d-flex align-items-center py-2 text-start" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i>
                    <div class="small">
                            ${mensaje}
                    </div>
                </div>
            </c:if>

            <form action="register" method="post" id="formRegistro">

                <div class="mb-3 text-start">
                    <label for="txtNombre" class="form-label fw-bold label-formulario">Nombre(s):</label>
                    <input type="text" name="nombre" value="${param.nombre}" class="form-control input-formulario" id="txtNombre" placeholder="Tu nombre(s):" pattern="[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+" title="Solo se permiten letras y espacios" required>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3 text-start">
                        <label for="txtApellidoPaterno" class="form-label fw-bold label-formulario">Apellido Paterno:</label>
                        <input type="text" name="apellidoPaterno" value="${param.apellidoPaterno}" class="form-control input-formulario" id="txtApellidoPaterno" placeholder="Primer apellido:" pattern="[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+" title="Solo se permiten letras y espacios" required>
                    </div>

                    <div class="col-md-6 mb-3 text-start">
                        <label for="txtApellidoMaterno" class="form-label fw-bold label-formulario">Apellido Materno:</label>
                        <input type="text" name="apellidoMaterno" value="${param.apellidoMaterno}" class="form-control input-formulario" id="txtApellidoMaterno" placeholder="Segundo apellido:" pattern="[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+" title="Solo se permiten letras y espacios" required>
                    </div>
                </div>

                <div class="mb-3 text-start">
                    <label for="txtCorreo" class="form-label fw-bold label-formulario ${not empty errorEmail ? 'text-danger' : ''}">Correo Electrónico:</label>
                    <input type="email" name="email" value="${param.email}" class="form-control input-formulario ${not empty errorEmail ? 'is-invalid' : ''}" id="txtCorreo" placeholder="Tu correo electrónico:" pattern="^[a-zA-Z0-9._%+\-]+@(gmail\.com|hotmail\.com|yahoo\.com|outlook\.com|utez\.edu\.mx)$" title="Dominios permitidos: gmail.com, hotmail.com, yahoo.com, outlook.com, utez.edu.mx" required>
                </div>

                <div class="mb-3 text-start">
                    <label for="txtCorreoConfirmacion" class="form-label fw-bold label-formulario ${not empty errorEmail ? 'text-danger' : ''}">Confirmar Correo:</label>
                    <input type="email" name="emailConfirmacion" value="${param.emailConfirmacion}" class="form-control input-formulario ${not empty errorEmail ? 'is-invalid' : ''}" id="txtCorreoConfirmacion" placeholder="Confirme su correo electronico:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="txtPassword" class="form-label fw-bold label-formulario ${not empty errorContra ? 'text-danger' : ''}">Contraseña:</label>
                    <div class="input-group">
                        <input type="password" name="contra" class="form-control input-formulario ${not empty errorContra ? 'is-invalid' : ''}" id="txtPassword" placeholder="Crea una contraseña:" required>
                        <button class="btn btn-outline-secondary btn-ver-password" type="button" onclick="togglePassword('txtPassword', this)">
                            <i class="bi bi-eye-fill"></i>
                        </button>
                    </div>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-ingresar text-white fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
                        <i class="bi bi-person-plus-fill me-2" style="font-size: 1.2rem;"></i> Registrarme
                    </button>
                </div>

                <div class="text-center mt-3">
                    <a href="login.jsp" class="text-decoration-none enlace-oscuro">¿Ya tienes una cuenta? Inicia sesión</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/utilidades.js"></script>
<script src="js/alertaregistro.js"></script>

</body>
</html>