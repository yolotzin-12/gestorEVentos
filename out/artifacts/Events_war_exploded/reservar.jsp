<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
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
</head>
<body class="bg-light">

<div class="container my-5 d-flex justify-content-center">

    <div class="card p-4 shadow modal-reservacion w-100" style="max-width: 900px;">

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4 class="titulo-reserva m-0">FORMULARIO DE RESERVACIÓN</h4>
            <a href="detalle-evento.jsp" class="text-secondary fs-4"><i class="bi bi-x-lg"></i></a>
        </div>

        <div class="row align-items-stretch">

            <div class="col-md-7 pe-md-4 border-end">
                <form action="#" method="post">

                    <div class="mb-3">
                        <label for="nombre" class="form-label text-muted fw-semibold mb-1">Nombre completo</label>
                        <input type="text" name="nombre" class="form-control rounded-3" id="nombre" placeholder="Tu nombre:" required>
                    </div>

                    <div class="row mb-3">
                        <div class="col-6">
                            <label for="matricula" class="form-label text-muted fw-semibold mb-1">Matrícula:</label>
                            <input type="text" name="matricula" class="form-control rounded-3" id="matricula" placeholder="Matrícula:" required>
                        </div>
                        <div class="col-6">
                            <label for="carrera" class="form-label text-muted fw-semibold mb-1">Carrera</label>
                            <input type="text" name="carrera" class="form-control rounded-3" id="carrera" placeholder="Carrera" required>
                        </div>
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
                        <button type="submit" class="btn btn-confirmar fw-bold py-2 px-4 shadow-sm fs-5">
                            <i class="bi bi-check-lg me-2"></i> Confirmar reserva
                        </button>
                    </div>
                </form>
            </div>

            <div class="col-md-5 ps-md-4 d-flex flex-column justify-content-between mt-4 mt-md-0 text-center">

                <div class="d-flex flex-column gap-3">
                    <div class="badge-info-modal">
                        FECHA: 15/12/26
                    </div>

                    <div class="badge-info-modal">
                        Auditorio Principal
                    </div>
                </div>

                <div class="my-auto pt-4">
                    <div class="circulo-progreso-modal" id="circuloProgreso">
                        <div class="fs-4 fw-bold m-0 text-dark pt-4">
                            <span id="registrados">135</span><span class="fs-5 fw-normal text-muted">/<span id="cupoMaximo">200</span></span>
                        </div>
                        <small class="fw-bold text-muted" style="font-size: 0.8rem;"><span id="porcentaje">67.5</span>%</small>
                    </div>
                    <p class="fw-bold text-secondary mt-2 mb-0" style="font-size: 0.9rem;">Aforo actual</p>
                </div>

            </div>
        </div>

    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>