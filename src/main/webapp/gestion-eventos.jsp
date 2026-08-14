<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión - SRAE</title>

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }

        .main-card {
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            padding: 2rem;
            margin-top: 1.5rem;
            margin-bottom: 2rem;
        }

        /* Menú Lateral (Sidebar) */
        .sidebar-menu {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .sidebar-btn {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 14px 20px;
            border-radius: 10px;
            font-weight: 600;
            text-decoration: none;
            color: #2c3e50;
            background-color: #ffffff;
            border: 1px solid #e2e8f0;
            transition: all 0.2s ease;
        }

        .sidebar-btn:hover {
            background-color: #f8fafc;
            color: #162e54;
        }

        .sidebar-btn.active {
            background-color: #11294a;
            color: #ffffff;
            border-color: #11294a;
        }

        .sidebar-btn-danger {
            background-color: #cc0000;
            color: #ffffff;
            border: none;
        }

        .sidebar-btn-danger:hover {
            background-color: #a30000;
            color: #ffffff;
        }

        .section-title {
            color: #11294a;
            font-weight: 700;
            font-size: 1.5rem;
            border-bottom: 3px solid #0d8a5f;
            padding-bottom: 8px;
            margin-bottom: 1.5rem;
        }

        .tabla-custom {
            border-radius: 10px;
            overflow: hidden;
        }

        .tabla-custom thead {
            background-color: #f8fafc;
        }

        .tabla-custom th {
            color: #11294a;
            font-weight: 700;
            padding: 12px 16px;
            border-bottom: 2px solid #e2e8f0;
        }

        .btn-accion {
            width: 32px;
            height: 32px;
            padding: 0;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            border-radius: 6px;
        }
    </style>
</head>
<body>

<div class="container py-4">

    <!-- Header Superior -->
    <header class="d-flex justify-content-between align-items-center mb-3 px-2">
        <div class="d-flex align-items-center gap-3">
            <img src="img/logo.png" alt="Logo SRAE" style="height: 50px;">
            <div>
                <h5 class="fw-bold m-0" style="color: #11294a; letter-spacing: 0.5px;">SRAE</h5>
                <small class="text-muted fw-semibold" style="font-size: 0.75rem;">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm rounded-3 px-3 py-2" title="Cerrar sesión" onclick="confirmarCierreSesion(event)">
                <i class="bi bi-box-arrow-right fs-6"></i>
            </a>
        </div>
    </header>

    <!-- Contenido Principal -->
    <div class="main-card">
        <div class="row g-4">

            <!-- COLUMNA IZQUIERDA: MENÚ LATERAL -->
            <div class="col-lg-3 col-md-4">
                <nav class="sidebar-menu">
                    <%-- Inicio te lleva al catálogo público --%>
                    <a href="${pageContext.request.contextPath}/evento" class="sidebar-btn">
                        <i class="bi bi-house-door fs-5"></i>
                        <span>Inicio</span>
                    </a>

                    <%-- Eventos (gestion carga la lista completa en lugar de misEventos) --%>
                    <a href="${pageContext.request.contextPath}/evento?action=gestion" class="sidebar-btn active">
                        <i class="bi bi-calendar-event fs-5"></i>
                        <span>Eventos</span>
                    </a>

                    <%-- Usuarios solo para Administrador --%>
                    <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 1}">
                        <a href="${pageContext.request.contextPath}/usuarios" class="sidebar-btn">
                            <i class="bi bi-people fs-5"></i>
                            <span>Usuarios</span>
                        </a>
                    </c:if>

                    <%-- Mi perfil --%>
                    <a href="${pageContext.request.contextPath}/crearPerfil.jsp" class="sidebar-btn">
                        <i class="bi bi-person fs-5"></i>
                        <span>Mi perfil</span>
                    </a>

                    <%-- Salir --%>
                    <a href="${pageContext.request.contextPath}/logout" class="sidebar-btn sidebar-btn-danger mt-2" onclick="confirmarCierreSesion(event)">
                        <i class="bi bi-box-arrow-right fs-5"></i>
                        <span>Salir</span>
                    </a>
                </nav>
            </div>

            <!-- COLUMNA DERECHA: CONTENIDO DE GESTIÓN -->
            <div class="col-lg-9 col-md-8">

                <div id="seccion-eventos">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h3 class="section-title m-0">
                            ${sessionScope.usuario.idRol == 1 ? 'GESTIÓN GENERAL DE EVENTOS' : 'EVENTOS'}
                        </h3>

                        <div class="d-flex gap-2">
                            <a href="${pageContext.request.contextPath}/evento?action=crear" class="btn btn-success btn-sm fw-bold px-3 py-2" style="background-color: #0d8a5f; border: none;">
                                <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                            </a>

                            <c:if test="${sessionScope.usuario.idRol != 1 && not empty listaEventos}">
                                <a href="${pageContext.request.contextPath}/evento?action=limpiarHistorial" class="btn btn-outline-danger btn-sm fw-semibold px-3 py-2" onclick="confirmarLimpiar(event)">
                                    <i class="bi bi-broom me-1"></i> Limpiar
                                </a>
                            </c:if>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${empty listaEventos}">
                            <div class="text-center py-5 border rounded-4 bg-light">
                                <i class="bi bi-calendar-x text-muted display-4"></i>
                                <p class="mt-3 text-muted fw-bold">No hay eventos para mostrar.</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table align-middle bg-white border tabla-custom">
                                    <thead>
                                    <tr>
                                        <th>Evento</th>
                                        <th>Estado</th>
                                        <th>Fecha</th>
                                        <th>Cupos</th>
                                        <th class="text-center">Reservas</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${listaEventos}" var="evento">
                                        <tr>
                                            <td class="fw-bold" style="color: #11294a;">${evento.nombre}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${evento.eventoFinalizado}">
                                                        <span class="badge bg-secondary px-2 py-1">Finalizado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Cancelado'}">
                                                        <span class="badge bg-danger px-2 py-1">Cancelado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Borrador'}">
                                                        <span class="badge bg-warning text-dark px-2 py-1">Borrador</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success px-2 py-1">Disponible</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-nowrap" style="font-size: 0.9rem;">${evento.fechaHora}</td>
                                            <td style="font-size: 0.9rem;">
                                                <span class="fw-bold">${evento.capacidadDisponible}</span> / ${evento.capacidadMaxima}
                                            </td>
                                            <td class="text-center">
                                                <span class="badge ${evento.totalReservas > 0 ? 'bg-primary' : 'bg-light text-dark border'} px-2 py-1">
                                                    <i class="bi bi-people-fill me-1"></i>${evento.totalReservas}
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <div class="d-flex justify-content-center gap-1">
                                                    <a href="${pageContext.request.contextPath}/evento?action=detalle&id=${evento.id}" class="btn btn-outline-secondary btn-accion" title="Ver Detalle">
                                                        <i class="bi bi-eye"></i>
                                                    </a>

                                                    <c:if test="${!evento.eventoFinalizado && evento.estado != 'Cancelado'}">
                                                        <a href="${pageContext.request.contextPath}/evento?action=editar&id=${evento.id}" class="btn btn-outline-primary btn-accion" title="Editar">
                                                            <i class="bi bi-pencil"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/evento?action=cancelar&id=${evento.id}" class="btn btn-outline-warning text-dark btn-accion" title="Cancelar Evento" onclick="confirmarCancelar(event)">
                                                            <i class="bi bi-slash-circle"></i>
                                                        </a>
                                                    </c:if>

                                                    <c:if test="${sessionScope.usuario.idRol == 1 || evento.eventoFinalizado || evento.estado == 'Cancelado'}">
                                                        <a href="${pageContext.request.contextPath}/evento?action=delete&id=${evento.id}" class="btn btn-outline-danger btn-accion" title="Eliminar" onclick="confirmarEliminarEvento(event)">
                                                            <i class="bi bi-trash"></i>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>

        </div>
    </div>

</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

<script>
    function confirmarCancelar(e) {
        if (!confirm('¿Estás seguro de que deseas cancelar este evento?')) {
            e.preventDefault();
        }
    }

    function confirmarEliminarEvento(e) {
        if (!confirm('¿Deseas eliminar este evento definitivamente?')) {
            e.preventDefault();
        }
    }

    function confirmarLimpiar(e) {
        if (!confirm('¿Deseas limpiar el historial de eventos cancelados y finalizados?')) {
            e.preventDefault();
        }
    }
</script>

</body>
</html>