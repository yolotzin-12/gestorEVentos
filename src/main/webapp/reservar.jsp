<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservación - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/reservaModal.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        /* Anillo tipo donut: fondo azul marino, progreso en verde/teal,
           centro blanco. Se pone en rojo cuando ya no hay cupo. */
        .circulo-progreso-modal {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            margin: 0 auto;
        }
        .circulo-progreso-modal::after {
            content: '';
            position: absolute;
            width: 118px;
            height: 118px;
            background-color: #ffffff;
            border-radius: 50%;
        }
        .circulo-progreso-modal .contenido-circulo {
            position: relative;
            z-index: 1;
        }
    </style>
</head>
<body class="bg-light">

<c:choose>
    <c:when test="${not empty evento}">

        <c:set var="cupoMax" value="${evento.capacidadMaxima}" />
        <c:set var="cupoDisp" value="${evento.capacidadDisponible}" />
        <c:set var="registrados" value="${cupoMax - cupoDisp}" />
        <c:set var="sinCupo" value="${cupoDisp <= 0}" />

        <%-- Separa fecha y hora si vienen juntas como "dd/mm/aaaa hh:mm" --%>
        <c:set var="fechaHoraTexto" value="${evento.fechaHora}" />
        <c:set var="partesFecha" value="${fn:split(fechaHoraTexto, ' ')}" />
        <c:set var="soloFecha" value="${partesFecha[0]}" />
        <c:choose>
            <c:when test="${fn:length(partesFecha) > 1}">
                <c:set var="soloHora" value="${partesFecha[1]}" />
            </c:when>
            <c:otherwise>
                <c:set var="soloHora" value="Por definir" />
            </c:otherwise>
        </c:choose>

        <div class="container my-5 d-flex justify-content-center">

            <div class="card p-4 shadow modal-reservacion w-100" style="max-width: 900px;">

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="titulo-reserva m-0">FORMULARIO DE RESERVACIÓN</h4>
                    <a href="evento" class="text-secondary fs-4"><i class="bi bi-x-lg"></i></a>
                </div>

                <div class="row align-items-stretch">

                    <div class="col-md-7 pe-md-4 border-end">

                        <form action="reserva" method="post" id="formReserva">

                            <input type="hidden" name="action" value="reservar">
                            <input type="hidden" name="idEvento" value="${evento.id}">

                            <div class="mb-3">
                                <label for="nombre" class="form-label text-muted fw-semibold mb-1">Nombre completo</label>
                                <input type="text" name="nombre" class="form-control rounded-3" id="nombre" placeholder="Tu nombre:" required>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label text-muted fw-semibold mb-1">Correo electrónico</label>
                                <input type="email" name="email" class="form-control rounded-3" id="email" placeholder="Tu correo electrónico:" required>
                            </div>

                            <div class="mb-4">
                                <label for="asistencia" class="form-label text-muted fw-semibold mb-1">Motivo de asistencia</label>
                                <input type="text" name="asistencia" class="form-control rounded-3" id="asistencia" placeholder="Proyecto académico:" required>
                            </div>

                            <div class="text-start">
                                <c:choose>
                                    <c:when test="${sinCupo}">
                                        <button type="button" id="btnSinCupo" class="btn btn-confirmar fw-bold py-2 px-4 shadow-sm fs-5" style="opacity:0.6; cursor:not-allowed;">
                                            <i class="bi bi-x-circle me-2"></i> Sin cupo disponible
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="submit" class="btn btn-confirmar fw-bold py-2 px-4 shadow-sm fs-5">
                                            <i class="bi bi-check-lg me-2"></i> Confirmar reserva
                                        </button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </form>

                    </div>

                    <div class="col-md-5 ps-md-4 d-flex flex-column justify-content-between mt-4 mt-md-0 text-center">

                        <div class="d-flex flex-column gap-3">
                            <div class="badge-info-modal">
                                FECHA: <c:out value="${soloFecha}" default="Por definir" />
                            </div>

                            <div class="badge-info-modal">
                                HORA: <c:out value="${soloHora}" default="Por definir" />
                            </div>

                            <div class="badge-info-modal">
                                <c:out value="${evento.ubicacion}" default="Por definir" />
                            </div>
                        </div>

                        <div class="my-auto pt-4">
                            <div class="circulo-progreso-modal" id="circuloProgreso">
                                <div class="contenido-circulo">
                                    <div class="fs-4 fw-bold m-0 text-dark">
                                        <span id="registrados">${registrados}</span><span class="fs-5 fw-normal text-muted">/<span id="cupoMaximo">${cupoMax}</span></span>
                                    </div>
                                    <small class="fw-bold text-muted d-block" style="font-size: 0.8rem;"><span id="porcentaje">0</span>%</small>
                                </div>
                            </div>
                            <p class="fw-bold text-secondary mt-2 mb-0" style="font-size: 0.9rem;">Aforo actual</p>
                        </div>

                    </div>
                </div>

            </div>
        </div>

        <!-- Modal: no hay disponibilidad -->
        <div class="modal fade" id="modalSinCupo" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content rounded-4">
                    <div class="modal-header border-0">
                        <h5 class="modal-title fw-bold" style="color:#162e54;">
                            <i class="bi bi-exclamation-triangle-fill text-danger me-2"></i>Sin disponibilidad
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                    </div>
                    <div class="modal-body">
                        <p class="mb-0">Lo sentimos, este evento ya no tiene lugares disponibles. Puedes explorar otros eventos que sí tengan cupo.</p>
                    </div>
                    <div class="modal-footer border-0">
                        <button type="button" class="btn btn-secondary rounded-3" data-bs-dismiss="modal">Cancelar</button>
                        <a href="evento" class="btn btn-confirmar rounded-3">
                            <i class="bi bi-calendar-event me-1"></i> Ver más eventos
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <script>
            (function () {
                var cupoMax = ${cupoMax};
                var registrados = ${registrados};
                var porcentaje = cupoMax > 0 ? Math.round((registrados / cupoMax) * 1000) / 10 : 0;

                document.getElementById('porcentaje').textContent = porcentaje;

                var circulo = document.getElementById('circuloProgreso');

                // Verde/teal mientras haya cupo, rojo en cuanto se llena (100%)
                var colorProgreso = porcentaje >= 100 ? '#dc3545' : '#0d8a5f';
                var colorFondo = '#162e54';

                circulo.style.background =
                    'conic-gradient(' + colorProgreso + ' ' + (porcentaje * 3.6) + 'deg, ' + colorFondo + ' 0deg)';
            })();

            <c:if test="${sinCupo}">
            document.getElementById('btnSinCupo').addEventListener('click', function () {
                var modal = new bootstrap.Modal(document.getElementById('modalSinCupo'));
                modal.show();
            });
            </c:if>
        </script>

    </c:when>

    <c:otherwise>
        <div class="container my-5 d-flex justify-content-center">
            <div class="card p-4 shadow modal-reservacion w-100 text-center" style="max-width: 600px;">
                <h4 class="fw-bold mb-3" style="color:#162e54;">Evento no encontrado</h4>
                <p class="text-muted mb-4">No fue posible cargar la información de este evento.</p>
                <a href="evento" class="btn btn-confirmar fw-bold py-2 px-4 shadow-sm fs-5 mx-auto" style="max-width:300px;">
                    <i class="bi bi-arrow-left me-2"></i> Volver a eventos
                </a>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/cierresesion.js"></script>

</body>
</html>