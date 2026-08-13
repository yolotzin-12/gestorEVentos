<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">
<div class="container d-flex justify-content-center align-items-center min-vh-100">

    <div class="card p-4 shadow-sm tarjeta-personalizada position-relative" style="max-width: 450px; width: 100%;">

        <a href="${pageContext.request.contextPath}/login.jsp" class="text-secondary fs-4 position-absolute" style="top: 16px; right: 20px;" title="Regresar al inicio de sesión">
            <i class="bi bi-x-lg"></i>
        </a>

        <div class="card-body text-center">

            <img src="img/utez.png" alt="Logo UTEZ" class="mb-4" style="max-height: 100px;">

            <h4 class="fw-bold mb-4 text-dark">Recuperar contraseña</h4>

            <c:if test="${empty correoNoExistente}">
                <div class="alert alert-danger ${empty error ? 'd-none' : ''}" role="alert">
                        ${error}
                </div>
            </c:if>
            <div class="alert alert-success ${empty mensaje ? 'd-none' : ''}" role="alert">
                ${mensaje}
            </div>

            <form action="${pageContext.request.contextPath}/recuperar" method="POST">

                <div class="mb-4 text-start">
                    <label for="email" class="form-label fw-bold text-secondary text-center d-block">Ingresa tu correo</label>
                    <input type="email" id="email" name="email" class="form-control" placeholder="ejemplo@utez.edu.mx" required>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-primary fw-bold py-2 px-5 d-inline-flex">
                        <i class="bi bi-envelope me-2" style="font-size: 1.2rem;"></i> Generar enlace
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<!-- Modal: se muestra solo cuando el correo ingresado no está registrado -->
<div class="modal fade" id="modalCorreoNoExiste" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4">
            <div class="modal-header border-0">
                <h5 class="modal-title fw-bold" style="color:#162e54;">
                    <i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>Correo no existente
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <p class="mb-0">${error}</p>
            </div>
            <div class="modal-footer border-0">
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-outline-secondary fw-bold px-4">
                    <i class="bi bi-x-lg me-1"></i> Cancelar
                </a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-success fw-bold px-4">
                    <i class="bi bi-person-plus me-1"></i> Registrarse
                </a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

<c:if test="${not empty correoNoExistente}">
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            var modal = new bootstrap.Modal(document.getElementById('modalCorreoNoExiste'));
            modal.show();
        });
    </script>
</c:if>

</body>
</html>