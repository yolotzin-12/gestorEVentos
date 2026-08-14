<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="u" value="${not empty requestScope.usuario ? requestScope.usuario : sessionScope.usuario}" />

<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - SRAE</title>

    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        /* Estilos de la Navbar */
        .navbar-custom {
            background-color: #0A1429;
            padding: 0.8rem 2rem;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
        }
        .navbar-brand {
            color: #ffffff !important;
            font-weight: 800;
            font-size: 1.5rem;
            margin-right: 2rem;
            letter-spacing: 0.5px;
        }
        .navbar-custom .nav-link {
            color: #ffffff;
            font-size: 0.95rem;
            margin: 0 10px;
            padding: 8px 0;
            transition: color 0.2s ease;
        }
        .navbar-custom .nav-link:hover {
            color: #0d8a5f;
        }
        .navbar-custom .nav-link.active {
            font-weight: 600;
            border-bottom: 2px solid #ffffff;
        }
        .nav-icon-btn {
            background-color: #233659;
            color: #ffffff;
            border: none;
            border-radius: 6px;
            width: 40px;
            height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-left: 10px;
            text-decoration: none;
            transition: background-color 0.2s ease;
        }
        .nav-icon-btn:hover {
            background-color: #314b7a;
            color: #ffffff;
        }
        .nav-icon-btn.active-icon {
            background-color: #0d8a5f;
        }
        .nav-icon-btn i { font-size: 1.2rem; }

        /* Estilos Perfil */
        .cumplido { color: #0d8a5f; }
        .no-cumplido { color: #dc3545; }
        .panel-perfil {
            background-color: #E9EEED !important;
            border-radius: 20px !important;
            border: none;
        }
        .input-group-text.candado-perfil {
            border-top-right-radius: 50px !important;
            border-bottom-right-radius: 50px !important;
            border: 1px solid #d1d5db !important;
            border-left: none !important;
            background-color: #dcdcdc;
        }
    </style>
</head>
<body class="bg-light">

<!-- Navbar Superior -->
<nav class="navbar navbar-expand-lg navbar-custom mb-4">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/evento">srae</a>
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <i class="bi bi-list text-white fs-2"></i>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/evento">Inicio</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/evento?action=gestion">Eventos</a>
                </li>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/usuarios">Usuarios</a>
                    </li>
                </c:if>
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 3}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/reserva">Mis reservas</a>
                    </li>
                </c:if>
            </ul>
            <div class="d-flex align-items-center mt-3 mt-lg-0">
                <a href="${pageContext.request.contextPath}/usuarios?action=perfil" class="nav-icon-btn active-icon" title="Mi perfil">
                    <i class="bi bi-person-fill"></i>
                </a>
                <a href="${pageContext.request.contextPath}/logout" class="nav-icon-btn" title="Cerrar sesión" onclick="confirmarCierreSesion(event)">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

<!-- Contenedor Principal (Sin Sidebar) -->
<div class="container my-4">
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MI PERFIL</h4>

        <div class="row g-4">
            <!-- Columna 1: Datos Personales (Expandida) -->
            <div class="col-md-7">
                <div class="p-4 panel-perfil shadow-sm h-100">
                    <h5 class="fw-bold mb-3" style="color: #162e54;">Datos Personales</h5>

                    <input type="hidden" id="updateSuccess" value="${param.update}">

                    <form action="${pageContext.request.contextPath}/usuarios?action=actualizarDatos" method="post" enctype="multipart/form-data" id="formActualizarPerfil">
                        <input type="hidden" name="action" value="actualizarDatos">

                        <div class="text-center mb-4">
                            <div class="position-relative d-inline-block">
                                <c:choose>
                                    <c:when test="${not empty u.fotoUrl}">
                                        <img src="${u.fotoUrl}" id="previewFoto" class="rounded-circle object-fit-cover shadow-sm" style="width: 95px; height: 95px; border: 3px solid #162e54;">
                                    </c:when>
                                    <c:otherwise>
                                        <div id="defaultIcon" class="bg-primary text-white d-inline-flex flex-column align-items-center justify-content-center rounded-circle shadow-sm" style="width: 95px; height: 95px; border: 3px solid #162e54;">
                                            <i class="bi bi-person-fill fs-1"></i>
                                        </div>
                                        <img src="" id="previewFoto" class="rounded-circle object-fit-cover d-none" style="width: 95px; height: 95px; border: 3px solid #162e54;">
                                    </c:otherwise>
                                </c:choose>

                                <label for="fotoPerfil" class="btn btn-sm text-white rounded-circle position-absolute d-flex align-items-center justify-content-center shadow" style="background-color: #058971; width: 32px; height: 32px; bottom: 0; right: -5px; cursor: pointer; padding: 0; border: 2px solid white;">
                                    <i class="bi bi-camera-fill" style="font-size: 15px;"></i>
                                </label>
                                <input type="file" id="fotoPerfil" name="fotoPerfil" class="d-none" accept="image/*" onchange="previewImage(event)">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3 text-start">
                                <label for="nombre" class="form-label fw-bold label-formulario mb-1">Nombre:</label>
                                <input type="text" name="nombre" class="form-control input-formulario py-2 px-3" id="nombre" value="${u.nombre}" required>
                            </div>
                            <div class="col-md-6 mb-3 text-start">
                                <label for="apeP" class="form-label fw-bold label-formulario mb-1">Apellido Paterno:</label>
                                <input type="text" name="apeP" class="form-control input-formulario py-2 px-3" id="apeP" value="${u.apellidoPaterno}" required>
                            </div>
                            <div class="col-md-6 mb-3 text-start">
                                <label for="apeM" class="form-label fw-bold label-formulario mb-1">Apellido Materno:</label>
                                <input type="text" name="apeM" class="form-control input-formulario py-2 px-3" id="apeM" value="${u.apellidoMaterno}" required>
                            </div>
                            <div class="col-md-6 mb-3 text-start">
                                <label for="telefono" class="form-label fw-bold label-formulario mb-1">Teléfono:</label>
                                <input type="tel" name="telefono" class="form-control input-formulario py-2 px-3" id="telefono" value="${u.telefono}" placeholder="10 dígitos" maxlength="10" minlength="10" pattern="[0-9]{10}">
                            </div>
                            <div class="col-md-12 mb-4 text-start">
                                <label for="correo" class="form-label fw-bold label-formulario mb-1">Correo Electrónico:</label>
                                <div class="input-group">
                                    <input type="email" name="correo" class="form-control input-formulario py-2 px-3" style="background-color: #dcdcdc; color: #6c757d; cursor: not-allowed; border-right: none;" id="correo" value="${u.email}" readonly title="El correo electrónico no puede ser modificado" required>
                                    <span class="input-group-text candado-perfil"><i class="bi bi-lock-fill text-muted"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="text-center mt-2">
                            <button type="submit" class="btn btn-ingresar text-white fw-bold py-2 px-4 d-inline-flex align-items-center justify-content-center">
                                <i class="bi bi-floppy me-2"></i> Guardar Cambios
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Columna 2: Seguridad (Expandida) -->
            <div class="col-md-5">
                <div class="p-4 panel-perfil shadow-sm h-100">
                    <h5 class="fw-bold mb-1" style="color: #162e54;">Seguridad</h5>
                    <small class="text-muted d-block mb-4 fw-semibold" style="font-size: 0.85rem;">Cambiar contraseña</small>

                    <input type="hidden" id="serverErrorPerfil" value="${not empty error ? error : (param.error == 'pass_invalid' ? 'La contraseña actual es incorrecta.' : '')}">
                    <input type="hidden" id="serverSuccessPerfil" value="${param.success == 'pass_updated' ? 'true' : 'false'}">
                    <div id="alertasContainerPerfil" class="mb-3"></div>

                    <form action="${pageContext.request.contextPath}/usuarios" method="post" id="formCambiarContra">
                        <input type="hidden" name="action" value="cambiarPassword">

                        <div class="mb-3 text-start">
                            <label for="contraActual" class="form-label fw-bold label-formulario mb-1">Contraseña actual:</label>
                            <div class="input-group">
                                <input type="password" name="contraActual" class="form-control input-formulario py-2 px-3" id="contraActual" placeholder="Contraseña actual" required>
                                <button class="btn btn-outline-secondary btn-ver-password" type="button" data-target="contraActual">
                                    <i class="bi bi-eye-fill"></i>
                                </button>
                            </div>
                        </div>

                        <div class="mb-3 text-start">
                            <label for="contraNew" class="form-label fw-bold label-formulario mb-1">Contraseña nueva:</label>
                            <div class="input-group">
                                <input type="password" name="contraNew" class="form-control input-formulario py-2 px-3" id="contraNew" placeholder="Contraseña Nueva" required>
                                <button class="btn btn-outline-secondary btn-ver-password" type="button" data-target="contraNew">
                                    <i class="bi bi-eye-fill"></i>
                                </button>
                            </div>
                            <ul id="listaRequisitos" class="list-unstyled mt-2" style="display: none; font-size: 0.8em;">
                                <li id="req-longitud" class="no-cumplido"><i class="bi bi-x-circle-fill me-1"></i> Mínimo 8 caracteres</li>
                                <li id="req-mayuscula" class="no-cumplido"><i class="bi bi-x-circle-fill me-1"></i> Una letra mayúscula</li>
                                <li id="req-minuscula" class="no-cumplido"><i class="bi bi-x-circle-fill me-1"></i> Una letra minúscula</li>
                                <li id="req-numero" class="no-cumplido"><i class="bi bi-x-circle-fill me-1"></i> Un número</li>
                            </ul>
                        </div>

                        <div class="mb-4 text-start">
                            <label for="confirmarContra" class="form-label fw-bold label-formulario mb-1">Confirmar contraseña:</label>
                            <div class="input-group">
                                <input type="password" name="confirmarContra" class="form-control input-formulario py-2 px-3" id="confirmarContra" placeholder="Confirmar contraseña" required>
                                <button class="btn btn-outline-secondary btn-ver-password" type="button" data-target="confirmarContra">
                                    <i class="bi bi-eye-fill"></i>
                                </button>
                            </div>
                        </div>

                        <div class="text-center mt-4">
                            <button type="submit" class="btn btn-ingresar text-white fw-bold py-2 px-4 d-inline-flex align-items-center justify-content-center">
                                <i class="bi bi-arrow-clockwise me-2"></i> Actualizar
                            </button>
                        </div>
                    </form>
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