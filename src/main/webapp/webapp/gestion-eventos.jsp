<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión - SRAE</title>

    <!-- Hojas de estilo generales (las mismas que usas en Usuarios) -->
    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
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
<body class="bg-light">

<!-- Header Superior (Fuera del container para que ocupe todo el ancho) -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<div class="container my-4">

    <!-- Tarjeta Principal Adaptada al Diseño de Usuarios -->
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <!-- COLUMNA ÚNICA: CONTENIDO DE GESTIÓN -->
            <div class="col-12">

                <div id="seccion-eventos">

                    <!-- Encabezado con línea verde similar a Usuarios -->
                    <div class="d-flex justify-content-between align-items-end pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f;">
                        <h4 class="fw-bold m-0" style="color: #1a1a1a;">
                            ${sessionScope.usuario.idRol == 1 ? 'GESTIÓN GENERAL DE EVENTOS' : 'EVENTOS'}
                        </h4>

                        <div class="d-flex gap-2 mb-1">
                            <a href="${pageContext.request.contextPath}/evento?action=crear" class="btn btn-success btn-sm fw-bold px-3 py-2 rounded-3" style="background-color: #0d8a5f; border: none;">
                                <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                            </a>

                            <c:if test="${sessionScope.usuario.idRol != 1 && not empty listaEventos}">
                                <a href="${pageContext.request.contextPath}/evento?action=limpiarHistorial" class="btn btn-outline-danger btn-sm fw-semibold px-3 py-2 rounded-3" onclick="confirmarLimpiar(event)">
                                    <i class="bi bi-broom me-1"></i> Limpiar
                                </a>
                            </c:if>
                        </div>
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
                            <div class="table-responsive shadow-sm border rounded-3">
                                <table class="table table-hover m-0 bg-white" id="tablaEventos">
                                    <thead>
                                    <tr>
                                        <th scope="col" class="py-3 px-3 text-dark fw-bold border-bottom">Evento</th>
                                        <th scope="col" class="py-3 text-dark fw-bold border-bottom">Estado</th>
                                        <th scope="col" class="py-3 text-dark fw-bold border-bottom">Fecha</th>
                                        <th scope="col" class="py-3 text-dark fw-bold border-bottom">Cupos</th>
                                        <th scope="col" class="text-center py-3 text-dark fw-bold border-bottom">Reservas</th>
                                        <th scope="col" class="text-center py-3 text-dark fw-bold border-bottom">Acciones</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${listaEventos}" var="evento">
                                        <tr class="align-middle">
                                            <td class="px-3">
                                                <div class="fw-semibold text-dark">${evento.nombre}</div>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${evento.eventoFinalizado}">
                                                        <span class="badge bg-secondary px-2 py-1 rounded-pill">Finalizado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Cancelado'}">
                                                        <span class="badge bg-danger px-2 py-1 rounded-pill">Cancelado</span>
                                                    </c:when>
                                                    <c:when test="${evento.estado == 'Borrador'}">
                                                        <span class="badge bg-warning text-dark px-2 py-1 rounded-pill">Borrador</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-success px-2 py-1 rounded-pill" style="background-color: #0d8a5f !important;">Disponible</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-nowrap text-muted fw-medium" style="font-size: 0.95rem;">${evento.fechaHora}</td>
                                            <td class="text-muted fw-medium" style="font-size: 0.95rem;">
                                                <span class="fw-bold text-dark">${evento.capacidadDisponible}</span> / ${evento.capacidadMaxima}
                                            </td>
                                            <td class="text-center">
                                                <span class="badge ${evento.totalReservas > 0 ? 'bg-primary' : 'bg-light text-dark border'} px-2 py-1 rounded-pill">
                                                    <i class="bi bi-people-fill me-1"></i>${evento.totalReservas}
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <div class="d-flex justify-content-center gap-2">
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