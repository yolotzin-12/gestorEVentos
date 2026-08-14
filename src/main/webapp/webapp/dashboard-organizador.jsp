<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Eventos - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/eventos.css">
</head>
<body class="eventos-body d-flex flex-column min-vh-100">

<main class="flex-grow-1">

    <jsp:include page="navbar.jsp">
        <jsp:param name="activePage" value="eventos" />
    </jsp:include>

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
                <input type="text" name="buscar" placeholder="Buscar en mis eventos" autocomplete="off">
            </div>
        </div>

        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="alert alert-info text-center rounded-4 shadow-sm">
                    <i class="bi bi-info-circle-fill fs-4 d-block mb-2"></i>
                    Aún no has creado ningún evento.
                    <a href="evento?action=nuevo" class="d-block mt-2 fw-bold">Crea tu primer evento</a>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4 mb-4" id="listaEventosGrid">
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3" data-nombre-evento="${fn:toLowerCase(evento.nombre)}">
                            <div class="tarjeta-evento">
                                <a href="evento?id=${evento.id}" class="tarjeta-evento-link">
                                    <div class="encabezado-evento">
                                        <h3>${evento.nombre}</h3>
                                        <p>${evento.categoria} &middot; ${evento.estado}</p>
                                    </div>
                                    <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                    <div class="pie-evento">
                                        <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> ${evento.ubicacion}</div>
                                    </div>
                                </a>
                                <form action="evento" method="post" onsubmit="return confirm('¿Eliminar este evento?');" class="p-2">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="${evento.id}">
                                    <button type="submit" class="btn btn-sm btn-outline-danger w-100">
                                        <i class="bi bi-trash"></i> Eliminar
                                    </button>
                                </form>
                            </div>
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
<script src="js/cierresesion.js"></script>

</body>
</html>
