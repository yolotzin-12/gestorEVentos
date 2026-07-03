<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear Evento - SRAE</title>

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
            <a href="evento" class="btn text-white fw-bold p-2 rounded-3 shadow-sm" style="background-color: #162e54;">
                <i class="bi bi-eye"></i> Ver Eventos
            </a>
            <a href="index.jsp" class="btn text-white d-flex align-items-center justify-content-center p-2 rounded-3" style="background-color: #cc0000; width: 40px; height: 40px;">
                <i class="bi bi-box-arrow-right fs-5"></i>
            </a>
        </div>
    </div>

    <div class="card p-4 shadow-sm border-0 rounded-4">
        <h4 class="fw-bold pb-2 mb-4" style="border-bottom: 3px solid #0d8a5f; color: #1a1a1a;">CREACIÓN DEL EVENTO</h4>

        <form action="evento" method="post">
            <input type="hidden" name="estado" value="true">

            <div class="row">
                <div class="col-md-6 d-flex flex-column justify-content-between">

                    <div class="mb-3">
                        <label for="categoria" class="form-label fw-bold text-dark m-1">Categoría</label>
                        <select name="categoria" class="form-select p-2 rounded-3" id="categoria" required>
                            <option value="" disabled selected>Selecciona una categoría</option>
                            <option value="Academicos">Académicos</option>
                            <option value="Deportivos">Deportivos</option>
                            <option value="Culturales">Culturales</option>
                            <option value="Conferencias">Conferencias</option>
                            <option value="Otros">Otros</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="nombre" class="form-label fw-bold text-dark m-1">Nombre del evento</label>
                        <input type="text" name="nombre" class="form-control p-2 rounded-3" id="nombre" placeholder="Ingresa el nombre del evento" required>
                    </div>

                    <div class="mb-3">
                        <label for="ubicacion" class="form-label fw-bold text-dark m-1">Ubicación</label>
                        <input type="text" name="ubicacion" class="form-control p-2 rounded-3" id="ubicacion" placeholder="Ingresa la dirección de la ubicación" required>
                    </div>

                    <div class="mb-3">
                        <label for="fecha" class="form-label fw-bold text-dark m-1">Fecha del evento</label>
                        <input type="date" name="fecha" class="form-control p-2 rounded-3" id="fecha" required>
                    </div>

                    <div class="mb-3">
                        <label for="capacidad" class="form-label fw-bold text-dark m-1">Capacidad máxima</label>
                        <input type="number" name="capacidad" class="form-control p-2 rounded-3" id="capacidad" placeholder="Ej. 100" required min="1">
                    </div>

                </div>

                <div class="col-md-6 d-flex flex-column justify-content-start mt-md-0 mt-3">
                    <label class="form-label fw-bold text-dark m-1">Imagen del evento <i class="bi bi-calendar4-event"></i></label>
                    <div class="border text-center p-4 rounded-3 bg-white d-flex flex-column align-items-center justify-content-center flex-grow-1" style="border-style: dashed !important; min-height: 340px;">
                        <label for="img" class="btn text-white fw-bold px-4 py-2 mb-3 d-inline-flex align-items-center shadow-sm" style="background-color: #0d8a5f; border-radius: 10px; cursor: pointer;">
                            <i class="bi bi-upload me-2"></i> Seleccionar imagen
                        </label>
                        <input type="file" name="img" id="img" accept="image/*" class="d-none">
                        <small class="text-muted">Formatos permitidos: JPG, PNG, Máx 10MB</small>
                    </div>
                </div>
            </div>

            <div class="d-flex justify-content-end mt-4">
                <button type="submit" class="btn text-white fw-bold py-2 px-4 shadow-sm d-inline-flex align-items-center" style="background-color: #0d8a5f; border-radius: 10px;">
                    <i class="bi bi-send-fill me-2"></i> Publicar evento
                </button>
            </div>
        </form>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>