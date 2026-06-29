<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Principal</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<header>
    <div class="logo">
        <img src="img/logo.png" width="100" alt="Logo">
        <img src="img/letras.png" width="210" alt="SRAE">
    </div>
    <i class="bi bi-list"></i>
</header>

<br>

<div class="contenedor">
    <div class="principal">
        <img src="img/personas.jpg" alt="Personas">

        <div class="informacion">
            <h2>EVENTO NOMBRE</h2>
            <div class="detalle">
                <div>
                    <p>Descripción del evento</p>
                    <p>Descripción del evento</p>
                </div>
                <a href="registroUsuarios.jsp">
                    <button type="button" class="btn btn-success">
                        <i class="bi bi-calendar-event"></i> Reservar / Registrarse
                    </button>
                </a>
            </div>
        </div>
    </div>

    <div class="lateral">
        <div class="tarjeta">
            <h4>EVENTO SECUNDARIO</h4>
            <p>15 de junio del 2026</p>
        </div>
        <div class="tarjeta">
            <h4>EVENTO SECUNDARIO</h4>
            <p>15 de junio del 2026</p>
        </div>
        <div class="tarjeta">
            <h4>Galería de eventos</h4>
            <p>Revive nuestros mejores momentos</p>
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