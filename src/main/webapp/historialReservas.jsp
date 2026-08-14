<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
<%--suppress XmlUnresolvedReference --%>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
<%--suppress XmlUnresolvedReference --%>
    <meta http-equiv="Pragma" content="no-cache">
<%--suppress XmlUnresolvedReference --%>
    <meta http-equiv="Expires" content="0">
    <title>Mis Reservas - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/misReservas.css?v=3">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<!-- Navbar Superior -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="reservas" />
</jsp:include>

<!-- Contenedor Principal (Ajustado a ancho completo) -->
<div class="container my-4">
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">

        <!-- Encabezado del módulo -->
        <div class="d-flex justify-content-between align-items-center pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f;">
            <h4 class="fw-bold m-0" style="color: #1a1a1a;">MIS RESERVAS</h4>
            <button type="button" class="btn btn-limpiar-historial" data-bs-toggle="modal" data-bs-target="#modalLimpiar">
                <i class="bi bi-trash3 me-1"></i> Limpiar historial
            </button>
        </div>

        <!-- Filtros -->
        <form action="reserva" method="get" class="row g-3 align-items-end mb-4" id="formFiltros">
            <div class="col-sm-4">
                <label class="form-label fw-bold text-dark small mb-1">Filtrar por: estado</label>
                <select name="estado" class="form-select input-filtro" onchange="this.form.submit()">
                    <option value="" ${empty filtroEstado ? 'selected' : ''}>Todos</option>
                    <option value="Reservado" ${filtroEstado == 'Reservado' ? 'selected' : ''}>Reservado</option>
                    <option value="Cancelado" ${filtroEstado == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
                    <option value="Utilizado" ${filtroEstado == 'Utilizado' ? 'selected' : ''}>Finalizado</option>
                </select>
            </div>
            <div class="col-sm-4">
                <label class="form-label fw-bold text-dark small mb-1">Fecha del evento</label>
                <input type="date" name="fecha" class="form-control input-filtro" value="${filtroFecha}">
            </div>
            <div class="col-sm-4">
                <button type="submit" class="btn btn-aplicar-filtros w-100 d-flex align-items-center justify-content-center gap-2 shadow-sm">
                    <i class="bi bi-funnel-fill"></i> Aplicar Filtros
                </button>
            </div>
        </form>

        <!-- Tabla de Reservas -->
        <div class="table-responsive shadow-sm tabla-reservas">
            <table class="table table-hover m-0">
                <thead>
                <tr>
                    <th scope="col">ID Reserva</th>
                    <th scope="col">Evento</th>
                    <th scope="col">Fecha reserva</th>
                    <th scope="col">Fecha Evento</th>
                    <th scope="col">Lugar</th>
                    <th scope="col" class="text-center">Estado</th>
                    <th scope="col" class="text-center">Acciones</th>
                </tr>
                </thead>
                <tbody>

                <c:choose>
                    <c:when test="${not empty misReservas}">
                        <c:forEach var="reserva" items="${misReservas}">
                            <tr>
                                <td class="text-muted">${reserva.codigoReserva}</td>
                                <td class="fw-semibold">${reserva.nombreEvento}</td>
                                <td class="text-muted">${reserva.fechaHoraReserva}</td>
                                <td class="text-muted">${reserva.fechaEvento}</td>
                                <td>${reserva.nombreEspacio}</td>

                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${reserva.estado == 'Reservado' && reserva.eventoFinalizado}">
                                            <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">Finalizado</span>
                                        </c:when>
                                        <c:when test="${reserva.estado == 'Reservado'}">
                                            <span class="badge bg-success rounded-pill px-3 py-2 w-100">✔ Reservado</span>
                                        </c:when>
                                        <c:when test="${reserva.estado == 'Cancelado'}">
                                            <span class="badge bg-danger rounded-pill px-3 py-2 w-100">✖ Cancelado</span>
                                        </c:when>
                                        <c:when test="${reserva.estado == 'Utilizado'}">
                                            <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">✔ Finalizado</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">⏳ ${reserva.estado}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${reserva.estado == 'Reservado' && reserva.eventoFinalizado}">
                                            <button type="button" class="btn btn-consultar" disabled>
                                                <i class="bi bi-check2-circle me-1"></i> Finalizado
                                            </button>
                                        </c:when>
                                        <c:when test="${reserva.estado == 'Reservado'}">
                                            <button type="button" class="btn btn-cancelar"
                                                    data-bs-toggle="modal" data-bs-target="#modalCancelar"
                                                    onclick="document.getElementById('idReservaCancelar').value='${reserva.id}';">
                                                <i class="bi bi-x-circle me-1"></i> Cancelar
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="text-center text-muted py-4">
                                Aún no tienes reservas registradas.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>

                </tbody>
            </table>
        </div>

    </div>
</div>

<!-- Modal de confirmación para cancelar una reserva -->
<div class="modal fade" id="modalCancelar" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4">
            <div class="modal-header border-0">
                <h5 class="modal-title fw-bold" style="color:#162e54;">
                    <i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>Cancelar reserva
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <p class="mb-0">¿Estás seguro o segura de que deseas cancelar esta reserva? Tu lugar quedará liberado y recibirás un correo confirmando la cancelación.</p>
            </div>
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-secondary rounded-3" data-bs-dismiss="modal">No, volver</button>
                <form action="reserva" method="post">
                    <input type="hidden" name="action" value="cancelar">
                    <input type="hidden" name="idReserva" id="idReservaCancelar" value="">
                    <input type="hidden" name="filtroEstado" value="${filtroEstado}">
                    <input type="hidden" name="filtroFecha" value="${filtroFecha}">
                    <button type="submit" class="btn btn-cancelar rounded-3">
                        <i class="bi bi-check-lg me-1"></i> Sí, cancelar
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Modal de confirmación para limpiar el historial -->
<div class="modal fade" id="modalLimpiar" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4">
            <div class="modal-header border-0">
                <h5 class="modal-title fw-bold" style="color:#162e54;">
                    <i class="bi bi-trash3-fill text-danger me-2"></i>Limpiar historial
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                <p class="mb-0">Esto eliminará permanentemente de tu historial las reservas <strong>canceladas</strong> y <strong>finalizadas</strong>. Tus reservas activas no se verán afectadas. ¿Deseas continuar?</p>
            </div>
            <div class="modal-footer border-0">
                <button type="button" class="btn btn-secondary rounded-3" data-bs-dismiss="modal">No, volver</button>
                <form action="reserva" method="post">
                    <input type="hidden" name="action" value="limpiarHistorial">
                    <input type="hidden" name="filtroEstado" value="${filtroEstado}">
                    <input type="hidden" name="filtroFecha" value="${filtroFecha}">
                    <button type="submit" class="btn btn-cancelar rounded-3">
                        <i class="bi bi-check-lg me-1"></i> Sí, limpiar
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

<jsp:include page="logoutModal.jsp" />

</body>
</html>