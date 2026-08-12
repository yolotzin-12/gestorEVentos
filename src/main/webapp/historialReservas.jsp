<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="0">
    <title>Mis Reservas - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/misReservas.css?v=2">
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
                    <a href="logout" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MIS RESERVAS</h4>

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
                                                    <form action="reserva" method="post" onsubmit="return confirm('¿Seguro que deseas cancelar esta reserva?');">
                                                        <input type="hidden" name="action" value="cancelar">
                                                        <input type="hidden" name="idReserva" value="${reserva.id}">
                                                        <input type="hidden" name="filtroEstado" value="${filtroEstado}">
                                                        <input type="hidden" name="filtroFecha" value="${filtroFecha}">
                                                        <button type="submit" class="btn btn-cancelar">
                                                            <i class="bi bi-x-circle me-1"></i> Cancelar
                                                        </button>
                                                    </form>
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
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>