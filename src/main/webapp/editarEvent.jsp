<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Evento - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/perfil.css">
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
            <a href="${pageContext.request.contextPath}/evento" class="btn text-white fw-bold p-2 rounded-3 shadow-sm" style="background-color: #162e54;">
                <i class="bi bi-eye"></i> Ver Eventos
            </a>
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

    <div class="card p-4 shadow-sm border-0 rounded-4">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">EDICIÓN DEL EVENTO</h4>

        <c:if test="${not empty error}">
            <div class="alert alert-danger d-flex align-items-center py-2 mb-4" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div>${error}</div>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/guardarEvento" method="post">
            <input type="hidden" name="accion" value="actualizar">
            <input type="hidden" name="id" value="${evento.id}">

            <div class="row">
                <div class="col-md-6 d-flex flex-column justify-content-between">

                    <div class="mb-3">
                        <label for="categoria" class="form-label fw-bold text-dark m-1">Categoría</label>
                        <select name="categoria" class="form-select p-2 rounded-3" id="categoria" required>
                            <option value="" disabled>Selecciona una categoría</option>
                            <option value="Academicos" ${evento.categoria == 'Academicos' ? 'selected' : ''}>Académicos</option>
                            <option value="Deportivos" ${evento.categoria == 'Deportivos' ? 'selected' : ''}>Deportivos</option>
                            <option value="Culturales" ${evento.categoria == 'Culturales' ? 'selected' : ''}>Culturales</option>
                            <option value="Conferencias" ${evento.categoria == 'Conferencias' ? 'selected' : ''}>Conferencias</option>
                            <option value="Otros" ${evento.categoria == 'Otros' ? 'selected' : ''}>Otros</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold text-dark m-1">Nombre del evento</label>
                        <input type="text" name="nombre" value="${evento.nombre}" class="form-control p-2 rounded-3" id="nombre" placeholder="Ingresa el nombre del evento" required>
                    </div>

                    <div class="mb-3">
                        <label for="fechaHora" class="form-label fw-bold text-dark m-1">Fecha del evento</label>
                        <input type="date" name="fechaHora" value="${evento.fechaHora}" class="form-control p-2 rounded-3" id="fechaHora" required>
                    </div>

                    <div class="mb-3">
                        <label for="capacidadMaxima" class="form-label fw-bold text-dark m-1">Capacidad máxima</label>
                        <input type="number" name="capacidadMaxima" value="${evento.capacidadMaxima}" class="form-control p-2 rounded-3" id="capacidadMaxima" placeholder="Ej. 100" required min="1">
                    </div>

                    <div class="mb-3">
                        <label for="estado" class="form-label fw-bold text-dark m-1">Estado</label>
                        <select name="estado" class="form-select p-2 rounded-3" id="estado" required>
                            <option value="Disponible" ${evento.estado == 'Disponible' ? 'selected' : ''}>Disponible</option>
                            <option value="Borrador" ${evento.estado == 'Borrador' ? 'selected' : ''}>Borrador</option>
                            <option value="Cancelado" ${evento.estado == 'Cancelado' ? 'selected' : ''}>Cancelado</option>
                        </select>
                    </div>

                </div>

                <div class="col-md-6 d-flex flex-column justify-content-start mt-md-0 mt-3">
                    <div class="mb-3">
                        <label for="descripcion" class="form-label fw-bold text-dark m-1">Descripción del evento</label>
                        <textarea name="descripcion" class="form-control p-2 rounded-3" id="descripcion" rows="9" placeholder="Describe los detalles del evento..." required>${evento.descripcion}</textarea>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end">
                <button type="submit" class="btn text-white fw-bold py-2 px-4 shadow-sm d-inline-flex align-items-center" style="background-color: #0d8a5f; border-radius: 10px;">
                    <i class="bi bi-send-fill me-2"></i> Guardar cambios
                </button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>