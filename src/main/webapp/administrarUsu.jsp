<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrar Usuarios - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/administrarUsuarios.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="d-flex align-items-center">
            <img src="img/logo.png" alt="Logo SRAE" style="height:70px;">
            <img src="img/letras.png" alt="SRAE" style="height:120px;">
            <div>
                <h5 class="fw-bold m-0" style="color: #162e54;">SRAE</h5>
                <small class="text-muted fw-semibold">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-3 mb-4 mb-md-0">
                <div class="d-flex flex-column gap-1">
                    <a href="evento" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-house-door me-3"></i> Inicio
                    </a>

                    <a href="evento" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-calendar-event me-3"></i> Eventos
                    </a>

                    <a href="${pageContext.request.contextPath}/usuarios" class="btn sidebar-btn py-3 px-4 fw-bold active">
                        <i class="bi bi-people me-3"></i> Usuarios
                    </a>

                    <a href="crearPerfil.jsp" class="btn sidebar-btn py-3 px-4 fw-bold">
                        <i class="bi bi-person me-3"></i> Mi perfil
                    </a>

                    <a href="logout" class="btn sidebar-btn py-3 px-4 fw-bold text-danger">
                        <i class="bi bi-box-arrow-left me-3"></i> Salir
                    </a>
                </div>
            </div>

            <div class="col-md-9">
                <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">ADMINISTRAR USUARIOS</h4>

                <div class="row mb-4 gx-2">
                    <div class="col-md-6 position-relative mb-2 mb-md-0">
                        <i class="bi bi-search position-absolute top-50 start-0 translate-middle-y ms-3 text-muted fs-5"></i>
                        <input type="text" id="inputBusqueda" class="form-control input-busqueda-admin w-100" placeholder="Buscar por nombre o correo...">
                    </div>
                    <div class="col-md-3 mb-2 mb-md-0">
                        <select id="selectFiltroRol" class="form-select input-busqueda-admin w-100 text-muted" style="padding-left: 15px;">
                            <option value="todos">Todos los roles</option>
                            <option value="2">Organizador</option>
                            <option value="3">Asistente</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <select id="selectFiltroEstado" class="form-select input-busqueda-admin w-100 text-muted" style="padding-left: 15px;">
                            <option value="todos">Todos los estados</option>
                            <option value="activo">Activo</option>
                            <option value="inactivo">Inactivo</option>
                        </select>
                    </div>
                </div>

                <div class="table-responsive shadow-sm tabla-admin">
                    <table class="table table-hover m-0" id="tablaUsuarios">
                        <thead>
                        <tr>
                            <th scope="col">Foto de perfil</th>
                            <th scope="col">Nombre completo</th>
                            <th scope="col">Correo electrónico</th>
                            <th scope="col">Rol</th>
                            <th scope="col" class="text-center">Estado</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="u" items="${listaUsuarios}">
                            <tr class="fila-usuario">
                                <td>
                                    <img src="${not empty u.fotoUrl ? u.fotoUrl : 'https://cdn-icons-png.flaticon.com/512/3135/3135715.png'}" alt="User" class="avatar-usuario ms-2">
                                </td>
                                <td>
                                    <div class="fw-semibold text-dark item-nombre">
                                            ${u.nombre} ${u.apellidoPaterno} ${u.apellidoMaterno != null ? u.apellidoMaterno : ''}
                                    </div>
                                </td>
                                <td>
                                    <div class="text-dark item-correo">${u.email}</div>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/usuarios" method="POST" class="m-0">
                                        <input type="hidden" name="action" value="asignarRol">
                                        <input type="hidden" name="idUsuario" value="${u.id}">

                                        <c:choose>
                                            <c:when test="${u.idRol == 1}">
                                                <select class="form-select select-rol-admin" disabled>
                                                    <option value="1" selected>Administrador</option>
                                                </select>
                                            </c:when>
                                            <c:otherwise>
                                                <select name="idRol" class="form-select select-rol-admin" data-original-value="${u.idRol}" onchange="confirmarCambioRol(this)">
                                                    <option value="3" ${u.idRol == 3 ? 'selected' : ''}>Asistente</option>
                                                    <option value="2" ${u.idRol == 2 ? 'selected' : ''}>Organizador</option>
                                                </select>
                                            </c:otherwise>
                                        </c:choose>
                                    </form>
                                </td>
                                <td class="text-center">
                                    <form action="${pageContext.request.contextPath}/usuarios" method="POST" class="m-0 d-inline-block">
                                        <input type="hidden" name="action" value="cambiarEstado">
                                        <input type="hidden" name="idUsuario" value="${u.id}">
                                        <input type="hidden" name="estado" value="${!u.activo}">
                                        <div class="form-check form-switch d-inline-block">
                                            <input class="form-check-input check-estado-usuario" type="checkbox" role="switch" ${u.activo ? 'checked' : ''} onchange="confirmarCambioEstado(this, ${u.activo})">
                                        </div>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listaUsuarios}">
                            <tr>
                                <td colspan="5" class="text-center py-4 text-muted">No hay usuarios registrados en la base de datos.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/Adminusu.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>