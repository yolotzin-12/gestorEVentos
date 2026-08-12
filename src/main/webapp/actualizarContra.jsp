<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actualizar Contraseña - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="bg-light">

<div class="container d-flex justify-content-center align-items-center min-vh-100">
    <div class="card p-4 shadow-sm tarjeta-personalizada border-0 rounded-4" style="max-width: 450px; width: 100%; background-color: #f1f5f4;">
        <div class="card-body text-center">

            <img src="img/logo.png" alt="Logo SRAE" class="mb-4" style="max-height: 110px;">

            <div class="alert alert-danger ${empty error ? 'd-none' : ''}" role="alert">
                ${error}
            </div>
            <div class="alert alert-success ${empty mensaje ? 'd-none' : ''}" role="alert">
                ${mensaje}
            </div>

            <form action="login.jsp" method="GET">

                <div class="mb-3 text-start">
                    <label for="nuevaContrasena" class="form-label fw-bold text-dark">Nueva contraseña:</label>
                    <input type="password" id="nuevaContrasena" name="nuevaContrasena" class="form-control rounded-3 py-2 px-3" placeholder="Tu contraseña:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="confirmarContrasena" class="form-label fw-bold text-dark">Confirmar contraseña:</label>
                    <input type="password" id="confirmarContrasena" name="confirmarContrasena" class="form-control rounded-3 py-2 px-3" placeholder="Tu contraseña:" required>
                </div>

                <div class="text-center mt-3">
                    <button type="submit" class="btn btn-success fw-bold py-2 px-4 rounded-3 d-inline-flex align-items-center justify-content-center w-100" style="background-color: #008767; border: none; font-size: 1.1rem;">
                        <i class="bi bi-check-lg me-2" style="font-size: 1.4rem;"></i> Confirmar contraseña
                    </button>
                </div>

            </form>

        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>