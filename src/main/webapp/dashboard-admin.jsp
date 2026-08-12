<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eventos - SRAE</title>

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
                <a href="evento" class="activo">Eventos</a>
                <a href="administrarUsu.jsp">Usuarios</a>
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

        <div class="barra-filtros">
            <div class="filtro-ubicacion" id="filtroUbicacion">
                <div class="pin-ubicacion"><i class="bi bi-geo-alt-fill"></i></div>
                <div class="chip-ubicacion">
                    Campus UTEZ
                    <i class="bi bi-x-lg" onclick="quitarUbicacion()" title="Quitar filtro"></i>
                </div>
            </div>

            <div class="buscador-evento">
                <i class="bi bi-search"></i>
                <input type="text" name="buscar" placeholder="Buscar evento" autocomplete="off">
            </div>
        </div>

        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="alert alert-info text-center rounded-4 shadow-sm">
                    <i class="bi bi-info-circle-fill fs-4 d-block mb-2"></i>
                    Todavía no hay eventos registrados.
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4 mb-4" id="listaEventosGrid">
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3" data-nombre-evento="${fn:toLowerCase(evento.nombre)}">
                            <a href="evento?id=${evento.id}" class="tarjeta-evento-link">
                                <div class="tarjeta-evento">
                                    <div class="encabezado-evento">
                                        <h3>${evento.nombre}</h3>
                                        <p>${evento.categoria}</p>
                                    </div>
                                    <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                    <div class="pie-evento">
                                        <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> ${evento.ubicacion}</div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </div>
                <div id="sinResultadosBusqueda" class="alert alert-secondary text-center rounded-4 shadow-sm" style="display:none;">
                    <i class="bi bi-search"></i> No se encontraron eventos con ese nombre.
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

<footer class="footer-eventos">
    <div><i class="bi bi-people-fill"></i> CONTACTANOS</div>
    <div><i class="bi bi-telephone-fill"></i> 777-0000-000</div>
    <div><i class="bi bi-envelope-fill"></i> CORREO@UTEZ.EDU.MX</div>
    <div><i class="bi bi-geo-alt-fill"></i> UBICACIÓN</div>
</footer>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/buscador.js"></script>
</body>
</html>
