<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - Error del Servidor</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background-color: #f8f9fa; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .error-card { max-width: 500px; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); }
        .error-code { font-size: 5rem; font-weight: 800; color: #184258; }
    </style>
</head>
<body class="d-flex align-items-center justify-content-center min-vh-100">
<div class="card error-card text-center p-5 bg-white border-0">
    <div class="error-code">500</div>
    <h3 class="fw-bold mb-3">Error Interno del Servidor</h3>
    <p class="text-muted mb-4">Ocurrió un problema inesperado al procesar la solicitud. Nuestro equipo técnico ya está al tanto.</p>
    <a href="login.jsp" class="btn btn-danger px-4 py-2 rounded-pill">Regresar al Inicio</a>
</div>
</body>
</html>