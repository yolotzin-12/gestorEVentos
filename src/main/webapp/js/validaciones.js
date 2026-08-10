document.addEventListener('DOMContentLoaded', () => {

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