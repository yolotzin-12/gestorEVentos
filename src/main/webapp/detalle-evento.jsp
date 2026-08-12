<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Detalle del Evento - SRAE</title>

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
                <a href="evento">Eventos</a>
                <!-- Ocultar "Mis reservas" si es Organizador/Admin -->
                <c:if test="${sessionScope.usuario.idRol != 1 && sessionScope.usuario.idRol != 2}">
                    <a href="reserva">Mis reservas</a>
                </c:if>
            </nav>

            <div class="d-flex align-items-center gap-2">
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

    <div class="container">

        <a href="evento" class="btn btn-sm mb-3" style="background-color:#e4e4e6; color:#162e54; font-weight:700; border-radius:10px;">
            <i class="bi bi-arrow-left"></i> Volver a eventos
        </a>

        <c:choose>
            <c:when test="${not empty evento}">
                <div class="card p-4 shadow-sm border-0 rounded-4 bg-white mb-4">
                    <div class="row g-4">
                        <div class="col-md-6">
                            <c:choose>
                                <c:when test="${not empty evento.imagenUrl}">
                                    <img src="${evento.imagenUrl}" alt="${evento.nombre}" class="img-fluid rounded-4" style="width:100%; height:350px; object-fit:cover;">
                                </c:when>
                                <c:otherwise>
                                    <img src="img/personas.jpg" alt="Evento" class="img-fluid rounded-4" style="width:100%; height:350px; object-fit:cover;">
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="col-md-6 d-flex flex-column justify-content-between">
                            <div>
                                <span class="badge mb-2" style="background-color:#0d8a5f;">
                                    <c:out value="${evento.nombreCategoria}" default="General" />
                                </span>

                                <h3 class="fw-bold mb-3" style="color:#162e54;"><c:out value="${evento.nombre}" default="Evento sin nombre" /></h3>

                                <p class="text-muted mb-3" style="white-space: pre-line; line-height: 1.5;">
                                    <c:out value="${evento.descripcion}" default="Sin descripción disponible." />
                                </p>

                                <p class="text-secondary mb-1">
                                    <i class="bi bi-calendar-event me-2" style="color:#0d8a5f;"></i>
                                    <strong>Fecha y Hora:</strong> <c:out value="${evento.fechaHora}" default="Por definir" />
                                </p>

                                <p class="text-secondary mb-1">
                                    <i class="bi bi-geo-alt-fill me-2" style="color:#0d8a5f;"></i>
                                    <strong>Ubicación:</strong>
                                    <c:out value="${evento.ubicacion}" default="Por definir" />
                                </p>

                                <p class="text-secondary mb-3">
                                    <i class="bi bi-people-fill me-2" style="color:#0d8a5f;"></i>
                                    <strong>Aforo máximo:</strong>
                                    <c:out value="${evento.capacidadDisponible}" default="${evento.capacidadMaxima}" /> / <c:out value="${evento.capacidadMaxima}" default="N/A" /> personas
                                </p>
                            </div>

                            <!-- VALIDACIÓN SEGÚN EL ROL DE USUARIO -->
                            <div class="mt-3">
                                <c:choose>
                                    <%-- SI ES ORGANIZADOR O ADMIN: Opciones de gestión --%>
                                    <c:when test="${sessionScope.usuario.idRol == 1 || sessionScope.usuario.idRol == 2}">
                                        <div class="d-flex gap-2">

                                            <a href="evento?action=editar&id=${evento.id}" class="btn btn-outline-success w-50 fw-bold py-2 rounded-3">
                                                <i class="bi bi-pencil-square me-1"></i> Editar
                                            </a>

                                            <form action="evento" method="post" class="w-50" onsubmit="return confirm('¿Estás seguro de eliminar este evento?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${evento.id}">
                                                <button type="submit" class="btn btn-outline-danger w-100 fw-bold py-2 rounded-3">
                                                    <i class="bi bi-trash me-1"></i> Eliminar
                                                </button>
                                            </form>
                                        </div>
                                    </c:when>

                                    <%-- SI ES UN USUARIO NORMAL / ALUMNO: Botón de Reservar --%>
                                    <c:otherwise>
                                        <a href="reservar.jsp?id=${evento.id}" class="text-decoration-none w-100">
                                            <button type="button" class="btn fs-5 w-100 d-flex align-items-center justify-content-center gap-2" style="background-color:#0d8a5f; color:#fff; font-weight:bold; border-radius:10px; padding:10px; border:none;">
                                                <i class="bi bi-calendar-check"></i> Reservar / Registrarse
                                            </button>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                        </div>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <div class="card p-4 shadow-sm border-0 rounded-4 bg-white mb-4">
                    <div class="row g-4">
                        <div class="col-md-6">
                            <img src="img/personas.jpg" alt="Evento" class="img-fluid rounded-4" style="width:100%; height:320px; object-fit:cover;">
                        </div>
                        <div class="col-md-6 d-flex flex-column justify-content-between">
                            <div>
                                <span class="badge mb-2" style="background-color:#0d8a5f;">Aviso</span>
                                <h3 class="fw-bold" style="color:#162e54;">No se encontró la información del evento</h3>
                                <p class="text-secondary mb-3">No fue posible recuperar los detalles para el ID especificado.</p>
                            </div>
                            <a href="evento" class="text-decoration-none w-100">
                                <button type="button" class="btn fs-5 w-100 d-flex align-items-center justify-content-center gap-2" style="background-color:#162e54; color:#fff; font-weight:bold; border-radius:10px; padding:10px; border:none;">
                                    <i class="bi bi-arrow-left"></i> Regresar al catálogo
                                </button>
                            </a>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>