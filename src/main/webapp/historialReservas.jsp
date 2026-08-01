<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
        <div class="d-flex align-items-center gap-3">
            <c:if test="${not empty sessionScope.usuario}">
                <div class="d-flex align-items-center gap-2">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario.foto}">
                            <img src="${sessionScope.usuario.foto}" alt="Perfil" class="rounded-circle border border-2 border-primary" style="width: 38px; height: 38px; object-fit: cover;">
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-person-circle fs-3 text-secondary"></i>
                        </c:otherwise>
                    </c:choose>
                    <span class="fw-bold text-dark fs-6">${sessionScope.usuario.nombre}</span>
                </div>
            </c:if>
            <a href="${pageContext.request.contextPath}/logout" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;" title="Cerrar sesión">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="paginaPrincipal.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>
                    <a href="misReservas.jsp" class="btn sidebar-btn py-3 px-4 fw-bold active">
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

                <form action="misReservas.jsp" method="get" class="row g-3 align-items-end mb-4">
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Filtrar por: estado</label>
                        <select name="estado" class="form-select input-filtro">
                            <option value="">Todos</option>
                            <option value="confirmado" ${param.estado == 'confirmado' ? 'selected' : ''}>Confirmado</option>
                            <option value="pendiente" ${param.estado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                            <option value="cancelado" ${param.estado == 'cancelado' ? 'selected' : ''}>Cancelado</option>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label class="form-label fw-bold text-dark small mb-1">Fecha del evento</label>
                        <input type="date" name="fecha" value="${param.fecha}" class="form-control input-filtro">
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
                            <c:when test="${empty listaReservas}">
                                <tr>
                                    <td class="text-muted">0001</td>
                                    <td class="fw-semibold">INNOVACIÓN TECNOLÓGICA</td>
                                    <td class="text-muted">10/12/26</td>
                                    <td class="text-muted">15/12/26</td>
                                    <td>Auditorio Principal</td>
                                    <td class="text-center"><span class="badge bg-danger rounded-pill px-3 py-2 w-100">✔ Cancelado</span></td>
                                    <td class="text-center"><a href="detalleReserva.jsp?id=1" class="btn btn-consultar"><i class="bi bi-eye me-1"></i> Consultar Reserva</a></td>
                                </tr>
                                <tr>
                                    <td class="text-muted">0002</td>
                                    <td class="fw-semibold">INNOVACIÓN TECNOLÓGICA</td>
                                    <td class="text-muted">11/12/26</td>
                                    <td class="text-muted">15/12/26</td>
                                    <td>Auditorio Principal</td>
                                    <td class="text-center"><span class="badge bg-success rounded-pill px-3 py-2 w-100">✔ Confirmado</span></td>
                                    <td class="text-center"><a href="detalleReserva.jsp?id=2" class="btn btn-consultar"><i class="bi bi-eye me-1"></i> Consultar Reserva</a></td>
                                </tr>
                                <tr>
                                    <td class="text-muted">0003</td>
                                    <td class="fw-semibold">INNOVACIÓN TECNOLÓGICA</td>
                                    <td class="text-muted">12/12/26</td>
                                    <td class="text-muted">15/12/26</td>
                                    <td>Auditorio Principal</td>
                                    <td class="text-center"><span class="badge bg-secondary rounded-pill px-3 py-2 w-100">⏳ Pendiente</span></td>
                                    <td class="text-center"><a href="detalleReserva.jsp?id=3" class="btn btn-consultar"><i class="bi bi-eye me-1"></i> Consultar Reserva</a></td>
                                </tr>
                            </c:when>

                            <c:otherwise>
                                <c:forEach items="${listaReservas}" var="reserva">
                                    <tr>
                                        <td class="text-muted">${reserva.id}</td>
                                        <td class="fw-semibold">${reserva.eventoNombre}</td>
                                        <td class="text-muted">${reserva.fechaReserva}</td>
                                        <td class="text-muted">${reserva.fechaEvento}</td>
                                        <td>${reserva.lugar}</td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${reserva.estado == 'Confirmado'}">
                                                    <span class="badge bg-success rounded-pill px-3 py-2 w-100">✔ Confirmado</span>
                                                </c:when>
                                                <c:when test="${reserva.estado == 'Cancelado'}">
                                                    <span class="badge bg-danger rounded-pill px-3 py-2 w-100">✖ Cancelado</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary rounded-pill px-3 py-2 w-100">⏳ ${reserva.estado}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <a href="detalleReserva.jsp?id=${reserva.id}" class="btn btn-consultar">
                                                <i class="bi bi-eye me-1"></i> Consultar Reserva
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
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