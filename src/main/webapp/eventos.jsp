<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                <!-- Texto dinámico según el Rol del usuario -->
                <a href="${pageContext.request.contextPath}/evento" class="activo">
                    <c:choose>
                        <c:when test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 2}">
                            Mis eventos
                        </c:when>
                        <c:otherwise>
                            Eventos
                        </c:otherwise>
                    </c:choose>
                </a>

                <c:if test="${sessionScope.usuario != null && (sessionScope.usuario.idRol == 1 || sessionScope.usuario.idRol == 2)}">
                    <!-- Redirección directa al Servlet -->
                    <a href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                </c:if>
            </nav>

            <div class="d-flex align-items-center gap-2">
                <c:if test="${sessionScope.usuario != null && (sessionScope.usuario.idRol == 1 || sessionScope.usuario.idRol == 2)}">
                    <a href="evento?action=crear" class="btn btn-success btn-sm fw-bold me-2" style="background-color: #0d8a5f; border: none;">
                        <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                    </a>
                </c:if>
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
            <div class="buscador-evento w-100">
                <i class="bi bi-search"></i>
                <input type="text" name="buscar" placeholder="Buscar evento">
            </div>
        </div>

        <div class="row g-4 mb-4">
            <c:choose>
                <c:when test="${empty listaEventos}">
                    <div class="col-12 text-center py-5">
                        <i class="bi bi-calendar-x text-muted fs-1"></i>
                        <p class="mt-2 text-muted fw-bold">No hay eventos disponibles por el momento.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3">
                            <a href="evento?action=detalle&id=${evento.id}" class="tarjeta-evento-link">
                                <div class="tarjeta-evento">
                                    <div class="encabezado-evento">
                                        <h3>${evento.nombre}</h3>
                                        <p><c:out value="${evento.nombreCategoria}" default="General"/></p>
                                    </div>

                                    <c:choose>
                                        <c:when test="${not empty evento.imagenUrl}">
                                            <img src="${evento.imagenUrl}" alt="${evento.nombre}" class="imagen-evento">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                        </c:otherwise>
                                    </c:choose>

                                    <div class="pie-evento">
                                        <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> <c:out value="${evento.ubicacion}" default="Sin ubicación"/></div>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>