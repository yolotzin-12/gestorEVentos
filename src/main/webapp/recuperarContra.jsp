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

    <div class="card p-4 shadow-sm tarjeta-personalizada" style="max-width: 450px; width: 100%;">
        <div class="card-body text-center">

            <img src="img/logo.png" alt="Logo SRAE" class="mb-4" style="max-height: 100px;">

            <h4 class="fw-bold mb-4 text-dark">Recuperar contraseña</h4>

            <c:if test="${not empty error}">
                <div class="alert alert-danger d-flex align-items-center py-2 text-start mb-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div class="small">
                            ${error}
                    </div>
                </div>
            </c:if>

            <c:if test="${not empty mensaje}">
                <div class="alert alert-success d-flex align-items-center py-2 text-start mb-3" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i>
                    <div class="small">
                            ${mensaje}
                    </div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/recuperar" method="POST">

                <div class="mb-4 text-start">
                    <label for="email" class="form-label fw-bold label-formulario">Ingresa tu correo</label>
                    <input type="email" name="email" value="${param.email}" class="form-control input-formulario" id="email" placeholder="Correo electrónico / Correo institucional" required>
                </div>

                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-primary fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
                        <i class="bi bi-envelope me-2" style="font-size: 1.2rem;"></i> Generar enlace
                    </button>
                </div>

                <div class="text-center mt-3">
                    <a href="login.jsp" class="text-decoration-none">Volver al inicio de sesión</a>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>