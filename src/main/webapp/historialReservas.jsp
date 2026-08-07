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
            <a href="${pageContext.request.contextPath}/logout" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <!-- Alertas de estado enviadas por ReservaServlet -->
    <c:if test="${not empty mensaje}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${mensaje}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="index.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>
                    <a href="${pageContext.request.contextPath}/reserva" class="btn sidebar-btn py-3 px-4 fw-bold active">
                        <i class="bi bi-calendar-check me-3"></i> Mis reservas
                    </a>
                    <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MIS RESERVAS</h4>

                <!-- Formulario con filtros GET hacia /reserva -->
                <form action="${pageContext.request.contextPath}/reserva" method="get" class="row g-3 align-items-end mb-4">
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Filtrar por: estado</label>
                        <select name="estado" class="form-select input-filtro">
                            <option value="">Todos los estados</option>
                            <option value="Reservado" ${param.estado == 'Reservado' ? 'selected' : ''}>Reservado</option>
                            <option value="Cancelado" ${param.estado == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Fecha de reserva</label>
                        <input type="date" name="fecha" value="${param.fecha}" class="form-control input-filtro">
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
                            <th scope="col">Código</th>
                            <th scope="col">ID Evento</th>
                            <th scope="col">Fecha Reserva</th>
                            <th scope="col" class="text-center">Estado</th>
                            <th scope="col" class="text-center">Acciones</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty misReservas}">
                                <c:forEach var="reserva" items="${misReservas}">
                                    <tr>
                                        <td class="fw-semibold text-primary">${reserva.codigoReserva}</td>
                                        <td class="fw-semibold">Evento #${reserva.idEvento}</td>
                                        <td class="text-muted">${reserva.fechaHoraReserva}</td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${reserva.estado eq 'Reservado'}">
                                                    <span class="badge bg-success rounded-pill px-3 py-2 w-100">✔ Reservado</span>
                                                </c:when>
                                                <c:when test="${reserva.estado eq 'Cancelado'}">
                                                    <span class="badge bg-danger rounded-pill px-3 py-2 w-100">✖ Cancelado</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">${reserva.estado}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex justify-content-center gap-2">
                                                <!-- Formulario para cancelar utilizando el método POST del ReservaServlet -->
                                                <c:if test="${reserva.estado eq 'Reservado'}">
                                                    <form action="${pageContext.request.contextPath}/reserva" method="post" onsubmit="return confirm('¿Seguro que deseas cancelar esta reserva?');">
                                                        <input type="hidden" name="action" value="cancelar">
                                                        <input type="hidden" name="idReserva" value="${reserva.id}">
                                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                                            <i class="bi bi-x-circle me-1"></i> Cancelar
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
                                    <td colspan="5" class="text-center py-4 text-muted">No se encontraron reservas registradas.</td>
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