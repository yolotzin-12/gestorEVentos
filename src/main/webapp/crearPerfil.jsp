<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">

    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/perfil.css">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<header>
    <div class="logo">
        <img src="img/logo.png" width="100" alt="Logo">
        <img src="img/letras.png" width="210" alt="SRAE">
    </div>
</header>

<div class="text-center my-4">
    <h3 class="fw-bold" style="border-bottom: 3px solid #0d8a5f; display: inline-block; padding-bottom: 5px;">MI PERFIL</h3>
</div>

<div class="container-fluid px-5">
    <div class="row">

        <div class="col-md-3 pe-4">
            <div class="d-flex flex-column">
                <a href="paginaPrincipal.jsp" class="btn sidebar-btn py-3 px-4 fw-bold"><i class="bi bi-house-door me-3"></i> Inicio</a>
                <a href="historialReservas.jsp" class="btn sidebar-btn py-3 px-4 fw-bold"><i class="bi bi-calendar-check me-3"></i> Reservas</a>
                <a href="administrarUsu.jsp" class="btn sidebar-btn py-3 px-4 fw-bold"><i class="bi bi-people me-3"></i> Usuarios</a>
                <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold active" style="background-color: #e2e6ea;"><i class="bi bi-person me-3"></i> Mi perfil</a>
                <a href="index.jsp" class="btn sidebar-btn py-3 px-4 fw-bold text-danger"><i class="bi bi-box-arrow-left me-3"></i> Salir</a>
            </div>
        </div>

        <div class="col-md-5 pe-3">
            <div class="card-seccion shadow-sm">
                <h5 class="fw-bold mb-3">Datos Personales</h5>

                <div class="avatar-perfil text-center flex-column">
                    <i class="bi bi-person-fill fs-2"></i>
                    <small style="font-size: 0.7rem;">Cambiar imagen</small>
                </div>

                <form action="paginaPrincipal.jsp" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="nombre" class="form-label fw-bold">Nombre:</label>
                            <input type="text" name="nombre" class="form-control" id="nombre" placeholder="Nombre:" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="apeP" class="form-label fw-bold">Apellido Paterno:</label>
                            <input type="text" name="apeP" class="form-control" id="apeP" placeholder="Apellido Paterno" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="apeM" class="form-label fw-bold">Apellido Materno:</label>
                            <input type="text" name="apeM" class="form-control" id="apeM" placeholder="Apellido Materno:" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="telefono" class="form-label fw-bold">Teléfono:</label>
                            <input type="tel" name="telefono" class="form-control" id="telefono" placeholder="777-0000-000">
                        </div>
                        <div class="col-md-12 mb-4">
                            <label for="correo" class="form-label fw-bold">Correo Electrónico:</label>
                            <div class="input-group">
                                <input type="email" name="correo" class="form-control" id="correo" placeholder="correo@utez.edu.mx" required>
                                <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                            </div>
                        </div>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-success fw-bold px-4" style="background-color: #0d8a5f;"><i class="bi bi-floppy me-2"></i> Guardar</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card-seccion shadow-sm">
                <h5 class="fw-bold mb-3">Seguridad</h5>
                <h6 class="text-muted mb-3">Cambiar contraseña</h6>

                <form action="login.jsp" method="post">
                    <div class="mb-3">
                        <label for="contraActual" class="form-label fw-bold">Contraseña actual:</label>
                        <input type="password" name="contraActual" class="form-control" id="contraActual" placeholder="Contraseña actual:" required>
                    </div>
                    <div class="mb-3">
                        <label for="contraNew" class="form-label fw-bold">Contraseña nueva:</label>
                        <input type="password" name="contraNew" class="form-control" id="contraNew" placeholder="Contraseña Nueva:" required>
                    </div>
                    <div class="mb-4">
                        <label for="confirmarContra" class="form-label fw-bold">Confirmar contraseña:</label>
                        <input type="password" name="confirmarContra" class="form-control" id="confirmarContra" placeholder="Confirmar contraseña:" required>
                    </div>
                    <div class="text-center">
                        <button type="submit" class="btn btn-success fw-bold px-4" style="background-color: #0d8a5f;"><i class="bi bi-arrow-clockwise me-2"></i> Actualizar</button>
                    </div>
                </form>
            </div>
        </div>

    </div>
</div>

<br>
<footer>
    <div><i class="bi bi-people-fill"></i> CONTÁCTANOS</div>
    <div><i class="bi bi-telephone-fill"></i> 777-0000-000</div>
    <div><i class="bi bi-envelope-fill"></i> CORREO@UTEZ.EDU.MX</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>