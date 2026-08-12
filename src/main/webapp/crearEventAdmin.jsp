<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
                <i class="bi bi-eye"></i> Ver Eventos
            </a>
            <a href="index.jsp" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">CREACIÓN DEL EVENTO</h4>

        <form action="evento" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="publicar">

            <div class="row">
                <div class="col-md-6 d-flex flex-column justify-content-between">

                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center m-1">
                            <label for="idCategoria" class="form-label fw-bold text-dark mb-0">Categoría</label>
                            <div class="btn-group">
                                <button type="button" class="btn btn-sm btn-outline-primary fw-bold" data-bs-toggle="modal" data-bs-target="#modalCategoria">
                                    <i class="bi bi-plus-lg"></i> Nueva
                                </button>
                                <button type="button" class="btn btn-sm btn-outline-danger fw-bold" data-bs-toggle="modal" data-bs-target="#modalEliminarCategoria">
                                    <i class="bi bi-trash"></i> Eliminar
                                </button>
                            </div>
                        </div>
                        <select name="idCategoria" class="form-select p-2 rounded-3" id="idCategoria" required>
                            <option value="" disabled selected>Selecciona una categoría</option>
                            <c:forEach items="${listaCategorias}" var="cat">
                                <option value="${cat.idCategoria}">${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold text-dark m-1">Nombre del evento</label>
                        <input type="text" name="nombre" class="form-control p-2 rounded-3" id="nombre" placeholder="Ingresa el nombre del evento" required>
                    </div>

                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center m-1">
                            <label for="idEspacio" class="form-label fw-bold text-dark mb-0">Espacio / Ubicación</label>
                            <div class="btn-group">
                                <button type="button" class="btn btn-sm btn-outline-primary fw-bold" data-bs-toggle="modal" data-bs-target="#modalEspacio">
                                    <i class="bi bi-plus-lg"></i> Nuevo
                                </button>
                                <button type="button" class="btn btn-sm btn-outline-danger fw-bold" data-bs-toggle="modal" data-bs-target="#modalEliminarEspacio">
                                    <i class="bi bi-trash"></i> Eliminar
                                </button>
                            </div>
                        </div>
                        <select name="idEspacio" id="idEspacio" class="form-select p-2 rounded-3" required>
                            <option value="" disabled selected>Selecciona un espacio</option>
                            <c:forEach items="${listaEspacios}" var="esp">
                                <option value="${esp.idEspacio}">${esp.nombreEspacio} - ${esp.ubicacion}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="organizador" class="form-label fw-bold text-dark m-1">Asignar a Organizador</label>
                        <select name="idOrganizador" class="form-select p-2 rounded-3" id="organizador" required>
                            <option value="" disabled selected>Seleccione un organizador</option>
                            <c:forEach items="${listaOrganizadores}" var="org">
                                <option value="${org.id}">${org.nombre} ${org.apellidoPaterno} (${org.organizacion})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="fecha" class="form-label fw-bold text-dark m-1">Fecha del evento</label>
                        <input type="date" name="fecha" class="form-control p-2 rounded-3" id="fecha" required>
                    </div>

                    <div class="mb-3">
                        <label for="capacidad" class="form-label fw-bold text-dark m-1">Capacidad máxima</label>
                        <input type="number" name="capacidad" class="form-control p-2 rounded-3" id="capacidad" placeholder="Ej. 100" required min="1">
                    </div>

                </div>

                <div class="col-md-6 d-flex flex-column">

                    <label class="form-label fw-bold text-dark m-1">Imagen del evento <i class="bi bi-image"></i></label>
                    <div class="border text-center p-4 rounded-3 bg-white d-flex flex-column align-items-center justify-content-center mb-3" style="border-style: dashed !important; min-height: 250px;">

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

                    <div class="mb-3 flex-grow-1 d-flex flex-column">
                        <label for="descripcion" class="form-label fw-bold text-dark m-1">Descripción del evento</label>
                        <textarea name="descripcion" id="descripcion" class="form-control p-2 rounded-3 flex-grow-1" placeholder="Describe el evento..." required style="min-height: 120px;"></textarea>
                    </div>

                </div>
            </div>

            <div class="d-flex justify-content-end mt-4">
                <button type="submit" name="action" value="publicar" class="btn text-white fw-bold py-2 px-4"
                        style="background-color: #0d8a5f; border-radius: 10px;">
                    <i class="bi bi-send-fill me-2"></i> Publicar evento
                </button>
                <button type="submit" name="action" value="borrador" class="btn fw-bold py-2 px-4 ms-2"
                        style="background-color: #6c757d; border-radius: 10px; color: white;">
                    <i class="bi bi-floppy me-2"></i> Guardar borrador
                </button>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="modalCategoria" tabindex="-1" aria-labelledby="modalCategoriaLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalCategoriaLabel">Agregar Nueva Categoría</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <label for="nombreCategoria" class="form-label fw-bold">Nombre de la Categoría</label>
                <input type="text" id="nombreCategoria" class="form-control p-2" placeholder="Ej. Taller de robótica">
                <div id="mensajeCategoria" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="guardarCategoria()">Guardar Categoría</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalEliminarCategoria" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header text-white bg-danger">
                <h5 class="modal-title fw-bold">Eliminar Categoría</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <p>Selecciona la categoría a eliminar. No se pueden eliminar si ya tienen eventos asignados.</p>
                <select id="selectEliminarCat" class="form-select p-2">
                    <option value="" disabled selected>Seleccione...</option>
                    <c:forEach items="${listaCategorias}" var="cat">
                        <option value="${cat.idCategoria}">${cat.nombre}</option>
                    </c:forEach>
                </select>
                <div id="mensajeEliminarCat" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" onclick="borrarCategoria()">Eliminar</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalEspacio" tabindex="-1" aria-labelledby="modalEspacioLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalEspacioLabel">Agregar Nuevo Espacio</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label for="nombreEspacio" class="form-label fw-bold">Nombre del Espacio</label>
                    <input type="text" id="nombreEspacio" class="form-control p-2" placeholder="Ej. Auditorio Principal">
                </div>
                <div class="mb-3">
                    <label for="ubicacionEspacio" class="form-label fw-bold">Ubicación (Opcional)</label>
                    <input type="text" id="ubicacionEspacio" class="form-control p-2" placeholder="Ej. Edificio B">
                </div>
                <div id="mensajeEspacio" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="guardarEspacio()">Guardar Espacio</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalEliminarEspacio" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header text-white bg-danger">
                <h5 class="modal-title fw-bold">Eliminar Espacio</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <p>Selecciona el espacio a eliminar. No se puede eliminar si ya tiene eventos programados.</p>
                <select id="selectEliminarEsp" class="form-select p-2">
                    <option value="" disabled selected>Seleccione...</option>
                    <c:forEach items="${listaEspacios}" var="esp">
                        <option value="${esp.idEspacio}">${esp.nombreEspacio} - ${esp.ubicacion}</option>
                    </c:forEach>
                </select>
                <div id="mensajeEliminarEsp" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-danger" onclick="borrarEspacio()">Eliminar</button>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/categorias.js?v=2"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>