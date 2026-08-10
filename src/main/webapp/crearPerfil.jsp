<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <style>
        .profile-box {
            width: 100px;
            height: 90px;
            cursor: pointer;
            overflow: hidden;
            position: relative;
        }
        .profile-box img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    </style>
</head>
<body class="bg-light">

<div class="container my-4">

    <!-- ENCABEZADO UNIFICADO -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center">
            <img src="img/logo.png" alt="Logo SRAE" style="max-height: 70px;" class="me-3">
            <div>
                <h5 class="fw-bold m-0" style="color: #162e54;">SRAE</h5>
                <small class="text-muted fw-semibold">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>
        </div>
        <div class="d-flex align-items-center">
            <a href="logout" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;" title="Cerrar Sesión">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <!-- TARJETA CONTENEDORA PRINCIPAL -->
    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <!-- MENÚ LATERAL (DINÁMICO SEGÚN ROL) -->
            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="evento" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>

                    <%-- VISTA PARA ORGANIZADOR (idRol == 2) --%>
                    <c:if test="${sessionScope.idRol == 2 || sessionScope.usuario.idRol == 2}">
                        <a href="gestion-eventos.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                            <i class="bi bi-calendar-event me-3"></i> Mis eventos
                        </a>
                    </c:if>

                    <%-- VISTA PARA ADMINISTRADOR (idRol == 1) --%>
                    <c:if test="${sessionScope.idRol == 1 || sessionScope.usuario.idRol == 1}">
                        <a href="historialReservas.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                            <i class="bi bi-calendar-check me-3"></i> Reservas
                        </a>
                        <a href="${pageContext.request.contextPath}/usuarios" class="btn sidebar-btn py-3 px-4 fw-bold">
                            <i class="bi bi-people me-3"></i> Usuarios
                        </a>
                    </c:if>

                    <%-- VISTA PARA ASISTENTE (idRol == 3) --%>
                    <c:if test="${sessionScope.idRol == 3 || sessionScope.usuario.idRol == 3}">
                        <a href="historialReservas.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                            <i class="bi bi-calendar-check me-3"></i> Reservas
                        </a>
                    </c:if>

                    <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold active">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>
                    <a href="logout" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <!-- CONTENIDO DE MI PERFIL -->
            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">MI PERFIL</h4>

                <!-- MENSAJES DE ALERTA -->
                <c:if test="${param.msg == 'profile_updated'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        Perfil actualizado con éxito.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.msg == 'pass_updated'}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        Contraseña cambiada correctamente.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.error == 'pass_mismatch'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        Las nuevas contraseñas no coinciden.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.error == 'pass_invalid'}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        La contraseña actual ingresada es incorrecta.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <div class="row g-4">
                    <!-- DATOS PERSONALES -->
                    <div class="col-md-7">
                        <div class="p-3 bg-light rounded-4 border">
                            <h5 class="fw-bold mb-3" style="color: #162e54;">Datos Personales</h5>

                            <form action="usuarios" method="post" enctype="multipart/form-data">
                                <input type="hidden" name="action" value="actualizarPerfil">

                                <!-- Botón e Imagen de Selección de Foto -->
                                <div class="text-center mb-3">
                                    <div class="bg-primary text-white d-inline-flex flex-column align-items-center justify-content-center rounded-3 p-3 profile-box shadow-sm" onclick="document.getElementById('inputFoto').click();">
                                        <c:choose>
                                            <c:when test="${not empty sessionScope.usuario.fotoUrl}">
                                                <img id="preview" src="${sessionScope.usuario.fotoUrl}" alt="Foto de perfil">
                                            </c:when>
                                            <c:otherwise>
                                                <img id="preview" src="" style="display:none;">
                                                <i class="bi bi-person-fill fs-2" id="iconDefault"></i>
                                                <small id="textDefault" style="font-size: 0.65rem;">Cambiar imagen</small>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <!-- Input Oculto -->
                                    <input type="file" id="inputFoto" name="fotoPerfil" accept="image/*" class="d-none" onchange="previewImage(event)">
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nombre" class="form-label fw-bold">Nombre:</label>
                                        <input type="text" name="nombre" class="form-control" id="nombre" value="${sessionScope.usuario.nombre}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apeP" class="form-label fw-bold">Apellido Paterno:</label>
                                        <input type="text" name="apeP" class="form-control" id="apeP" value="${sessionScope.usuario.apellidoPaterno}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apeM" class="form-label fw-bold">Apellido Materno:</label>
                                        <input type="text" name="apeM" class="form-control" id="apeM" value="${sessionScope.usuario.apellidoMaterno}">
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="telefono" class="form-label fw-bold">Teléfono:</label>
                                        <input type="tel" name="telefono" class="form-control" id="telefono" value="${sessionScope.usuario.telefono}" placeholder="777-0000-000">
                                    </div>
                                    <div class="col-md-12 mb-3">
                                        <label for="correo" class="form-label fw-bold">Correo Electrónico:</label>
                                        <div class="input-group">
                                            <input type="email" name="correo" class="form-control" id="correo" value="${sessionScope.usuario.email}" required>
                                            <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                                        </div>
                                    </div>
                                </div>
                                <div class="text-center mt-2">
                                    <button type="submit" class="btn btn-success fw-bold px-4" style="background-color: #0d8a5f;">
                                        <i class="bi bi-floppy me-2"></i> Guardar
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

                            <form action="usuarios" method="post">
                                <input type="hidden" name="action" value="cambiarPassword">

                                <div class="mb-3">
                                    <label for="contraActual" class="form-label fw-bold">Contraseña actual:</label>
                                    <input type="password" name="contraActual" class="form-control" id="contraActual" placeholder="Contraseña actual" required>
                                </div>
                                <div class="mb-3">
                                    <label for="contraNew" class="form-label fw-bold">Contraseña nueva:</label>
                                    <input type="password" name="contraNew" class="form-control" id="contraNew" placeholder="Contraseña Nueva" required>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmarContra" class="form-label fw-bold">Confirmar contraseña:</label>
                                    <input type="password" name="confirmarContra" class="form-control" id="confirmarContra" placeholder="Confirmar contraseña" required>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function previewImage(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.getElementById('preview');
                const icon = document.getElementById('iconDefault');
                const text = document.getElementById('textDefault');

                img.src = e.target.result;
                img.style.display = 'block';

                if (icon) icon.style.display = 'none';
                if (text) text.style.display = 'none';
            }
            reader.readAsDataURL(file);
        }
    }
</script>
</body>
</html>