<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .footer-custom {
        background-color: #0A1429;
        color: #ffffff;
        padding: 1.5rem 0;
        margin-top: auto; /* Empuja el footer hacia abajo si usas un layout flex/grid */
        box-shadow: 0 -4px 6px -1px rgba(0, 0, 0, 0.05);
    }
    .footer-custom .text-muted-custom {
        color: #a0aec0; /* Un gris claro para textos secundarios que contraste bien con el fondo oscuro */
    }
</style>

<footer class="footer-custom">
    <div class="container text-center">
        <p class="mb-1 fw-semibold">&copy; <%= java.time.Year.now().getValue() %> SRAE - Sistema de Reservación y Administración de Eventos.</p>
        <small class="text-muted-custom">Todos los derechos reservados.</small>
    </div>
</footer>