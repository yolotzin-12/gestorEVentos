function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function(){
        const output = document.getElementById('previewFoto');
        const defaultIcon = document.getElementById('defaultIcon');
        output.src = reader.result;
        output.classList.remove('d-none');
        if(defaultIcon) defaultIcon.classList.add('d-none');
    };
    if (event.target.files[0]) {
        reader.readAsDataURL(event.target.files[0]);
    }
}

document.addEventListener("DOMContentLoaded", function() {

    const updateSuccessInput = document.getElementById('updateSuccess');
    if (updateSuccessInput) {
        const updateStatus = updateSuccessInput.value;
        if (updateStatus === 'success') {
            Swal.fire({
                title: '¡Perfil Actualizado!',
                text: 'Tus datos se han guardado correctamente.',
                icon: 'success',
                confirmButtonColor: '#0d8a5f'
            });
        } else if (updateStatus === 'error') {
            Swal.fire({
                title: 'Error',
                text: 'Hubo un problema al guardar tus datos. Inténtalo de nuevo.',
                icon: 'error',
                confirmButtonColor: '#162e54'
            });
        }
    }

    if (window.history.replaceState) {
        const cleanUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
        window.history.replaceState(null, null, cleanUrl);
    }

    const formPerfil = document.getElementById('formActualizarPerfil');
    const telefonoInput = document.getElementById('telefono');
    const errorTelefono = document.getElementById('errorTelefono');

    if (formPerfil && telefonoInput) {
        telefonoInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, ''); // Quita todo lo que no sea número

            if (this.value.length === 10 || this.value.length === 0) {
                errorTelefono.style.display = 'none';
                this.classList.remove('is-invalid');
            }
        });

        formPerfil.addEventListener('submit', function(event) {
            if (telefonoInput.value.length > 0 && telefonoInput.value.length !== 10) {
                errorTelefono.style.display = 'block';
                telefonoInput.classList.add('is-invalid');
                event.preventDefault(); // Detiene el envío
            }
        });
    }


    const nombreInput = document.getElementById('nombre');
    const apePInput = document.getElementById('apeP');
    const apeMInput = document.getElementById('apeM');

    function permitirSoloLetras(event) {
        event.target.value = event.target.value.replace(/[^a-zA-ZáéíóúÁÉÍÓÚñÑ\s]/g, '');
    }

    if (nombreInput) nombreInput.addEventListener('input', permitirSoloLetras);
    if (apePInput) apePInput.addEventListener('input', permitirSoloLetras);
    if (apeMInput) apeMInput.addEventListener('input', permitirSoloLetras);

    const alertasContainer = document.getElementById('alertasContainerPerfil');
    const serverErrorInput = document.getElementById('serverErrorPerfil');
    const serverSuccessInput = document.getElementById('serverSuccessPerfil');

    if (serverSuccessInput && serverSuccessInput.value === 'true') {
        Swal.fire({
            title: '¡Actualización Exitosa!',
            text: 'Tu contraseña ha sido actualizada. Por seguridad, tendrás que iniciar sesión nuevamente.',
            icon: 'success',
            confirmButtonColor: '#0d8a5f',
            confirmButtonText: 'Aceptar',
            allowOutsideClick: false
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = 'logout';
            }
        });
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