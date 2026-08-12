<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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

            <img src="img/utez.png" alt="Logo UTEZ" class="mb-4" style="max-height: 100px;">

            <h4 class="fw-bold mb-4 text-dark">Recuperar contraseña</h4>
            <div class="alert alert-danger ${empty error ? 'd-none' : ''}" role="alert">
                ${error}
            </div>
            <div class="alert alert-success ${empty mensaje ? 'd-none' : ''}" role="alert">
                ${mensaje}
            </div>
            <form action="${pageContext.request.contextPath}/recuperar" method="POST">

                <div class="mb-4 text-start">
                    <label for="email" class="form-label fw-bold text-secondary text-center d-block">Ingresa tu correo</label>
                    <!-- Es vital que exista name="email" para que el Servlet lo encuentre -->
                    <input type="email" id="email" name="email" class="form-control" placeholder="ejemplo@utez.edu.mx" required>
                </div>

                <!-- Tu botón existente -->
                <div class="text-center mt-2">
                    <button type="submit" class="btn btn-primary fw-bold py-2 px-5 d-inline-flex">
                        <i class="bi bi-envelope me-2" style="font-size: 1.2rem;"></i> Generar enlace
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>