<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Perfil - SRAE</title>

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

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="paginaPrincipal.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>
                    <a href="historialReservas.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-calendar-check me-3"></i> Reservas
                    </a>
                    <a href="administrarUsu.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-people me-3"></i> Usuarios
                    </a>
                    <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold active">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <div class="col-md-5 mb-4 mb-md-0">
                <div class="card-seccion shadow-sm p-3 bg-white rounded-3 border">
                    <h5 class="fw-bold mb-3 pb-2 border-bottom">Datos Personales</h5>

                    <div class="avatar-perfil text-center flex-column mb-3">
                        <c:choose>
                            <c:when test="${not empty sessionScope.usuario.foto}">
                                <img src="${sessionScope.usuario.foto}" alt="Foto de perfil" class="rounded-circle mb-2" style="width: 80px; height: 80px; object-fit: cover;">
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-person-fill fs-1 text-secondary"></i>
                            </c:otherwise>
                        </c:choose>
                        <div>
                            <small class="text-primary text-decoration-underline cursor-pointer" style="font-size: 0.75rem;">Cambiar imagen</small>
                        </div>
                    </div>

                    <form action="${pageContext.request.contextPath}/actualizarPerfil" method="post">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="nombre" class="form-label fw-bold small">Nombre:</label>
                                <input type="text" name="nombre" class="form-control" id="nombre" value="${sessionScope.usuario.nombre}" placeholder="Nombre" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apeP" class="form-label fw-bold small">Apellido Paterno:</label>
                                <input type="text" name="apeP" class="form-control" id="apeP" value="${sessionScope.usuario.apeP}" placeholder="Apellido Paterno" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="apeM" class="form-label fw-bold small">Apellido Materno:</label>
                                <input type="text" name="apeM" class="form-control" id="apeM" value="${sessionScope.usuario.apeM}" placeholder="Apellido Materno" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="telefono" class="form-label fw-bold small">Teléfono:</label>
                                <input type="tel" name="telefono" class="form-control" id="telefono" value="${sessionScope.usuario.telefono}" placeholder="777-0000-000">
                            </div>
                            <div class="col-md-12 mb-4">
                                <label for="correo" class="form-label fw-bold small">Correo Electrónico:</label>
                                <div class="input-group">
                                    <input type="email" name="correo" class="form-control" id="correo" value="${sessionScope.usuario.correo}" placeholder="correo@utez.edu.mx" required>
                                    <span class="input-group-text"><i class="bi bi-envelope"></i></span>
                                </div>
                            </div>
                        </div>
                        <div class="text-center">
                            <button type="submit" class="btn text-white fw-bold px-4" style="background-color: #0d8a5f; border-radius: 8px;">
                                <i class="bi bi-floppy me-2"></i> Guardar
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card-seccion shadow-sm p-3 bg-white rounded-3 border">
                    <h5 class="fw-bold mb-2 pb-2 border-bottom">Seguridad</h5>
                    <h6 class="text-muted mb-3 small">Cambiar contraseña</h6>

                    <form action="${pageContext.request.contextPath}/cambiarPassword" method="post">
                        <div class="mb-3">
                            <label for="contraActual" class="form-label fw-bold small">Contraseña actual:</label>
                            <input type="password" name="contraActual" class="form-control" id="contraActual" placeholder="Contraseña actual" required>
                        </div>
                        <div class="mb-3">
                            <label for="contraNew" class="form-label fw-bold small">Contraseña nueva:</label>
                            <input type="password" name="contraNew" class="form-control" id="contraNew" placeholder="Contraseña nueva" required>
                        </div>
                        <div class="mb-4">
                            <label for="confirmarContra" class="form-label fw-bold small">Confirmar contraseña:</label>
                            <input type="password" name="confirmarContra" class="form-control" id="confirmarContra" placeholder="Confirmar contraseña" required>
                        </div>
                        <div class="text-center">
                            <button type="submit" class="btn text-white fw-bold px-4" style="background-color: #0d8a5f; border-radius: 8px;">
                                <i class="bi bi-arrow-clockwise me-2"></i> Actualizar
                            </button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>