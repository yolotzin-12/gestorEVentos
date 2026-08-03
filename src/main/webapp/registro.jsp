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

            <img src="img/logo.png" alt="Logo SRAE" class="mb-4" style="max-height: 100px;">
            <h4 class="fw-bold mb-4 text-dark">Crear Cuenta</h4>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center py-2 text-start" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div class="small">
                            ${error}
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty mensaje}">
                <div class="alert alert-info d-flex align-items-center py-2 text-start" role="alert">
                    <i class="bi bi-info-circle-fill me-2"></i>
                    <div class="small">
                            ${mensaje}
                    </div>
                </div>
            </c:if>

            <form action="register" method="post">

                <div class="mb-3 text-start">
                    <label for="txtNombre" class="form-label fw-bold label-formulario">Nombre(s):</label>
                    <input type="text" name="nombre" value="${param.nombre}" class="form-control input-formulario" id="txtNombre" placeholder="Tu nombre(s):" required>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3 text-start">
                        <label for="txtApellidoPaterno" class="form-label fw-bold label-formulario">Apellido Paterno:</label>
                        <input type="text" name="apellidoPaterno" value="${param.apellidoPaterno}" class="form-control input-formulario" id="txtApellidoPaterno" placeholder="Primer apellido:" required>
                    </div>

                    <div class="col-md-6 mb-3 text-start">
                        <label for="txtApellidoMaterno" class="form-label fw-bold label-formulario">Apellido Materno:</label>
                        <input type="text" name="apellidoMaterno" value="${param.apellidoMaterno}" class="form-control input-formulario" id="txtApellidoMaterno" placeholder="Segundo apellido:" required>
                    </div>
                </div>

                <div class="mb-3 text-start">
                    <label for="txtCorreo" class="form-label fw-bold label-formulario">Correo Electrónico:</label>
                    <input type="email" name="email" value="${param.email}" class="form-control input-formulario" id="txtCorreo" placeholder="Tu correo electrónico:" required>
                </div>

                <div class="mb-3 text-start">
                    <label for="txtCorreoConfirmacion" class="form-label fw-bold label-formulario">Confirmar Correo:</label>
                    <input type="email" name="emailConfirmacion" value="${param.emailConfirmacion}" class="form-control input-formulario" id="txtCorreoConfirmacion" placeholder="Confirme su correo electronico:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="txtPassword" class="form-label fw-bold label-formulario">Contraseña:</label>
                    <input type="password" name="contra" class="form-control input-formulario" id="txtPassword" placeholder="Crea una contraseña:" required>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-primary fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center carga">
                        <i class="bi bi-person-plus-fill me-2" style="font-size: 1.2rem;"></i> Registrarme
                    </button>
                </div>

                <div class="text-center mt-3">
                    <a href="login.jsp" class="text-decoration-none">¿Ya tienes una cuenta? Inicia sesión</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>