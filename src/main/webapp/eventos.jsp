<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Obtener objeto usuario y extraer el rol de manera segura --%>
<c:set var="u" value="${not empty sessionScope.usuario ? sessionScope.usuario : sessionScope.user}" />
<c:set var="rolUsuario" value="${not empty u.idRol ? u.idRol : (not empty u.id_rol ? u.id_rol : (not empty u.idrol ? u.idrol : sessionScope.rol))}" />

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

            <!-- NAVEGACIÓN SUPERIOR POR ROL -->
            <nav class="eventos-nav">
                <a href="${pageContext.request.contextPath}/evento" class="activo">Eventos</a>

                <%-- 'MIS RESERVAS' SOLO PARA CLIENTES (NUNCA ADMIN 1 NI ORGANIZADOR 2) --%>
                <c:if test="${rolUsuario != 1 && rolUsuario != '1' && rolUsuario != 2 && rolUsuario != '2'}">
                    <a href="${pageContext.request.contextPath}/reserva">Mis reservas</a>
                </c:if>

                <%-- 'USUARIOS' SOLO PARA ADMIN (ROL 1) --%>
                <c:if test="${rolUsuario == 1 || rolUsuario == '1'}">
                    <a href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                </c:if>
            </nav>

            <div class="d-flex align-items-center gap-2">

                <%-- '+ NUEVO EVENTO' SOLO PARA ADMIN (1) Y ORGANIZADOR (2) --%>
                <c:if test="${rolUsuario == 1 || rolUsuario == '1' || rolUsuario == 2 || rolUsuario == '2'}">
                    <a href="evento?action=crear" class="btn btn-success btn-sm fw-bold me-2" style="background-color: #0d8a5f; border: none;">
                        <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                    </a>
                </c:if>

                <a href="crearPerfil.jsp" class="icono-usuario" title="Mi Perfil">
                    <i class="bi bi-person"></i>
                </a>
                <a href="logout" class="btn-logout-eventos" title="Cerrar Sesión">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>

        </div>
    </header>

    <hr class="divisor-teal">

    <div class="container">

        <div class="barra-filtros">
            <div class="buscador-evento">
                <i class="bi bi-search"></i>
                <input type="text" name="buscar" placeholder="Buscar evento" autocomplete="off">
            </div>
        </div>

        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="col-12 text-center py-5">
                    <i class="bi bi-calendar-x text-muted fs-1"></i>
                    <p class="mt-2 text-muted fw-bold">No hay eventos disponibles por el momento.</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4 mb-4" id="listaEventosGrid">
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3"
                             data-nombre-evento="${fn:toLowerCase(evento.nombre)}"
                             data-ubicacion-evento="${fn:toLowerCase(evento.ubicacion)}">

                            <div class="tarjeta-evento d-flex flex-column h-100">
                                <a href="evento?action=detalle&id=${evento.id}" class="tarjeta-evento-link d-flex flex-column h-100" style="text-decoration: none; color: inherit;">
                                    <div class="encabezado-evento">
                                        <div class="d-flex justify-content-between align-items-start gap-2">
                                            <h3>${evento.nombre}</h3>
                                            <c:if test="${evento.estado == 'Borrador'}">
                                                <span class="badge bg-warning text-dark flex-shrink-0">Borrador</span>
                                            </c:if>
                                        </div>
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

                                    <div class="pie-evento mt-auto">
                                        <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                        <div><i class="bi bi-geo-alt-fill"></i> <c:out value="${evento.ubicacion}" default="Sin ubicación"/></div>
                                    </div>
                                </a>
                            </div>

                        </div>
                    </c:forEach>
                </div>

                <div id="sinResultadosBusqueda" class="alert alert-secondary text-center rounded-4 shadow-sm" style="display:none;">
                    <i class="bi bi-search"></i> No se encontraron eventos con ese nombre o ubicación.
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/buscador.js"></script>
<script src="js/categorias.js"></script>
</body>
</html>