<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- Respaldo de sesión: Si no existe en request, toma el usuario de la sesión --%>
<c:set var="u" value="${not empty requestScope.usuario ? requestScope.usuario : sessionScope.usuario}" />

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - SRAE</title>

    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/administrarUsuarios.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container my-4">

    <!-- Header Superior -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center gap-3">
            <img src="img/logo.png" alt="Logo SRAE" style="height: 50px;">
            <div>
                <h5 class="fw-bold m-0" style="color: #162e54;">SRAE</h5>
                <small class="text-muted fw-semibold" style="font-size: 0.75rem;">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm rounded-3 px-3 py-2" title="Cerrar sesión" onclick="confirmarCierreSesion(event)">
                <i class="bi bi-box-arrow-right fs-6"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <!-- MENÚ LATERAL (SIDEBAR CORREGIDO) -->
            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <!-- 1. Catálogo Público -->
                    <a href="${pageContext.request.contextPath}/evento" class="btn sidebar-btn py-3 px-4 fw-bold text-start">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>

                    <!-- 2. Gestión de Eventos (Pasa por el Servlet) -->
                    <a href="${pageContext.request.contextPath}/evento?action=gestion" class="btn sidebar-btn py-3 px-4 fw-bold text-start">
                        <i class="bi bi-calendar-event me-3"></i> Eventos
                    </a>

                    <!-- 3. Administrador (idRol == 1) -->
                    <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 1}">
                        <a href="${pageContext.request.contextPath}/usuarios" class="btn sidebar-btn py-3 px-4 fw-bold text-start">
                            <i class="bi bi-people me-3"></i> Usuarios
                        </a>
                    </c:if>

                    <!-- 4. Cliente / Usuario Normal (idRol == 3) -->
                    <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 3}">
                        <a href="${pageContext.request.contextPath}/reserva" class="btn sidebar-btn py-3 px-4 fw-bold text-start">
                            <i class="bi bi-calendar-check me-3"></i> Mis reservas
                        </a>
                    </c:if>

                    <!-- 5. Mi Perfil (Carga desde Servlet para refrescar datos) -->
                    <a href="${pageContext.request.contextPath}/usuarios?action=perfil" class="btn sidebar-btn py-3 px-4 fw-bold text-start active">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>

                    <!-- 6. Salir -->
                    <a href="${pageContext.request.contextPath}/logout" class="btn text-white py-3 px-4 fw-bold text-start" style="background-color: #cc0000;" onclick="confirmarCierreSesion(event)">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <!-- CONTENIDO PRINCIPAL -->
            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MI PERFIL</h4>

                <div class="row g-4">
                    <!-- DATOS PERSONALES -->
                    <div class="col-md-7">
                        <div class="p-3 bg-light rounded-4 border">
                            <h5 class="fw-bold mb-3" style="color: #162e54;">Datos Personales</h5>

                            <input type="hidden" id="updateSuccess" value="${param.update}">

                            <form action="${pageContext.request.contextPath}/usuarios?action=actualizarDatos" method="post" enctype="multipart/form-data" id="formActualizarPerfil">
                                <input type="hidden" name="action" value="actualizarDatos">

                                <div class="text-center mb-3">
                                    <div class="position-relative d-inline-block">
                                        <c:choose>
                                            <c:when test="${not empty u.fotoUrl}">
                                                <img src="${u.fotoUrl}" id="previewFoto" class="rounded-circle object-fit-cover shadow" style="width: 100px; height: 100px; border: 3px solid #162e54;">
                                            </c:when>
                                            <c:otherwise>
                                                <div id="defaultIcon" class="bg-primary text-white d-inline-flex flex-column align-items-center justify-content-center rounded-circle shadow" style="width: 100px; height: 100px;">
                                                    <i class="bi bi-person-fill fs-1"></i>
                                                </div>
                                                <img src="" id="previewFoto" class="rounded-circle object-fit-cover shadow d-none" style="width: 100px; height: 100px; border: 3px solid #162e54;">
                                            </c:otherwise>
                                        </c:choose>

                                        <label for="fotoPerfil" class="btn btn-sm btn-success rounded-circle position-absolute bottom-0 end-0" style="background-color: #0d8a5f; cursor: pointer;">
                                            <i class="bi bi-camera-fill"></i>
                                        </label>
                                        <input type="file" id="fotoPerfil" name="fotoPerfil" class="d-none" accept="image/*" onchange="previewImage(event)">
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nombre" class="form-label fw-bold">Nombre:</label>
                                        <input type="text" name="nombre" class="form-control" id="nombre" value="${u.nombre}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apeP" class="form-label fw-bold">Apellido Paterno:</label>
                                        <input type="text" name="apeP" class="form-control" id="apeP" value="${u.apellidoPaterno}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apeM" class="form-label fw-bold">Apellido Materno:</label>
                                        <input type="text" name="apeM" class="form-control" id="apeM" value="${u.apellidoMaterno}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="telefono" class="form-label fw-bold">Teléfono:</label>
                                        <input type="tel" name="telefono" class="form-control" id="telefono" value="${u.telefono}" placeholder="10 dígitos (Ej: 7771234567)" maxlength="10" minlength="10" pattern="[0-9]{10}">
                                        <div id="errorTelefono" class="text-danger mt-1" style="display: none; font-size: 0.85em;">
                                            El teléfono debe tener exactamente 10 dígitos.
                                        </div>
                                    </div>
                                    <div class="col-md-12 mb-3">
                                        <label for="correo" class="form-label fw-bold">Correo Electrónico:</label>
                                        <div class="input-group">
                                            <input type="email" name="correo" class="form-control" style="background-color: #e9ecef; color: #6c757d; cursor: not-allowed;" id="correo" value="${u.email}" readonly title="El correo electrónico no puede ser modificado" required>
                                            <span class="input-group-text" style="background-color: #e9ecef;"><i class="bi bi-lock-fill text-muted"></i></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center mt-2">
                                    <button type="submit" class="btn btn-success fw-bold px-4" style="background-color: #0d8a5f;">
                                        <i class="bi bi-floppy me-2"></i> Guardar Cambios
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!-- SEGURIDAD (CAMBIAR CONTRASEÑA) -->
                    <div class="col-md-5">
                        <div class="p-3 bg-light rounded-4 border">
                            <h5 class="fw-bold mb-1" style="color: #162e54;">Seguridad</h5>
                            <small class="text-muted d-block mb-3">Cambiar contraseña</small>

                            <input type="hidden" id="serverErrorPerfil" value="${not empty error ? error : (param.error == 'pass_invalid' ? 'La contraseña actual es incorrecta.' : '')}">
                            <input type="hidden" id="serverSuccessPerfil" value="${param.success == 'pass_updated' ? 'true' : 'false'}">

                            <div id="alertasContainerPerfil" class="mb-3"></div>

                            <form action="${pageContext.request.contextPath}/usuarios" method="post" id="formCambiarContra">
                                <input type="hidden" name="action" value="cambiarPassword">

                                <div class="mb-3">
                                    <label for="contraActual" class="form-label fw-bold">Contraseña actual:</label>
                                    <div class="input-group">
                                        <input type="password" name="contraActual" class="form-control" id="contraActual" placeholder="Contraseña actual" required>
                                        <button class="btn btn-outline-secondary toggle-password" type="button" data-target="contraActual" style="border-color: #dee2e6;">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="contraNew" class="form-label fw-bold">Contraseña nueva:</label>
                                    <div class="input-group">
                                        <input type="password" name="contraNew" class="form-control" id="contraNew" placeholder="Contraseña Nueva" required>
                                        <button class="btn btn-outline-secondary toggle-password" type="button" data-target="contraNew" style="border-color: #dee2e6;">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                    </div>
                                    <div id="errorLongitud" class="text-danger mt-1" style="display: none; font-size: 0.85em;">
                                        La contraseña debe tener al menos 8 caracteres.
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="confirmarContra" class="form-label fw-bold">Confirmar contraseña:</label>
                                    <div class="input-group">
                                        <input type="password" name="confirmarContra" class="form-control" id="confirmarContra" placeholder="Confirmar contraseña" required>
                                        <button class="btn btn-outline-secondary toggle-password" type="button" data-target="confirmarContra" style="border-color: #dee2e6;">
                                            <i class="bi bi-eye"></i>
                                        </button>
                                    </div>
                                    <div id="errorCoincidencia" class="text-danger mt-1" style="display: none; font-size: 0.85em;">
                                        Las contraseñas no coinciden.
                                    </div>
                                </div>

                                <div class="text-center mt-4">
                                    <button type="submit" class="btn btn-success fw-bold px-4" style="background-color: #0d8a5f;">
                                        <i class="bi bi-arrow-clockwise me-2"></i> Actualizar
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/perfil.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>