<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis eventos - SRAE</title>

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
                <a href="${pageContext.request.contextPath}/evento">Eventos</a>
                <a href="${pageContext.request.contextPath}/evento?action=misEventos" class="activo">Mis eventos</a>
            </nav>

            <div class="d-flex align-items-center gap-2">
                <a href="evento?action=crear" class="btn btn-success btn-sm fw-bold me-2" style="background-color: #0d8a5f; border: none;">
                    <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                </a>
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

    <div class="container py-4">

        <h4 class="fw-bold mb-4">Mis eventos</h4>

        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="col-12 text-center py-5">
                    <i class="bi bi-calendar-x text-muted fs-1"></i>
                    <p class="mt-2 text-muted fw-bold">Aún no has creado ningún evento.</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="table-responsive">
                    <table class="table align-middle bg-white rounded-4 overflow-hidden shadow-sm">
                        <thead class="table-light">
                        <tr>
                            <th>Evento</th>
                            <th>Estado</th>
                            <th>Fecha</th>
                            <th>Ubicación</th>
                            <th>Lugares disponibles</th>
                            <th>Reservas</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listaEventos}" var="evento">
                            <tr>
                                <td class="fw-bold">${evento.nombre}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${evento.estado == 'Disponible'}">
                                            <span class="badge rounded-pill text-bg-success">Disponible</span>
                                        </c:when>
                                        <c:when test="${evento.estado == 'Borrador'}">
                                            <span class="badge rounded-pill text-bg-secondary">Borrador</span>
                                        </c:when>
                                        <c:when test="${evento.estado == 'Cancelado'}">
                                            <span class="badge rounded-pill text-bg-danger">Cancelado</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge rounded-pill text-bg-light text-dark">${evento.estado}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${evento.fechaHora}</td>
                                <td><c:out value="${evento.ubicacion}" default="Sin ubicación"/></td>
                                <td>${evento.capacidadDisponible} / ${evento.capacidadMaxima}</td>
                                <td>
                                        <span class="badge rounded-pill text-bg-info">
                                            <i class="bi bi-people-fill"></i> ${evento.totalReservas}
                                        </span>
                                </td>
                                <td>
                                    <a href="evento?action=detalle&id=${evento.id}" class="btn btn-sm btn-outline-secondary">
                                        Ver
                                    </a>
                                    <a href="evento?action=editar&id=${evento.id}" class="btn btn-sm btn-outline-primary">
                                        Editar
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>