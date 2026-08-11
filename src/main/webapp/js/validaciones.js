document.addEventListener('DOMContentLoaded', () => {

    if (window.history.replaceState) {
        window.history.replaceState(null, null, window.location.href);
    }
    // ---------------------------------------------------------------------------

    const serverError = document.getElementById('serverError').value.trim();
    const serverMensaje = document.getElementById('serverMensaje').value.trim();
    const alertasContainer = document.getElementById('alertasContainer');

    if (serverError !== "") {
        alertasContainer.innerHTML = `
            <div class="alert alert-danger d-flex align-items-center py-2 text-start" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i>
                <div class="small">${serverError}</div>
            </div>
        `;

        document.getElementById('email').classList.add('is-invalid');
        document.getElementById('contra').classList.add('is-invalid');

        // Poner labels en rojo
        document.querySelector('label[for="email"]').classList.add('text-danger');
        document.querySelector('label[for="contra"]').classList.add('text-danger');
    }

    if (serverMensaje !== "") {
        alertasContainer.innerHTML = `
            <div class="alert alert-success d-flex align-items-center py-2 text-start" role="alert">
                <i class="bi bi-check-circle-fill me-2"></i>
                <div class="small">${serverMensaje}</div>
            </div>
        `;
    }
});