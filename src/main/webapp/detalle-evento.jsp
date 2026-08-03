<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del Evento - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/eventos.css">
</head>
<body class="eventos-body d-flex flex-column min-vh-100">

<main class="flex-grow-1">


    <header class="eventos-header">
        <div class="container d-flex justify-content-between align-items-center flex-wrap gap-3">

            <div class="d-flex align-items-center gap-2">
                <img src="img/logo.png" alt="Logo SRAE" style="height:70px;">
                <img src="img/letras.png" alt="SRAE" style="height:120px;">            </div>

            <nav class="eventos-nav">
                <a href="eventos.jsp">Eventos</a>
                <a href="categorias.jsp">Categorias</a>
                <a href="historialReservas.jsp">Mis reservas</a>
            </nav>

            <div class="d-flex align-items-center gap-2">
                <a href="crearPerfil.jsp" class="icono-usuario">
                    <i class="bi bi-person"></i>
                </a>
                <a href="logout" class="btn-logout-eventos">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>

        </div>
    </header>

    <hr class="divisor-teal">

    <div class="container">

        <a href="eventos.jsp" class="btn btn-sm mb-3" style="background-color:#e4e4e6; color:#162e54; font-weight:700; border-radius:10px;">
            <i class="bi bi-arrow-left"></i> Volver a eventos
        </a>

        <c:choose>
            <c:when test="${not empty evento}">
                <div class="card p-4 shadow-sm border-0 rounded-4 bg-white mb-4">
                    <div class="row g-4">
                        <div class="col-md-6">
                            <img src="img/personas.jpg" alt="Evento" class="img-fluid rounded-4" style="width:100%; height:320px; object-fit:cover;">
                        </div>
                        <div class="col-md-6 d-flex flex-column justify-content-between">
                            <div>
                                <span class="badge mb-2" style="background-color:#0d8a5f;">${evento.categoria}</span>
                                <h3 class="fw-bold" style="color:#162e54;">${evento.nombre}</h3>
                                <p class="text-secondary mb-1"><i class="bi bi-calendar-event me-2" style="color:#0d8a5f;"></i>${evento.fecha}</p>
                                <p class="text-secondary mb-1"><i class="bi bi-geo-alt-fill me-2" style="color:#0d8a5f;"></i>${evento.ubicacion}</p>
                                <p class="text-secondary mb-3"><i class="bi bi-people-fill me-2" style="color:#0d8a5f;"></i>Aforo máximo: ${evento.capacidad} personas</p>
                            </div>
                            <a href="reservar.jsp" class="text-decoration-none w-100">
                                <button type="button" class="btn fs-5 w-100 d-flex align-items-center justify-content-center gap-2" style="background-color:#0d8a5f; color:#fff; font-weight:bold; border-radius:10px; padding:10px; border:none;">
                                    <i class="bi bi-calendar-check"></i> Reservar / Registrarse
                                </button>
                            </a>
                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card p-4 shadow-sm border-0 rounded-4 bg-white mb-4">
                    <div class="row g-4">
                        <div class="col-md-6">
                            <img src="img/personas.jpg" alt="Evento" class="img-fluid rounded-4" style="width:100%; height:320px; object-fit:cover;">
                        </div>
                        <div class="col-md-6 d-flex flex-column justify-content-between">
                            <div>
                                <span class="badge mb-2" style="background-color:#0d8a5f;">Categoría</span>
                                <h3 class="fw-bold" style="color:#162e54;">Evento Nombre</h3>
                                <p class="text-secondary mb-3">Desc desc desc desc</p>
                                <p class="text-secondary mb-1"><i class="bi bi-calendar-event me-2" style="color:#0d8a5f;"></i>4 JUL | 11:00 AM</p>
                                <p class="text-secondary mb-1"><i class="bi bi-geo-alt-fill me-2" style="color:#0d8a5f;"></i>Auditorio Pacheco UTEZ</p>
                            </div>
                            <a href="reservar.jsp" class="text-decoration-none w-100">
                                <button type="button" class="btn fs-5 w-100 d-flex align-items-center justify-content-center gap-2" style="background-color:#0d8a5f; color:#fff; font-weight:bold; border-radius:10px; padding:10px; border:none;">
                                    <i class="bi bi-calendar-check"></i> Reservar / Registrarse
                                </button>
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>



<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
