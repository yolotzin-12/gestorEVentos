document.addEventListener("DOMContentLoaded", function() {

    if (window.history.replaceState) {
        const cleanUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        window.history.replaceState(null, null, cleanUrl);
    }

    const alertasContainer = document.getElementById('alertasContainerPerfil');
    const serverErrorInput = document.getElementById('serverErrorPerfil');
    const serverSuccessInput = document.getElementById('serverSuccessPerfil');

    if (serverSuccessInput && serverSuccessInput.value === 'true') {
        // Mostramos el mensaje, al darle "Aceptar" redirige a logout
        alert("Contraseña actualizada correctamente. Tendrá que iniciar sesión nuevamente.");
        window.location.href = 'logout';
    }

    const errorMsg = serverErrorInput ? serverErrorInput.value.trim() : "";
    if (errorMsg !== "") {
        if (alertasContainer) {
            alertasContainer.innerHTML = `
                <div class="alert alert-danger d-flex align-items-center py-2 text-start" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>
                    <div class="small">${errorMsg}</div>
                </div>
            `;
        }

        const inputActual = document.getElementById('contraActual');
        const labelActual = document.querySelector('label[for="contraActual"]');

        if (inputActual) inputActual.classList.add('is-invalid');
        if (labelActual) labelActual.classList.add('text-danger');
    }

    const toggleButtons = document.querySelectorAll('.toggle-password');
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
            const input = document.getElementById(targetId);
            const icon = this.querySelector('i');

            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            }
        });
    });

    const form = document.getElementById('formCambiarContra');
    const contraNew = document.getElementById('contraNew');
    const confirmarContra = document.getElementById('confirmarContra');
    const errorLongitud = document.getElementById('errorLongitud');
    const errorCoincidencia = document.getElementById('errorCoincidencia');

    if (form) {
        form.addEventListener('submit', function(event) {
            let isValid = true;

            if (contraNew.value.length < 8) {
                errorLongitud.style.display = 'block';
                contraNew.classList.add('is-invalid');
                isValid = false;
            } else {
                errorLongitud.style.display = 'none';
                contraNew.classList.remove('is-invalid');
            }

            if (contraNew.value !== confirmarContra.value) {
                errorCoincidencia.style.display = 'block';
                confirmarContra.classList.add('is-invalid');
                isValid = false;
            } else {
                errorCoincidencia.style.display = 'none';
                confirmarContra.classList.remove('is-invalid');
            }

            if (!isValid) {
                event.preventDefault();
            }
        });

        contraNew.addEventListener('input', function() {
            if (contraNew.value.length >= 8) {
                errorLongitud.style.display = 'none';
                contraNew.classList.remove('is-invalid');
            }
        });

        confirmarContra.addEventListener('input', function() {
            if (contraNew.value === confirmarContra.value) {
                errorCoincidencia.style.display = 'none';
                confirmarContra.classList.remove('is-invalid');
            }
        });
    }
});