<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link rel="stylesheet" href="css/navbar.css"> <!-- Se agrega el CSS de la navbar -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

<!-- ================= NAVBAR SUPERIOR ================= -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<!-- ================= CONTENIDO PRINCIPAL ================= -->
<main class="flex-grow-1">
    <div class="container my-4">

        <!-- Botón opcional para regresar fácilmente -->
        <div class="mb-3">
            <a href="evento?action=gestion" class="text-decoration-none text-secondary fw-bold">
                <i class="bi bi-arrow-left"></i> Volver a Gestión de Eventos
            </a>
        </div>

        <div class="card p-4 shadow-sm border-0 rounded-4">
            <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">EDICIÓN DEL EVENTO</h4>

            <form action="evento" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id" value="${evento.id}">

                <div class="row">
                    <div class="col-md-6 d-flex flex-column justify-content-between">

                        <div class="mb-3">
                            <label for="idCategoria" class="form-label fw-bold text-dark m-1">Categoría</label>
                            <select name="idCategoria" class="form-select p-2 rounded-3" id="idCategoria" required>
                                <option value="" disabled>Selecciona una categoría</option>
                                <c:forEach items="${listaCategorias}" var="cat">
                                    <option value="${cat.idCategoria}" ${cat.idCategoria == evento.idCategoria ? 'selected' : ''}>
                                            ${cat.nombre}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="nombre" class="form-label fw-bold text-dark m-1">Nombre del evento</label>
                            <input type="text" name="nombre" class="form-control p-2 rounded-3" id="nombre" value="${evento.nombre}" required>
                        </div>

                        <div class="mb-3">
                            <label for="idEspacio" class="form-label fw-bold text-dark m-1">Espacio / Ubicación</label>
                            <select name="idEspacio" id="idEspacio" class="form-select p-2 rounded-3" required>
                                <option value="" disabled>Selecciona un espacio</option>
                                <c:forEach items="${listaEspacios}" var="esp">
                                    <option value="${esp.idEspacio}" ${esp.idEspacio == evento.idEspacio ? 'selected' : ''}>
                                            ${esp.nombreEspacio} - ${esp.ubicacion}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="fecha" class="form-label fw-bold text-dark m-1">Fecha y hora del evento</label>
                            <input type="datetime-local" name="fecha" class="form-control p-2 rounded-3" id="fecha"
                                   value="${not empty evento.fechaHora ? fn:substring(fn:replace(evento.fechaHora, ' ', 'T'), 0, 16) : ''}" required>
                        </div>

                        <div class="mb-3">
                            <label for="capacidad" class="form-label fw-bold text-dark m-1">Capacidad máxima</label>
                            <!-- Precargamos la capacidad -->
                            <input type="number" name="capacidad" class="form-control p-2 rounded-3" id="capacidad" value="${evento.capacidadMaxima}" required min="1">
                        </div>

                        <div class="mb-3">
                            <label for="estado" class="form-label fw-bold text-dark m-1">Estado del Evento</label>
                            <select name="estado" id="estado" class="form-select p-2 rounded-3" required>
                                <option value="Disponible" ${evento.estado == 'Disponible' ? 'selected' : ''}>Publicado (Disponible)</option>
                                <option value="Borrador" ${evento.estado == 'Borrador' ? 'selected' : ''}>Borrador (Oculto)</option>
                            </select>
                        </div>

                    </div>

                    <div class="col-md-6 d-flex flex-column">

                        <label class="form-label fw-bold text-dark m-1">Imagen del evento <i class="bi bi-image"></i></label>
                        <div class="border text-center p-4 rounded-3 bg-white d-flex flex-column align-items-center justify-content-center mb-3" style="border-style: dashed !important; min-height: 250px;">

                            <img id="preview" src="${not empty evento.imagenUrl ? evento.imagenUrl : ''}"
                                 alt="Previsualización"
                                 class="img-fluid rounded-3 mb-3 ${empty evento.imagenUrl ? 'd-none' : ''}"
                                 style="max-height: 200px; object-fit: cover; width: 100%;">

                            <div id="cajaBoton" class="${not empty evento.imagenUrl ? 'd-none' : ''}">
                                <label for="img" class="btn text-white fw-bold px-4 py-2 mb-2 shadow-sm" style="background-color: #0d8a5f; border-radius: 10px; cursor: pointer;">
                                    <i class="bi bi-upload me-2"></i> Seleccionar imagen
                                </label>
                                <br>
                                <small class="text-muted">Formatos permitidos: JPG, PNG, Máx 10MB</small>
                            </div>

                            <!-- El input file no es requerido al editar, por si quieren conservar la imagen actual -->
                            <input type="file" name="img" id="img" accept="image/*" class="d-none" onchange="previsualizarImagen(this)">

                            <label for="img" id="btnCambiar" class="btn btn-sm btn-outline-secondary mt-2 ${empty evento.imagenUrl ? 'd-none' : ''}" style="cursor: pointer;">
                                Cambiar imagen
                            </label>
                        </div>

                        <div class="mb-3 flex-grow-1 d-flex flex-column">
                            <label for="descripcion" class="form-label fw-bold text-dark m-1">Descripción del evento</label>
                            <textarea name="descripcion" id="descripcion" class="form-control p-2 rounded-3 flex-grow-1" required style="min-height: 120px;">${evento.descripcion}</textarea>
                        </div>

                    </div>
                </div>

                <div class="d-flex justify-content-end mt-4">
                    <a href="evento?action=gestion" class="btn fw-bold py-2 px-4 me-2" style="background-color: #6c757d; border-radius: 10px; color: white;">
                        Cancelar
                    </a>
                    <button type="submit" class="btn text-white fw-bold py-2 px-4" style="background-color: #0d8a5f; border-radius: 10px;">
                        <i class="bi bi-arrow-repeat me-2"></i> Actualizar Evento
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="${pageContext.request.contextPath}/js/categorias.js?v=2"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>