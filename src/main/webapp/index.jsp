<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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

    <div class="mb-3 text-center">
        <img src="img/logo.png" alt="Logo SRAE" style="max-height: 90px;">
    </div>

    <div class="card p-4 shadow-sm tarjeta-personalizada mb-4">
        <div class="card-body">
            <form action="paginaPrincipal.jsp" method="post">

                <div class="mb-3 text-start">
                    <label for="email" class="form-label fw-bold label-formulario">Correo electrónico</label>
                    <input type="email" name="email" class="form-control input-formulario" id="email" placeholder="Correo electrónico:" required>
                </div>

                <div class="mb-4 text-start">
                    <label for="password" class="form-label fw-bold label-formulario">Contraseña</label>
                    <input type="password" name="password" class="form-control input-formulario" id="password" placeholder="Contraseña:" required>
                </div>

                <div class="text-center mb-4">
                    <a href="recuperarContra.jsp" class="text-decoration-none fw-bold" style="color: #2b4c7e;">¿Olvidaste tu contraseña?</a>
                </div>

                <div class="text-center mb-4">
                    <button type="submit" class="btn text-white fw-bold py-2 px-4 d-inline-flex align-items-center justify-content-center btn-ingresar" style="background-color: #058971 !important; color: #ffffff !important; border: none !important; border-radius: 10px !important; box-shadow: 0px 4px 6px rgba(0,0,0,0.15) !important;">
                        <i class="bi bi-box-arrow-in-right me-2" style="font-size: 1.4rem; color: #ffffff !important;"></i> Iniciar sesión
                    </button>
                </div>

                <div class="text-center">
                    <span class="text-dark fw-bold">¿No tienes cuenta?</span>
                    <a href="login.jsp" class="text-decoration-none fw-bold" style="color: #2b4c7e;">Regístrate aquí</a>
                </div>

            </form>
        </div>
    </div>

    <div>
        <button class="btn text-white fw-bold py-2 px-5 btn-srae rounded-pill">
            Acerca de
        </button>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>