<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Evento - SRAE</title>

    <!-- Hojas de estilo generales -->
    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/perfil.css">

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<!-- Navbar Principal -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<div class="container my-4">

    <!-- Tarjeta Principal del Formulario -->
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">CREACIÓN DEL EVENTO</h4>

        <form action="evento" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="publicar">

            <div class="row g-4">

                <!-- COLUMNA IZQUIERDA: CAMPOS DE TEXTO Y SELECCIÓN -->
                <div class="col-lg-6 d-flex flex-column justify-content-between">

                    <!-- Categoría (Solo selección para Organizador) -->
                    <div class="mb-3">
                        <label for="idCategoria" class="form-label fw-bold text-dark mb-1">Categoría</label>
                        <select name="idCategoria" class="form-select p-2 rounded-3" id="idCategoria" required>
                            <option value="" disabled selected>Selecciona una categoría</option>
                            <c:forEach items="${listaCategorias}" var="cat">
                                <option value="${cat.idCategoria}">${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Nombre del Evento -->
                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold text-dark mb-1">Nombre del evento</label>
                        <input type="text" name="nombre" class="form-control p-2 rounded-3" id="nombre" placeholder="Ingresa el nombre del evento" required>
                    </div>

                    <!-- Espacio / Ubicación (Solo selección para Organizador) -->
                    <div class="mb-3">
                        <label for="idEspacio" class="form-label fw-bold text-dark mb-1">Espacio / Ubicación</label>
                        <select name="idEspacio" id="idEspacio" class="form-select p-2 rounded-3" required>
                            <option value="" disabled selected>Selecciona un espacio</option>
                            <c:forEach items="${listaEspacios}" var="esp">
                                <option value="${esp.idEspacio}">${esp.nombreEspacio} - ${esp.ubicacion}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Fecha y hora -->
                    <div class="mb-3">
                        <label for="fecha" class="form-label fw-bold text-dark mb-1">Fecha y hora del evento</label>
                        <input type="datetime-local" name="fecha" class="form-control p-2 rounded-3" id="fecha"
                               value="${not empty evento ? fn:substring(fn:replace(evento.fechaHora, ' ', 'T'), 0, 16) : ''}" required>
                    </div>

                    <!-- Capacidad Máxima -->
                    <div class="mb-3">
                        <label for="capacidad" class="form-label fw-bold text-dark mb-1">Capacidad máxima</label>
                        <input type="number" name="capacidad" class="form-control p-2 rounded-3" id="capacidad" placeholder="Ej. 100" required min="1">
                    </div>

                </div>

                <!-- COLUMNA DERECHA: IMAGEN Y DESCRIPCIÓN -->
                <div class="col-lg-6 d-flex flex-column">

                    <!-- Carga de Imagen -->
                    <label class="form-label fw-bold text-dark mb-1">Imagen del evento <i class="bi bi-image"></i></label>
                    <div class="border text-center p-4 rounded-3 bg-white d-flex flex-column align-items-center justify-content-center mb-3" style="border-style: dashed !important; border-color: #dee2e6 !important; min-height: 250px;">

                        <img id="preview" src="" alt="Previsualización" class="img-fluid rounded-3 mb-3 d-none" style="max-height: 200px; object-fit: cover; width: 100%;">

                        <div id="cajaBoton">
                            <label for="img" class="btn text-white fw-bold px-4 py-2 mb-2 shadow-sm" style="background-color: #0d8a5f; border-radius: 10px; cursor: pointer;">
                                <i class="bi bi-upload me-2"></i> Seleccionar imagen
                            </label>
                            <br>
                            <small class="text-muted">Formatos permitidos: JPG, PNG, Máx 10MB</small>
                        </div>

                        <input type="file" name="img" id="img" accept="image/*" class="d-none" onchange="previsualizarImagen(this)">

                        <label for="img" id="btnCambiar" class="btn btn-sm btn-outline-secondary mt-2 d-none" style="cursor: pointer;">
                            Cambiar imagen
                        </label>
                    </div>

                    <!-- Descripción -->
                    <div class="mb-3 flex-grow-1 d-flex flex-column">
                        <label for="descripcion" class="form-label fw-bold text-dark mb-1">Descripción del evento</label>
                        <textarea name="descripcion" id="descripcion" class="form-control p-2 rounded-3 flex-grow-1" placeholder="Describe el evento..." required style="min-height: 120px; resize: none;"></textarea>
                    </div>

                </div>
            </div>

            <!-- BOTONES DE ACCIÓN FORMULARIO -->
            <div class="d-flex justify-content-end gap-2 mt-4">
                <button type="submit" name="action" value="publicar" class="btn text-white fw-bold py-2 px-4 shadow-sm"
                        style="background-color: #0d8a5f; border-radius: 10px;">
                    <i class="bi bi-send-fill me-2"></i> Publicar evento
                </button>
                <button type="submit" name="action" value="borrador" class="btn btn-secondary fw-bold py-2 px-4 shadow-sm"
                        style="border-radius: 10px;">
                    <i class="bi bi-floppy me-2"></i> Guardar borrador
                </button>
            </div>
        </form>
    </div>
</div>

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/categorias.js?v=2"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

<!-- Script para la previsualización de la imagen -->
<script>
    function previsualizarImagen(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                var preview = document.getElementById('preview');
                var cajaBoton = document.getElementById('cajaBoton');
                var btnCambiar = document.getElementById('btnCambiar');

                preview.src = e.target.result;
                preview.classList.remove('d-none');
                cajaBoton.classList.add('d-none');
                btnCambiar.classList.remove('d-none');
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>

</body>
</html>