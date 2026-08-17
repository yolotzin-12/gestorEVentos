<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Eventos - SRAE</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="css/eventos.css">
    <link rel="stylesheet" href="css/navbar.css"> <!-- Aquí ya tienes los estilos de tu navbar -->

    <style>
        /* Estilos exclusivos para tarjetas de eventos */
        .tarjeta-evento.evento-finalizado {
            opacity: 0.55;
            filter: grayscale(60%);
        }
        .tarjeta-evento.evento-finalizado .tarjeta-evento-link {
            cursor: pointer;
        }
        .badge-finalizado {
            background-color: #6c757d;
            color: #fff;
            font-size: 0.7rem;
            font-weight: bold;
            border-radius: 20px;
            padding: 4px 10px;
            flex-shrink: 0;
        }
    </style>
</head>
<body class="eventos-body d-flex flex-column min-vh-100 bg-light">

<!-- ================= NAVBAR SUPERIOR ================= -->
<jsp:include page="navbar.jsp">
    <jsp:param name="activePage" value="eventos" />
</jsp:include>

<!-- ================= CONTENIDO PRINCIPAL ================= -->
<main class="flex-grow-1">
    <div class="container">

        <div class="barra-filtros">
            <div class="buscador-evento">
                <i class="bi bi-search"></i>
                <input type="text" name="buscar" placeholder="Buscar evento" autocomplete="off">
            </div>
        </div>

        <c:choose>
            <c:when test="${empty listaEventos}">
                <div class="col-12 text-center py-5">
                    <i class="bi bi-calendar-x text-muted fs-1"></i>
                    <p class="mt-2 text-muted fw-bold">No hay eventos disponibles por el momento.</p>
                </div>
            </c:when>

            <c:otherwise>
                <div class="row g-4 mb-4" id="listaEventosGrid">
                    <c:forEach items="${listaEventos}" var="evento">
                        <div class="col-6 col-md-3"
                             data-nombre-evento="${fn:toLowerCase(evento.nombre)}"
                             data-ubicacion-evento="${fn:toLowerCase(evento.ubicacion)}">

                            <div class="tarjeta-evento d-flex flex-column h-100 ${evento.eventoFinalizado ? 'evento-finalizado' : ''}">

                                <c:choose>
                                    <c:when test="${evento.eventoFinalizado}">
                                        <a href="#" class="tarjeta-evento-link d-flex flex-column h-100" style="text-decoration: none; color: inherit;"
                                           onclick="mostrarEventoFinalizado(event, '${evento.id}', '${fn:escapeXml(evento.nombre)}', ${evento.idOrganizador})">
                                            <div class="encabezado-evento">
                                                <div class="d-flex justify-content-between align-items-start gap-2">
                                                    <h3>${evento.nombre}</h3>
                                                    <span class="badge-finalizado">Finalizado</span>
                                                </div>
                                                <p><c:out value="${evento.nombreCategoria}" default="General"/></p>
                                            </div>

                                            <c:choose>
                                                <c:when test="${not empty evento.imagenUrl}">
                                                    <img src="${evento.imagenUrl}" alt="${evento.nombre}" class="imagen-evento">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                                </c:otherwise>
                                            </c:choose>

                                            <div class="pie-evento mt-auto">
                                                <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                                <div><i class="bi bi-geo-alt-fill"></i> <c:out value="${evento.ubicacion}" default="Sin ubicación"/></div>
                                            </div>
                                        </a>
                                    </c:when>

                                    <c:otherwise>
                                        <a href="evento?action=detalle&id=${evento.id}" class="tarjeta-evento-link d-flex flex-column h-100" style="text-decoration: none; color: inherit;">
                                            <div class="encabezado-evento">
                                                <div class="d-flex justify-content-between align-items-start gap-2">
                                                    <h3>${evento.nombre}</h3>
                                                    <c:if test="${evento.estado == 'Borrador'}">
                                                        <span class="badge bg-warning text-dark flex-shrink-0">Borrador</span>
                                                    </c:if>
                                                </div>
                                                <p><c:out value="${evento.nombreCategoria}" default="General"/></p>
                                            </div>

                                            <c:choose>
                                                <c:when test="${not empty evento.imagenUrl}">
                                                    <img src="${evento.imagenUrl}" alt="${evento.nombre}" class="imagen-evento">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="img/personas.jpg" alt="Evento" class="imagen-evento">
                                                </c:otherwise>
                                            </c:choose>

                                            <div class="pie-evento mt-auto">
                                                <div><i class="bi bi-calendar-event"></i> ${evento.fechaHora}</div>
                                                <div><i class="bi bi-geo-alt-fill"></i> <c:out value="${evento.ubicacion}" default="Sin ubicación"/></div>
                                            </div>
                                        </a>
                                    </c:otherwise>
                                </c:choose>

                            </div>

                        </div>
                    </c:forEach>
                </div>

                <div id="sinResultadosBusqueda" class="alert alert-secondary text-center rounded-4 shadow-sm" style="display:none;">
                    <i class="bi bi-search"></i> No se encontraron eventos con ese nombre o ubicación.
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<!-- ================= MODAL ================= -->
<div class="modal fade" id="modalEventoFinalizado" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content rounded-4 border-0 shadow-lg">
            <div class="modal-header bg-secondary text-white border-0 rounded-top-4">
                <h5 class="modal-title fw-bold">
                    <i class="bi bi-calendar-x-fill me-2"></i>EVENTO FINALIZADO
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body p-4 text-center">
                <h5 id="modalNombreEvento" class="fw-bold mb-3" style="color: #162e54;"></h5>
                <p class="text-muted mb-0">Este evento ya ha concluido y no admite más reservaciones.</p>
            </div>
            <div class="modal-footer border-0 d-flex justify-content-between bg-light rounded-bottom-4">
                <button type="button" class="btn btn-secondary px-4 rounded-3" data-bs-dismiss="modal">Entendido</button>

                <%-- El botón se muestra/oculta dinámicamente vía JS, comparando
                     el dueño del evento con el organizador en sesión --%>
                <a id="btnEliminarEventoModal" href="#" class="btn btn-danger fw-bold rounded-3" style="display:none;" onclick="confirmarEliminarModal(event)">
                    <i class="bi bi-trash-fill me-1"></i> Eliminar evento
                </a>
            </div>
        </div>
    </div>
</div>

<!-- ================= FOOTER INFERIOR ================= -->
<jsp:include page="footer.jsp" />

<!-- ================= SCRIPTS ================= -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/buscador.js"></script>
<script src="js/categorias.js"></script>

<script>
    // Rol e id del organizador en sesión (null/-1 si no aplica).
    // Se usan en el modal para decidir si se muestra "Eliminar evento".
    const ROL_USUARIO = <c:out value="${sessionScope.usuario != null ? sessionScope.usuario.idRol : 0}"/>;
    const ID_ORGANIZADOR_SESION = <c:out value="${not empty idOrganizadorSesion ? idOrganizadorSesion : -1}"/>;

    function mostrarEventoFinalizado(event, idEvento, nombreEvento, idOrganizadorEvento) {
        event.preventDefault();

        document.getElementById('modalNombreEvento').innerText = nombreEvento;

        var btnEliminar = document.getElementById('btnEliminarEventoModal');
        if (btnEliminar) {
            // Solo el organizador dueño del evento puede ver el botón.
            // El admin (rol 1) nunca lo ve.
            var esDueno = (ROL_USUARIO === 2 && idOrganizadorEvento === ID_ORGANIZADOR_SESION);

            if (esDueno) {
                btnEliminar.style.display = 'inline-block';
                btnEliminar.setAttribute('href', '${pageContext.request.contextPath}/evento?action=delete&id=' + idEvento);
            } else {
                btnEliminar.style.display = 'none';
                btnEliminar.removeAttribute('href');
            }
        }

        var modal = new bootstrap.Modal(document.getElementById('modalEventoFinalizado'));
        modal.show();
    }

    function confirmarEliminarModal(e) {
        e.preventDefault();
        const url = e.currentTarget.getAttribute('href');
        if (!url) return;
        Swal.fire({
            title: '¿Eliminar evento?',
            text: "Esta acción no se puede deshacer.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#dc3545',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, eliminar'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = url;
            }
        });
    }
</script>

</body>
</html>