<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eventos Registrados - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
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
                <small class="text-muted fw-semibold">EVENTOS DISPONIBLES</small>
            </div>
        </div>
        <div class="d-flex align-items-center gap-3">
            <a href="evento?action=nuevo" class="btn text-white fw-bold p-2 rounded-3 shadow-sm" style="background-color: #0d8a5f;">
                <i class="bi bi-plus-circle"></i> Nuevo Evento
            </a>
            <a href="evento" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="row g-4">
        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="col-12">
                    <div class="alert alert-info text-center mt-4 rounded-4 shadow-sm" role="alert">
                        <i class="bi bi-info-circle-fill fs-4 d-block mb-2"></i> No hay eventos registrados en este momento.
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <c:forEach items="${listaEventos}" var="evento">
                    <div class="col-md-6 col-lg-4">
                        <div class="card shadow-sm border-0 rounded-4 h-100 bg-white">
                            <span class="badge position-absolute top-0 start-0 m-3 p-2 shadow-sm" style="background-color: #162e54; z-index: 1;">
                                    ${evento.nombreCategoria}                            </span>

                            <img src="${not empty evento.imagenUrl ? evento.imagenUrl : 'img/personas.jpg'}" alt="Imagen del evento" class="card-img-top rounded-top-4" style="height: 200px; object-fit: cover;">
                            <div class="card-body d-flex flex-column">
                                <h5 class="fw-bold text-dark mb-1">${evento.nombre}</h5>

                                <div class="mt-3 text-secondary text-sm">
                                    <p class="mb-2"><i class="bi bi-calendar-event me-2" style="color: #0d8a5f;"></i> <strong>Fecha:</strong> ${evento.fechaHora}</p>
                                    <p class="mb-2"><i class="bi bi-geo-alt-fill me-2" style="color: #0d8a5f;"></i> <strong>Lugar:</strong> ${evento.ubicacion}</p>
                                    <p class="mb-2"><i class="bi bi-people-fill me-2" style="color: #0d8a5f;"></i> <strong>Aforo Max:</strong> ${evento.capacidadMaxima} personas</p>
                                </div>
                                <div class="mt-auto pt-3 border-top">
                                            <button class="btn w-100 fw-bold text-white shadow-sm mb-2" style="background-color: #058971; border-radius: 10px;">
                                                <i class="bi bi-pencil"></i> Editar Evento
                                            </button>
                                            <button class="btn btn-secondary w-100 fw-bold shadow-sm mb-2"  style= "background-color: #C5001A; border-radius: 10px;">
                                                <i class="bi bi-backspace"></i> Eliminar evento
                                            </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>