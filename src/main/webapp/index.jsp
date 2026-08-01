<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                <img src="img/letras.png" alt="SRAE" style="height:120px;">
            </div>

            <nav class="eventos-nav">
                <a href="eventos.jsp" class="activo">Eventos</a>
                <a href="categorias.jsp">Categorias</a>
                <a href="misReservas.jsp">Mis reservas</a>
            </nav>

            <div class="d-flex align-items-center gap-3">
                <c:choose>
                    <c:when test="${not empty sessionScope.usuario}">
                        <a href="crearPerfil.jsp" class="d-flex align-items-center gap-2 text-decoration-none">
                            <c:choose>
                                <c:when test="${not empty sessionScope.usuario.foto}">
                                    <img src="${sessionScope.usuario.foto}" alt="Perfil" class="rounded-circle border border-2 border-white" style="width: 38px; height: 38px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <div class="icono-usuario">
                                        <i class="bi bi-person"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <span class="fw-bold text-white fs-6">${sessionScope.usuario.nombre}</span>
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="crearPerfil.jsp" class="icono-usuario" title="Perfil / Iniciar Sesión">
                            <i class="bi bi-person"></i>
                        </a>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/logout" class="btn-logout-eventos" title="Cerrar sesión">
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
                <input type="text" name="buscar" placeholder="Buscar evento">
            </div>
        </div>

        <div class="row g-4 mb-4">
            <c:choose>
                <c:when test="${empty listaEventos}">
                    <%-- Tarjetas de ejemplo mientras no haya datos dinámicos --%>
                    <c:forEach begin="1" end="4">
                        <div class="col-6 col-md-3">
                            <a href="detalleEvento.jsp" class="tarjeta-evento-link">
                                <div class="tarjeta-evento">
                                    <div class="encabezado-evento">
                                        <h3>Evento Nombre</h3>
                                        <p>Desc desc desc desc</p>
                                    </div>
                                    <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                    <div class="pie-evento">
                                        <div><i class="bi bi-calendar-event"></i> 4 JUL | 11:00 AM</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> Auditorio Pacheco UTEZ</div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3">
                            <a href="evento?id=${evento.id}" class="tarjeta-evento-link">
                                <div class="tarjeta-evento">
                                    <div class="encabezado-evento">
                                        <h3>${evento.nombre}</h3>
                                        <p>${evento.categoria}</p>
                                    </div>
                                    <img src="${not empty evento.foto ? evento.foto : 'img/personas.jpg'}" alt="Evento" class="imagen-evento">
                                    <div class="pie-evento">
                                        <div><i class="bi bi-calendar-event"></i> ${evento.fecha}</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> ${evento.ubicacion}</div>
                                    </div>
                                </div>
                            </a>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

    </div>

</main>

<footer class="footer-eventos">
    <div><i class="bi bi-people-fill"></i> CONTÁCTANOS</div>
    <div><i class="bi bi-telephone-fill"></i> 777-0000-000</div>
    <div><i class="bi bi-envelope-fill"></i> CORREO@UTEZ.EDU.MX</div>
    <div><i class="bi bi-geo-alt-fill"></i> UBICACIÓN</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function quitarUbicacion() {
        document.getElementById('filtroUbicacion').style.display = 'none';
    }
</script>
</body>
</html>