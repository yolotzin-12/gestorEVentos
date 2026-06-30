<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Principal Reservas - SRAE</title>

    <link rel="stylesheet" href="css/fooyini.css">
    <link rel="stylesheet" href="css/pagprin.css">
    <link rel="stylesheet" href="css/sidebar.css">
    <link rel="stylesheet" href="css/perfil.css">
    <link rel="stylesheet" href="css/reservas.css">
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
                <small class="text-muted fw-semibold">SISTEMA DE RESERVACIÓN Y ADMINISTRACION DE EVENTOS</small>
            </div>
        </div>
        <div class="d-flex align-items-center">
            <a href="index.jsp" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4 bg-white">
        <div class="row">

            <div class="col-md-7 d-flex flex-column justify-content-between">
                <div>
                    <h4 class="fw-bold text-dark mb-1">CONFERENCIA MAGISTRAL:</h4>
                    <h5 class="fw-semibold text-secondary mb-3">INNOVACIÓN TECNOLÓGICA</h5>

                    <div class="mb-4 text-center">
                        <img src="img/personas.jpg" alt="Auditorio" class="img-fluid rounded-4 shadow-sm" style="max-height: 250px; width: 100%; object-fit: cover;">
                    </div>
                </div>

                <div class="row g-3">
                    <div class="col-6">
                        <div class="badge-gris text-dark">
                            FECHA: 15/12/26
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="badge-gris text-dark">
                            Auditorio Principal
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-5 mt-4 mt-md-0">
                <div class="card-aforo text-center shadow-sm">

                    <div class="my-3 d-flex align-items-center justify-content-center" style="width: 180px; height: 180px;">
                        <div class="circulo-progreso" id="circuloProgreso">
                            <div class="fs-3 fw-bold m-0 text-dark">
                                <span id="registrados">135</span><span class="fs-4 fw-normal text-muted">/<span id="cupoMaximo">200</span></span>
                            </div>
                            <div class="fw-bold text-muted mt-1" style="font-size: 0.9rem;"><span id="porcentaje">67.5</span>%</div>
                        </div>
                    </div>

                    <div class="mb-4">
                        <p class="fw-bold text-dark m-0">Aforo disponible</p>
                        <small class="text-muted fw-semibold">(67.5%)</small>
                    </div>

                    <a href="reservar.jsp" class="text-decoration-none w-100">
                        <button type="button" class="btn fs-5 d-flex align-items-center justify-content-center gap-2" style="background-color: #0d8a5f !important; color: #ffffff !important; font-weight: bold !important; border-radius: 10px !important; width: 100% !important; padding: 10px !important; border: none !important;">
                            <i class="bi bi-calendar-check" style="color: #ffffff !important;"></i> Reservar
                        </button>
                    </a>

                    <br>

                    <a href="cancelar.jsp" class="text-decoration-none w-100">
                        <button type="button" class="btn fs-5 d-flex align-items-center justify-content-center gap-2" style="background-color: #dc3545 !important; color: #ffffff !important; font-weight: bold !important; border-radius: 10px !important; width: 100% !important; padding: 10px !important; border: none !important;">
                            <i class="bi bi-calendar-x" style="color: #ffffff !important;"></i> Cancelar
                        </button>
                    </a>

                </div>
            </div>

        </div>
    </div>
</div>

<footer>
    <div><i class="bi bi-people-fill"></i> CONTÁCTANOS</div>
    <div><i class="bi bi-telephone-fill"></i> 777-0000-000</div>
    <div><i class="bi bi-envelope-fill"></i> CORREO@UTEZ.EDU.MX</div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>