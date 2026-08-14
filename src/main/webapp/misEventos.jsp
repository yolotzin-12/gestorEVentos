<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis Eventos - SRAE</title>

    <!-- Bootstrap & Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: #333;
        }

        /* Contenedor principal estilo tarjeta flotante */
        .main-card {
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
            padding: 2rem;
            margin-top: 1.5rem;
            margin-bottom: 2rem;
        }

        /* Menú Lateral (Sidebar) */
        .sidebar-menu {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .sidebar-btn {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 14px 20px;
            border-radius: 10px;
            font-weight: 600;
            text-decoration: none;
            color: #2c3e50;
            background-color: #ffffff;
            border: 1px solid #e2e8f0;
            transition: all 0.2s ease;
        }

        .sidebar-btn:hover {
            background-color: #f8fafc;
            color: #162e54;
        }

        /* Botón Activo (Azul Oscuro) */
        .sidebar-btn.active {
            background-color: #11294a;
            color: #ffffff;
            border-color: #11294a;
        }

        /* Botón Salir (Rojo) */
        .sidebar-btn-danger {
            background-color: #cc0000;
            color: #ffffff;
            border: none;
        }

        .sidebar-btn-danger:hover {
            background-color: #a30000;
            color: #ffffff;
        }

        /* Títulos y Secciones */
        .section-title {
            color: #11294a;
            font-weight: 700;
            font-size: 1.5rem;
            border-bottom: 3px solid #0d8a5f;
            padding-bottom: 8px;
            margin-bottom: 1.5rem;
        }

        /* Estilo para la tabla */
        .tabla-eventos {
            border-radius: 10px;
            overflow: hidden;
        }

        .tabla-eventos thead {
            background-color: #f8fafc;
        }

        .tabla-eventos th {
            color: #11294a;
            font-weight: 700;
            padding: 12px 16px;
            border-bottom: 2px solid #e2e8f0;
        }

        .btn-accion {
            width: 32px;
            height: 32px;
            padding: 0;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            border-radius: 6px;
        }
    </style>
</head>
<body>

<div class="container py-4">

    <!-- Header Superior con Logo SRAE -->
    <header class="d-flex justify-content-between align-items-center mb-3 px-2">
        <div class="d-flex align-items-center gap-3">
            <img src="img/logo.png" alt="Logo SRAE" style="height: 50px;">
            <div>
                <h5 class="fw-bold m-0" style="color: #11294a; letter-spacing: 0.5px;">SRAE</h5>
                <small class="text-muted fw-semibold" style="font-size: 0.75rem;">SISTEMA DE RESERVACIÓN Y ADMINISTRACIÓN DE EVENTOS</small>
            </div>


            <nav class="eventos-nav">
                <a href="${pageContext.request.contextPath}/evento">Eventos</a>
                <a href="${pageContext.request.contextPath}/evento?action=misEventos" class="activo">Mis eventos</a>
            </nav>

            <div class="d-flex align-items-center gap-2">
                <a href="evento?action=crear" class="btn btn-success btn-sm fw-bold me-2" style="background-color: #0d8a5f; border: none;">
                    <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                </a>
                <a href="crearPerfil.jsp" class="icono-usuario">
                    <c:choose>
                        <c:when test="${not empty sessionScope.usuario.fotoUrl}">
                            <img src="${sessionScope.usuario.fotoUrl}" alt="Perfil">
                        </c:when>
                        <c:otherwise>
                            <i class="bi bi-person"></i>
                        </c:otherwise>
                    </c:choose>
                </a>
                <a href="logout" class="btn text-white ..." style="background-color: #cc0000;" onclick="confirmarCierreSesion(event)">
                    <i class="bi bi-box-arrow-right"></i>
                </a>
            </div>


        </div>
        <div>
            <a href="logout" class="btn btn-danger btn-sm rounded-3 px-3 py-2" title="Cerrar sesión" onclick="confirmarCierreSesion(event)">
                <i class="bi bi-box-arrow-right fs-6"></i>
            </a>

        </div>
    </header>

    <!-- Tarjeta Principal con Estructura de Columnas (Sidebar + Contenido) -->
    <div class="main-card">
        <div class="row g-4">

            <!-- COLUMNA IZQUIERDA: MENÚ LATERAL -->
            <div class="col-lg-3 col-md-4">
                <nav class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/evento" class="sidebar-btn">
                        <i class="bi bi-house-door fs-5"></i>
                        <span>Inicio</span>
                    </a>

                    <a href="${pageContext.request.contextPath}/evento?action=misEventos" class="sidebar-btn active">
                        <i class="bi bi-calendar-event fs-5"></i>
                        <span>Mis eventos</span>
                    </a>

                    <a href="crearPerfil.jsp" class="sidebar-btn">
                        <i class="bi bi-person fs-5"></i>
                        <span>Mi perfil</span>
                    </a>

                    <a href="logout" class="sidebar-btn sidebar-btn-danger mt-2" onclick="confirmarCierreSesion(event)">
                        <i class="bi bi-box-arrow-right fs-5"></i>
                        <span>Salir</span>
                    </a>
                </nav>
            </div>

            <!-- COLUMNA DERECHA: CONTENIDO DE MIS EVENTOS -->
            <div class="col-lg-9 col-md-8">

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h3 class="section-title m-0">MIS EVENTOS</h3>

                    <div class="d-flex gap-2">
                        <a href="evento?action=crear" class="btn btn-success btn-sm fw-bold px-3 py-2" style="background-color: #0d8a5f; border: none;">
                            <i class="bi bi-plus-circle me-1"></i> Nuevo Evento
                        </a>

                        <c:if test="${not empty listaEventos}">
                            <a href="${pageContext.request.contextPath}/evento?action=limpiarHistorial" class="btn btn-outline-danger btn-sm fw-semibold px-3 py-2" onclick="confirmarLimpiar(event)">
                                <i class="bi bi-broom me-1"></i> Limpiar
                            </a>
                        </c:if>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty listaEventos}">
                        <div class="text-center py-5 border rounded-4 bg-light">
                            <i class="bi bi-calendar-x text-muted display-4"></i>
                            <p class="mt-3 text-muted fw-bold">Aún no has creado ningún evento.</p>
                        </div>
                    </c:when>

                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table align-middle bg-white border tabla-eventos">
                                <thead>
                                <tr>
                                    <th>Evento</th>
                                    <th>Estado</th>
                                    <th>Fecha</th>
                                    <th>Cupos</th>
                                    <th class="text-center">Reservas</th>
                                    <th class="text-center">Acciones</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${listaEventos}" var="evento">
                                    <tr>
                                        <td class="fw-bold" style="color: #11294a;">${evento.nombre}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${evento.eventoFinalizado}">
                                                    <span class="badge bg-secondary px-2 py-1">Finalizado</span>
                                                </c:when>
                                                <c:when test="${evento.estado == 'Cancelado'}">
                                                    <span class="badge bg-danger px-2 py-1">Cancelado</span>
                                                </c:when>
                                                <c:when test="${evento.estado == 'Borrador'}">
                                                    <span class="badge bg-warning text-dark px-2 py-1">Borrador</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-success px-2 py-1">Activo</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-nowrap" style="font-size: 0.9rem;">${evento.fechaHora}</td>
                                        <td style="font-size: 0.9rem;">
                                            <span class="fw-bold">${evento.capacidadDisponible}</span> / ${evento.capacidadMaxima}
                                        </td>
                                        <td class="text-center">
                                            <span class="badge ${evento.totalReservas > 0 ? 'bg-primary' : 'bg-light text-dark border'} px-2 py-1">
                                                <i class="bi bi-people-fill me-1"></i>${evento.totalReservas}
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <div class="d-flex justify-content-center gap-1">
                                                <a href="evento?action=detalle&id=${evento.id}&origen=misEventos" class="btn btn-outline-secondary btn-accion" title="Ver detalle">
                                                    <i class="bi bi-eye"></i>
                                                </a>

                                                <c:if test="${!evento.eventoFinalizado && evento.estado != 'Cancelado'}">
                                                    <a href="evento?action=editar&id=${evento.id}" class="btn btn-outline-primary btn-accion" title="Editar">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                    <a href="evento?action=cancelar&id=${evento.id}" class="btn btn-outline-warning text-dark btn-accion" title="Cancelar" onclick="confirmarCancelar(event)">
                                                        <i class="bi bi-slash-circle"></i>
                                                    </a>
                                                </c:if>

                                                <c:if test="${evento.eventoFinalizado || evento.estado == 'Cancelado'}">
                                                    <a href="evento?action=delete&id=${evento.id}" class="btn btn-outline-danger btn-accion" title="Eliminar" onclick="confirmarEliminar(event)">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>

            </div>

        </div>
    </div>

</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

<script>
    function confirmarCancelar(e) {
        e.preventDefault();
        const url = e.currentTarget.getAttribute('href');
        Swal.fire({
            title: '¿Cancelar este evento?',
            text: "El evento pasará a estar cancelado y no recibirá más reservas.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#ffc107',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, cancelar',
            cancelButtonText: 'Volver'
        }).then((result) => {
            if (result.isConfirmed) { window.location.href = url; }
        });
    }

    function confirmarLimpiar(e) {
        e.preventDefault();
        const url = e.currentTarget.getAttribute('href');
        Swal.fire({
            title: '¿Limpiar historial?',
            text: "Se borrarán de forma definitiva los eventos Finalizados y Cancelados.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, limpiar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) { window.location.href = url; }
        });
    }

    function confirmarEliminar(e) {
        e.preventDefault();
        const url = e.currentTarget.getAttribute('href');
        Swal.fire({
            title: '¿Eliminar evento?',
            text: "Esta acción no se puede deshacer.",
            icon: 'error',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) { window.location.href = url; }
        });
    }
</script>

</body>
</html>