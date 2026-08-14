<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Categoría y Ubicación - SRAE</title>

    <!-- Hojas de estilo generales -->
    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/perfil.css">
    <link rel="stylesheet" href="css/navbar.css">

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

<!-- ================= NAVBAR SUPERIOR ================= -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<!-- ================= CONTENIDO PRINCIPAL ================= -->
<main class="flex-grow-1">
    <div class="container my-4">

        <!-- Enlace para regresar -->
        <div class="mb-3">
            <a href="evento?action=gestion" class="text-decoration-none text-secondary fw-bold">
                <i class="bi bi-arrow-left"></i> Volver a Gestión de Eventos
            </a>
        </div>

        <!-- Tarjeta Principal -->
        <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
            <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">GESTIÓN DE CATEGORÍAS Y UBICACIONES</h4>

            <div class="row g-4">

                <!-- SECCIÓN: CATEGORÍA -->
                <div class="col-md-6">
                    <div class="p-3 border rounded-3 bg-light h-100 d-flex flex-column justify-content-between">
                        <div>
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <label for="idCategoria" class="form-label fw-bold text-dark mb-0 fs-5">Categoría</label>
                                <div class="btn-group">
                                    <button type="button" class="btn btn-sm btn-outline-primary fw-bold" data-bs-toggle="modal" data-bs-target="#modalCategoria">
                                        <i class="bi bi-plus-lg"></i> Nueva
                                    </button>
                                    <button type="button" class="btn btn-sm btn-outline-secondary fw-bold" data-bs-toggle="modal" data-bs-target="#modalEditarCategoria">
                                        <i class="bi bi-pencil-square"></i> Editar
                                    </button>
                                    <button type="button" class="btn btn-sm btn-outline-danger fw-bold" data-bs-toggle="modal" data-bs-target="#modalEliminarCategoria">
                                        <i class="bi bi-trash"></i> Eliminar
                                    </button>
                                </div>
                            </div>
                            <p class="text-muted small mb-3">Selecciona o administra las categorías disponibles para los eventos.</p>
                        </div>
                        <select name="idCategoria" class="form-select p-2 rounded-3" id="idCategoria">
                            <option value="" disabled selected>Selecciona una categoría</option>
                            <c:forEach items="${listaCategorias}" var="cat">
                                <option value="${cat.idCategoria}">${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <!-- SECCIÓN: ESPACIO / UBICACIÓN -->
                <div class="col-md-6">
                    <div class="p-3 border rounded-3 bg-light h-100 d-flex flex-column justify-content-between">
                        <div>
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <label for="idEspacio" class="form-label fw-bold text-dark mb-0 fs-5">Espacio / Ubicación</label>
                                <div class="btn-group">
                                    <button type="button" class="btn btn-sm btn-outline-primary fw-bold" data-bs-toggle="modal" data-bs-target="#modalEspacio">
                                        <i class="bi bi-plus-lg"></i> Nuevo
                                    </button>
                                    <button type="button" class="btn btn-sm btn-outline-secondary fw-bold" data-bs-toggle="modal" data-bs-target="#modalEditarEspacio">
                                        <i class="bi bi-pencil-square"></i> Editar
                                    </button>
                                    <button type="button" class="btn btn-sm btn-outline-danger fw-bold" data-bs-toggle="modal" data-bs-target="#modalEliminarEspacio">
                                        <i class="bi bi-trash"></i> Eliminar
                                    </button>
                                </div>
                            </div>
                            <p class="text-muted small mb-3">Selecciona o administra las ubicaciones y espacios disponibles.</p>
                        </div>
                        <select name="idEspacio" id="idEspacio" class="form-select p-2 rounded-3">
                            <option value="" disabled selected>Selecciona un espacio</option>
                            <c:forEach items="${listaEspacios}" var="esp">
                                <option value="${esp.idEspacio}">${esp.nombreEspacio} - ${esp.ubicacion}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

            </div>
        </div>
    </div>
</main>

<!-- ================= MODALES CATEGORÍA ================= -->

<!-- Modal Agregar Categoría -->
<div class="modal fade" id="modalCategoria" tabindex="-1" aria-labelledby="modalCategoriaLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalCategoriaLabel">Agregar Nueva Categoría</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <label for="nombreCategoria" class="form-label fw-bold">Nombre de la Categoría</label>
                <input type="text" id="nombreCategoria" class="form-control p-2 rounded-3" placeholder="Ej. Taller de robótica">
                <div id="mensajeCategoria" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="guardarCategoria()">Guardar Categoría</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Editar Categoría -->
<div class="modal fade" id="modalEditarCategoria" tabindex="-1" aria-labelledby="modalEditarCategoriaLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalEditarCategoriaLabel">Editar Categoría</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label for="selectEditarCat" class="form-label fw-bold">Selecciona la Categoría</label>
                    <select id="selectEditarCat" class="form-select p-2 rounded-3">
                        <option value="" disabled selected>Seleccione...</option>
                        <c:forEach items="${listaCategorias}" var="cat">
                            <option value="${cat.idCategoria}">${cat.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="nombreEditarCategoria" class="form-label fw-bold">Nuevo Nombre</label>
                    <input type="text" id="nombreEditarCategoria" class="form-control p-2 rounded-3" placeholder="Ej. Taller de robótica">
                </div>
                <div id="mensajeEditarCat" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="editarCategoria()">Guardar Cambios</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Eliminar Categoría -->
<div class="modal fade" id="modalEliminarCategoria" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white bg-danger">
                <h5 class="modal-title fw-bold">Eliminar Categoría</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <p>Selecciona la categoría a eliminar. No se pueden eliminar si ya tienen eventos asignados.</p>
                <select id="selectEliminarCat" class="form-select p-2 rounded-3">
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

<!-- ================= MODALES ESPACIO / UBICACIÓN ================= -->

<!-- Modal Agregar Espacio -->
<div class="modal fade" id="modalEspacio" tabindex="-1" aria-labelledby="modalEspacioLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalEspacioLabel">Agregar Nuevo Espacio</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label for="nombreEspacio" class="form-label fw-bold">Nombre del Espacio</label>
                    <input type="text" id="nombreEspacio" class="form-control p-2 rounded-3" placeholder="Ej. Auditorio Principal">
                </div>
                <div class="mb-3">
                    <label for="ubicacionEspacio" class="form-label fw-bold">Ubicación (Opcional)</label>
                    <input type="text" id="ubicacionEspacio" class="form-control p-2 rounded-3" placeholder="Ej. Edificio B">
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

<!-- Modal Editar Espacio -->
<div class="modal fade" id="modalEditarEspacio" tabindex="-1" aria-labelledby="modalEditarEspacioLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white" style="background-color: #162e54;">
                <h5 class="modal-title fw-bold" id="modalEditarEspacioLabel">Editar Espacio</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label for="selectEditarEsp" class="form-label fw-bold">Selecciona el Espacio</label>
                    <select id="selectEditarEsp" class="form-select p-2 rounded-3">
                        <option value="" disabled selected>Seleccione...</option>
                        <c:forEach items="${listaEspacios}" var="esp">
                            <option value="${esp.idEspacio}">${esp.nombreEspacio} - ${esp.ubicacion}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="nombreEditarEspacio" class="form-label fw-bold">Nombre del Espacio</label>
                    <input type="text" id="nombreEditarEspacio" class="form-control p-2 rounded-3" placeholder="Ej. Auditorio Principal">
                </div>
                <div class="mb-3">
                    <label for="ubicacionEditarEspacio" class="form-label fw-bold">Ubicación (Opcional)</label>
                    <input type="text" id="ubicacionEditarEspacio" class="form-control p-2 rounded-3" placeholder="Ej. Edificio B">
                </div>
                <div id="mensajeEditarEsp" class="mt-2"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary" onclick="editarEspacio()">Guardar Cambios</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal Eliminar Espacio -->
<div class="modal fade" id="modalEliminarEspacio" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow rounded-4">
            <div class="modal-header text-white bg-danger">
                <h5 class="modal-title fw-bold">Eliminar Espacio</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body p-4">
                <p>Selecciona el espacio a eliminar. No se puede eliminar si ya tiene eventos programados.</p>
                <select id="selectEliminarEsp" class="form-select p-2 rounded-3">
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

<!-- Scripts -->
<script src="${pageContext.request.contextPath}/js/categorias.js?v=2"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>