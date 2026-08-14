<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<style>
    .navbar-custom {
        background-color: #0B1727 !important;
        padding: 0.8rem 1.5rem;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
    }
    .navbar-brand-custom span {
        color: #ffffff;
        font-weight: 800;
        font-size: 1.4rem;
        letter-spacing: 0.5px;
    }
    .navbar-custom .navbar-nav .nav-link {
        color: rgba(255, 255, 255, 0.7) !important;
        font-size: 0.95rem;
        margin: 0 10px;
        padding: 8px 5px;
        font-weight: 500;
        transition: all 0.2s ease;
        border-bottom: none !important;
    }
    .navbar-custom .navbar-nav .nav-link:hover {
        color: #ffffff !important;
    }
    .navbar-custom .navbar-nav .nav-link.active {
        color: #ffffff !important;
        font-weight: 600;
        border-bottom: none !important;
        text-decoration: none !important;
    }
    .btn-nuevo-evento {
        background-color: #10895f !important;
        color: #ffffff !important;
        font-weight: 600;
        border-radius: 6px;
        padding: 0.4rem 1rem;
        border: none;
        transition: background-color 0.2s ease;
    }
    .btn-nuevo-evento:hover {
        background-color: #0c6948 !important;
    }
    .nav-icon-logout {
        background-color: #1e2b45 !important;
        color: #ffffff !important;
        border-radius: 6px;
        width: 38px;
        height: 38px;
        transition: background-color 0.2s ease;
    }
    .nav-icon-logout:hover {
        background-color: #2a3b5c !important;
    }
</style>

<nav class="navbar navbar-expand-lg navbar-custom">
    <div class="container-fluid">
        <!-- LOGO -->
        <a class="navbar-brand-custom d-flex align-items-center text-decoration-none" href="${pageContext.request.contextPath}/evento">
            <img src="img/logo.png" alt="SRAE Logo" style="max-height: 40px; width: auto;" class="me-2">
            <span>srae</span>
        </a>

        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <i class="bi bi-list text-white fs-2"></i>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 mt-2 mt-lg-0 ms-4">

                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/evento">Inicio</a>
                </li>

                <c:if test="${sessionScope.usuario != null && (sessionScope.usuario.idRol == 1 || sessionScope.usuario.idRol == 2)}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/evento?action=gestion">Gestion de Eventos</a>
                    </li>
                </c:if>

                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/evento?action=crear">Gestión de categorias y ubicaciones</a>
                    </li>
                </c:if>

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

            <div class="d-flex align-items-center gap-3 mt-3 mt-lg-0">

                <!-- BOTÓN NUEVO EVENTO (SOLO ORGANIZADOR - ROL 2) -->
                <c:if test="${sessionScope.usuario != null && sessionScope.usuario.idRol == 2}">
                    <a href="${pageContext.request.contextPath}/evento?action=crear" class="btn btn-nuevo-evento shadow-sm d-flex align-items-center text-decoration-none">
                        <i class="bi bi-plus-circle me-2"></i> Nuevo Evento
                    </a>
                </c:if>

                <a href="${pageContext.request.contextPath}/crearPerfil.jsp" class="text-decoration-none" title="Mi perfil">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario.fotoUrl}">
                            <img src="${sessionScope.usuario.fotoUrl}" alt="Perfil" style="width: 38px; height: 38px; object-fit: cover; border-radius: 50%; border: 2px solid rgba(255,255,255,0.2);">
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex align-items-center justify-content-center" style="width: 38px; height: 38px; background-color: rgba(255,255,255,0.1); border-radius: 50%;">
                                <i class="bi bi-person-fill text-white fs-5"></i>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </a>

                <a href="#" data-bs-toggle="modal" data-bs-target="#modalConfirmarLogout" class="nav-icon-logout shadow-sm d-flex align-items-center justify-content-center text-decoration-none" title="Cerrar sesión">
                    <i class="bi bi-box-arrow-right text-white fs-5"></i>
                </a>
            </div>
        </div>
    </div>
</nav>

<div class="modal fade" id="modalConfirmarLogout" tabindex="-1" aria-labelledby="modalConfirmarLogoutLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="modalConfirmarLogoutLabel">Cerrar sesión</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
                ¿Estás seguro de que quieres cerrar sesión?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">Cerrar sesión</a>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/navbar.js"></script>