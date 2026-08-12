<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Evento - SRAE</title>

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
            <a href="evento" class="btn text-white fw-bold p-2 rounded-3 shadow-sm" style="background-color: #162e54;">
                <i class="bi bi-eye"></i> Mis Eventos
            </a>
            <a href="logout" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">
            ${not empty evento ? 'EDICIÓN DEL EVENTO' : 'CREACIÓN DEL EVENTO'}
        </h4>

        <c:if test="${empty listaEspacios}">
            <div class="alert alert-warning">
                <i class="bi bi-exclamation-triangle-fill"></i>
                No hay espacios registrados todavía. Se necesita al menos un espacio en la base de datos para poder publicar un evento.
            </div>
        </c:if>

        <form action="evento" method="post" enctype="multipart/form-data">

            <c:if test="${not empty evento}">
                <input type="hidden" name="idEvento" value="${evento.id}">
            </c:if>

            <div class="row">
                <div class="col-md-6 d-flex flex-column justify-content-between">

                    <div class="mb-3">
                        <label for="idCategoria" class="form-label fw-bold text-dark m-1">Categoría</label>
                        <select name="idCategoria" class="form-select p-2 rounded-3" id="idCategoria" required>
                            <option value="" disabled ${empty evento ? 'selected' : ''}>Selecciona una categoría</option>
                            <c:forEach items="${listaCategorias}" var="cat">
                                <c:set var="catId" value="${not empty cat.idCategoria ? cat.idCategoria : cat.id}" />
                                <option value="${catId}" ${not empty evento && evento.idCategoria == catId ? 'selected' : ''}>${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold text-dark m-1">Nombre del evento</label>
                        <input type="text" name="nombre" class="form-control p-2 rounded-3" id="nombre" placeholder="Ingresa el nombre del evento" value="${fn:escapeXml(evento.nombre)}" required>
                    </div>

                    <div class="mb-3">
                        <label for="idEspacio" class="form-label fw-bold text-dark m-1">Ubicación / Espacio</label>
                        <select name="idEspacio" class="form-select p-2 rounded-3" id="idEspacio" required>
                            <option value="" disabled ${empty evento ? 'selected' : ''}>Selecciona un espacio</option>
                            <c:forEach items="${listaEspacios}" var="esp">
                                <c:set var="espId" value="${not empty esp.idEspacio ? esp.idEspacio : esp.id}" />
                                <option value="${espId}" ${not empty evento && evento.idEspacio == espId ? 'selected' : ''}>
                                    <c:out value="${not empty esp.nombreEspacio ? esp.nombreEspacio : esp.nombre}"/>
                                    <c:if test="${not empty esp.ubicacion}"> &mdash; ${esp.ubicacion}</c:if>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="fecha" class="form-label fw-bold text-dark m-1">Fecha y hora del evento</label>
                        <input type="datetime-local" name="fecha" class="form-control p-2 rounded-3" id="fecha"
                               value="${not empty evento ? fn:substring(fn:replace(evento.fechaHora, ' ', 'T'), 0, 16) : ''}" required>
                    </div>

                    <div class="mb-3">
                        <label for="capacidad" class="form-label fw-bold text-dark m-1">Capacidad máxima</label>
                        <input type="number" name="capacidad" class="form-control p-2 rounded-3" id="capacidad" placeholder="Ej. 100" value="${evento.capacidadMaxima}" required min="1">
                    </div>

                </div>

                <div class="col-md-6 d-flex flex-column justify-content-start mt-md-0 mt-3">
                    <div class="mb-3">
                        <label for="descripcion" class="form-label fw-bold text-dark m-1">Descripción del evento</label>
                        <textarea name="descripcion" class="form-control p-2 rounded-3" id="descripcion" rows="4" placeholder="Describe los detalles del evento, objetivos, actividades, invitados, etc." required>${fn:escapeXml(evento.descripcion)}</textarea>
                    </div>

                    <label class="form-label fw-bold text-dark m-1">Imagen del evento <i class="bi bi-calendar4-event"></i></label>
                    <div class="border text-center p-4 rounded-3 bg-white d-flex flex-column align-items-center justify-content-center flex-grow-1" style="border-style: dashed !important; min-height: 200px;">
                        <c:if test="${not empty evento && not empty evento.imagenUrl}">
                            <img src="${evento.imagenUrl}" alt="Imagen actual" class="img-fluid rounded-3 mb-3" style="max-height: 160px; object-fit: cover;">
                        </c:if>
                        <label for="img" class="btn text-white fw-bold px-4 py-2 mb-3 d-inline-flex align-items-center shadow-sm" style="background-color: #0d8a5f; border-radius: 10px; cursor: pointer;">
                            <i class="bi bi-upload me-2"></i> ${not empty evento ? 'Cambiar imagen' : 'Seleccionar imagen'}
                        </label>
                        <input type="file" name="img" id="img" accept="image/*" class="d-none">
                        <small class="text-muted">
                            Formatos permitidos: JPG, PNG, Máx 10MB.
                            <c:if test="${not empty evento}"> Si no seleccionas una nueva, se conserva la imagen actual.</c:if>
                        </small>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="submit" name="action" value="borrador" class="btn btn-outline-secondary fw-bold py-2 px-4 rounded-3">
                    <i class="bi bi-save2"></i> ${not empty evento ? 'Guardar cambios como borrador' : 'Guardar borrador'}
                </button>
                <button type="submit" name="action" value="publicar" class="btn text-white fw-bold py-2 px-4 shadow-sm d-inline-flex align-items-center" style="background-color: #0d8a5f; border-radius: 10px;">
                    <i class="bi bi-send-fill me-2"></i> ${not empty evento ? 'Publicar cambios' : 'Publicar evento'}
                </button>
            </div>
        </form>
    </div>
</div>
<script src="${pageContext.request.contextPath}/js/categorias.js?v=2"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>