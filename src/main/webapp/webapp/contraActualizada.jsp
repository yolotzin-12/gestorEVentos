<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contraseña Actualizada - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/confirmacion.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<div class="container d-flex flex-column justify-content-center align-items-center min-vh-100">

    <div class="mb-4 text-center">
        <img src="img/utez.png" alt="Logo UTEZ" style="max-height: 80px;">
    </div>

    <div class="card p-4 shadow-sm border-0 tarjeta-confirmacion text-center mb-4">
        <div class="card-body p-2">

            <div class="mb-3">
                <div class="icono-check-verde">
                    <i class="bi bi-check-lg"></i>
                </div>
            </div>

            <h4 class="fw-bold text-dark mb-4">Contraseña Actualizada<br>Correctamente</h4>

        </div>
    </div>

    <div class="text-center">
        <a href="login.jsp" class="btn btn-ingresar text-white fw-bold py-2 px-5 d-inline-flex align-items-center justify-content-center">
            Volver a Iniciar Sesión
        </a>
    </div>

</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>