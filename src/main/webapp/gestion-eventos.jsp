<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión - SRAE</title>

    <!-- Hojas de estilo generales -->
    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        /* Fuerza el color azul bajito en las celdas del encabezado (Th) */
        #tablaEventos thead th {
            background-color: #e8f0fe !important; /* Azul bajito suave */
            color: #1b365d !important;             /* Texto azul marino */
            font-weight: 700 !important;
            font-size: 0.88rem;
            text-transform: uppercase;
            letter-spacing: 0.6px;
            border-bottom: 2px solid #cbd5e1 !important;
        }
    </style>
</head>
<body class="bg-light">

<!-- Header Superior -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<div class="container my-4">

    <!-- Tarjeta Principal -->
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">
            <div class="col-12">

                <div id="seccion-eventos">

                    <!-- Encabezado superior (Sin botón de crear evento) -->
                    <div class="d-flex justify-content-between align-items-center pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f;">
                        <h4 class="fw-bold m-0" style="color: #1a1a1a;">
                            ${sessionScope.usuario.idRol == 1 ? 'GESTIÓN GENERAL DE EVENTOS' : 'EVENTOS'}
                        </h4>

                        <c:if test="${sessionScope.usuario.idRol != 1 && not empty listaEventos}">
                            <div>
                                <a href="${pageContext.request.contextPath}/evento?action=limpiarHistorial" class="btn btn-outline-danger btn-sm fw-semibold px-3 py-2 rounded-3" onclick="confirmarLimpiar(event)">
                                    <i class="bi bi-broom me-1"></i> Limpiar
                                </a>
                            </div>
                        </c:if>
                    </div>

                    <!-- Contenido / Tabla -->
                    <c:choose>
                        <c:when test="${empty listaEventos}">
                            <div class="text-center py-5 border rounded-4 bg-light">
                                <i class="bi bi-calendar-x text-muted display-4"></i>
                                <p class="mt-3 text-muted fw-bold">No hay eventos para mostrar.</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="table-responsive shadow-sm border rounded-3 overflow-hidden">
                                <table class="table table-hover align-middle m-0 bg-white" id="tablaEventos">

                                    <!-- ENCABEZADO CON AZUL BAJITO -->
                                    <thead>
                                    <tr>
                                        <th scope="col" class="py-3 px-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Evento</th>
                                        <th scope="col" class="py-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Estado</th>
                                        <th scope="col" class="py-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Fecha</th>

                                        <!-- Ocultar estas columnas si es Administrador (idRol == 1) -->
                                        <c:if test="${sessionScope.usuario.idRol != 1}">
                                            <th scope="col" class="py-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Cupos</th>
                                            <th scope="col" class="text-center py-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Reservas</th>
                                            <th scope="col" class="text-center py-3" style="background-color: #e8f0fe !important; color: #1b365d !important;">Acciones</th>
                                        </c:if>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach items="${listaEventos}" var="evento">
                                        <tr>
                                            <!-- Columna: Evento -->
                                            <td class="px-3 py-3">
                                                <div class="fw-bold text-dark" style="font-size: 0.98rem;">${evento.nombre}</div>
                                            </td>

                                            <!-- Columna: Estado -->
                                            <td class="py-3">
                                                <c:choose>
                                                    <c:when test="${evento.eventoFinalizado}">
                                                        <span class="badge bg-secondary px-3 py-2 rounded-pill">Finalizado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Cancelado'}">
                                                        <span class="badge bg-danger px-3 py-2 rounded-pill">Cancelado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Borrador'}">
                                                        <span class="badge bg-warning text-dark px-3 py-2 rounded-pill">Borrador</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge px-3 py-2 rounded-pill text-white" style="background-color: #0d8a5f !important;">Disponible</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>

                                            <!-- Columna: Fecha -->
                                            <td class="py-3 text-nowrap text-secondary fw-medium" style="font-size: 0.95rem;">
                                                    ${evento.fechaHora}
                                            </td>

                                            <!-- Columnas adicionales que solo verán usuarios NO administradores -->
                                            <c:if test="${sessionScope.usuario.idRol != 1}">
                                                <!-- Cupos -->
                                                <td class="py-3 text-muted fw-medium" style="font-size: 0.95rem;">
                                                    <span class="fw-bold text-dark">${evento.capacidadDisponible}</span> / ${evento.capacidadMaxima}
                                                </td>

                                                <!-- Reservas -->
                                                <td class="py-3 text-center">
                                                    <span class="badge ${evento.totalReservas > 0 ? 'bg-primary' : 'bg-light text-dark border'} px-2 py-1 rounded-pill">
                                                        <i class="bi bi-people-fill me-1"></i>${evento.totalReservas}
                                                    </span>
                                                </td>

                                                <!-- Acciones (Menú Desplegable Dropdown) -->
                                                <td class="py-3 text-center">
                                                    <div class="dropdown">
                                                        <button class="btn btn-sm btn-light border shadow-sm rounded-3 dropdown-toggle fw-semibold text-secondary px-3"
                                                                type="button"
                                                                id="dropdownMenuAcciones${evento.id}"
                                                                data-bs-toggle="dropdown"
                                                                aria-expanded="false">
                                                            <i class="bi bi-gear-fill me-1"></i> Acciones
                                                        </button>

                                                        <ul class="dropdown-menu dropdown-menu-end shadow border-0 rounded-3 mt-1" aria-labelledby="dropdownMenuAcciones${evento.id}">
                                                            <li>
                                                                <a class="dropdown-item py-2 d-flex align-items-center text-secondary fw-medium"
                                                                   href="${pageContext.request.contextPath}/evento?action=detalle&id=${evento.id}">
                                                                    <i class="bi bi-eye-fill me-2 text-info fs-6"></i> Ver Detalle
                                                                </a>
                                                            </li>

                                                            <c:if test="${!evento.eventoFinalizado && evento.estado != 'Cancelado'}">
                                                                <li><hr class="dropdown-divider my-1"></li>
                                                                <li>
                                                                    <a class="dropdown-item py-2 d-flex align-items-center text-secondary fw-medium"
                                                                       href="${pageContext.request.contextPath}/evento?action=editar&id=${evento.id}">
                                                                        <i class="bi bi-pencil-square me-2 text-primary fs-6"></i> Editar Evento
                                                                    </a>
                                                                </li>
                                                                <li>
                                                                    <a class="dropdown-item py-2 d-flex align-items-center text-secondary fw-medium"
                                                                       href="${pageContext.request.contextPath}/evento?action=cancelar&id=${evento.id}"
                                                                       onclick="confirmarCancelar(event)">
                                                                        <i class="bi bi-slash-circle-fill me-2 text-warning fs-6"></i> Cancelar Evento
                                                                    </a>
                                                                </li>
                                                            </c:if>

                                                            <c:if test="${sessionScope.usuario.idRol == 1 || evento.eventoFinalizado || evento.estado == 'Cancelado'}">
                                                                <li><hr class="dropdown-divider my-1"></li>
                                                                <li>
                                                                    <a class="dropdown-item py-2 d-flex align-items-center text-danger fw-medium"
                                                                       href="${pageContext.request.contextPath}/evento?action=delete&id=${evento.id}"
                                                                       onclick="confirmarEliminarEvento(event)">
                                                                        <i class="bi bi-trash3-fill me-2 text-danger fs-6"></i> Eliminar Registro
                                                                    </a>
                                                                </li>
                                                            </c:if>
                                                        </ul>
                                                    </div>
                                                </td>
                                            </c:if>

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