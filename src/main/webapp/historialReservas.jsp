<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Reservas - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/misReservas.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center">
            <img src="img/logo.png" alt="Logo SRAE" style="max-height: 70px;" class="me-3">
            <div>
                <h5 class="fw-bold m-0" style="color: #162e54;">SRAE</h5>
                <small class="text-muted fw-semibold">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>
        </div>
        <div class="d-flex align-items-center">
            <a href="evento" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="evento" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>
                    <a href="reserva" class="btn sidebar-btn py-3 px-4 fw-bold active">
                        <i class="bi bi-calendar-check me-3"></i> Mis reservas
                    </a>
                    <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>
                    <a href="evento" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MIS RESERVAS</h4>


                <form action="reserva" method="get" class="row g-3 align-items-end mb-4">
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Filtrar por estado</label>
                        <select name="estado" class="form-select input-filtro">
                            <option value="">Todos los estados</option>
                            <option value="Reservado" ${filtroEstado == 'Reservado' ? 'selected' : ''}>Reservado / Confirmado</option>
                            <option value="Cancelado" ${filtroEstado == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
                            <option value="Utilizado" ${filtroEstado == 'Utilizado' ? 'selected' : ''}>Utilizado</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Fecha del evento</label>
                        <input type="date" name="fecha" value="${filtroFecha}" class="form-control input-filtro">
                    </div>
                    <div class="col-sm-4">
                        <button type="submit" class="btn btn-aplicar-filtros w-100 d-flex align-items-center justify-content-center gap-2 shadow-sm">
                            <i class="bi bi-funnel-fill"></i> Aplicar Filtros
                        </button>
                    </div>
                </form>

                <div class="table-responsive shadow-sm tabla-reservas">
                    <table class="table table-hover m-0 align-middle">
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
                                <c:forEach var="res" items="${misReservas}">
                                    <tr>
                                        <td class="text-muted fw-bold">${res.codigoReserva}</td>
                                        <td class="fw-semibold">${res.nombreEvento}</td>
                                        <td class="text-muted">${res.fechaHoraReserva}</td>
                                        <td class="text-muted">${res.fechaEvento}</td>
                                        <td>${res.nombreEspacio}</td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${res.estado == 'Reservado'}">
                                                    <span class="badge bg-success rounded-pill px-3 py-2 w-100">✔ Confirmado</span>
                                                </c:when>
                                                <c:when test="${res.estado == 'Cancelado'}">
                                                    <span class="badge bg-danger rounded-pill px-3 py-2 w-100">✖ Cancelado</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">⏳ ${res.estado}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex gap-1 justify-content-center">
                                                <button class="btn btn-sm btn-outline-primary" onclick="verDetalle(${res.id})">
                                                    <i class="bi bi-eye"></i>
                                                </button>
                                                <c:if test="${res.estado == 'Reservado'}">
                                                    <form action="reserva" method="post" style="display:inline;">
                                                        <input type="hidden" name="action" value="cancelar">
                                                        <input type="hidden" name="idReserva" value="${res.id}">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Seguro que deseas cancelar esta reserva?');">
                                                            <i class="bi bi-x-circle"></i>
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="text-center py-4 text-muted">No se encontraron reservas con los filtros seleccionados.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="modalDetalleReserva" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold"><i class="bi bi-ticket-perforated me-2"></i> Detalle de Reserva</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="text-center mb-3">
                    <span class="badge bg-primary fs-6 px-3 py-2 rounded-pill" id="mCodigo">—</span>
                </div>
                <div class="mb-3">
                    <h5 class="fw-bold text-dark mb-1" id="mEvento">—</h5>
                    <p class="text-muted small" id="mDescripcion">—</p>
                </div>
                <hr>
                <div class="row g-3 small">
                    <div class="col-6">
                        <strong><i class="bi bi-calendar-event me-1"></i> Fecha del evento:</strong>
                        <div class="text-secondary" id="mFechaEvento">—</div>
                    </div>
                    <div class="col-6">
                        <strong><i class="bi bi-clock-history me-1"></i> Fecha de reserva:</strong>
                        <div class="text-secondary" id="mFechaReserva">—</div>
                    </div>
                    <div class="col-6">
                        <strong><i class="bi bi-geo-alt me-1"></i> Lugar:</strong>
                        <div class="text-secondary" id="mLugar">—</div>
                    </div>
                    <div class="col-6">
                        <strong><i class="bi bi-info-circle me-1"></i> Estado:</strong>
                        <div id="mEstado">—</div>
                    </div>
                </div>
            </div>
            <div class="modal-footer border-0 pt-0">
                <button type="button" class="btn btn-secondary rounded-3" data-bs-dismiss="modal">Cerrar</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function verDetalle(idReserva) {
        fetch('reserva?action=detalle&idReserva=' + idReserva)
            .then(response => response.json())
            .then(data => {
                document.getElementById('mCodigo').innerText = data.codigo;
                document.getElementById('mEvento').innerText = data.evento;
                document.getElementById('mDescripcion').innerText = data.descripcion || 'Sin descripción disponible.';
                document.getElementById('mFechaEvento').innerText = data.fechaEvento;
                document.getElementById('mFechaReserva').innerText = data.fechaReserva;
                document.getElementById('mLugar').innerText = data.lugar + (data.ubicacion ? ' (' + data.ubicacion + ')' : '');

                const mEstado = document.getElementById('mEstado');
                if (data.estado === 'Reservado') {
                    mEstado.innerHTML = '<span class="badge bg-success">Confirmado</span>';
                } else if (data.estado === 'Cancelado') {
                    mEstado.innerHTML = '<span class="badge bg-danger">Cancelado</span>';
                } else {
                    mEstado.innerHTML = '<span class="badge bg-secondary">' + data.estado + '</span>';
                }

                const modal = new bootstrap.Modal(document.getElementById('modalDetalleReserva'));
                modal.show();
            })
            .catch(error => {
                console.error('Error al obtener el detalle:', error);
                alert('No se pudo cargar la información de la reserva.');
            });
    }
</script>
</body>
</html>