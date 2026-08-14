if (window.history.replaceState) {
    window.history.replaceState(null, null, window.location.href);
}

window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        const formularios = document.querySelectorAll('form');
        formularios.forEach(form => form.reset());
    }
});

function togglePassword(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('bi-eye-fill');
        icon.classList.add('bi-eye-slash-fill');
    } else {
        input.type = 'password';
        icon.classList.remove('bi-eye-slash-fill');
        icon.classList.add('bi-eye-fill');
    }
}

document.addEventListener("DOMContentLoaded", function() {
    const formRegistro = document.getElementById("formRegistro");

    if (formRegistro) {
        formRegistro.addEventListener("submit", function(e) {
            const regexNombre = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/;
            const regexEmail = /^[a-zA-Z0-9._%+-]+@(gmail\.com|hotmail\.com|yahoo\.com|outlook\.com|utez\.edu\.mx)$/i;

            const nombre = document.getElementById("txtNombre").value.trim();
            const apePat = document.getElementById("txtApellidoPaterno").value.trim();
            const apeMat = document.getElementById("txtApellidoMaterno").value.trim();
            const email = document.getElementById("txtCorreo").value.trim();

            if (!regexNombre.test(nombre) || !regexNombre.test(apePat) || (apeMat !== "" && !regexNombre.test(apeMat))) {
                e.preventDefault();
                Swal.fire({
                    icon: 'warning',
                    title: 'Formato incorrecto',
                    text: 'Los nombres y apellidos no deben contener números ni caracteres especiales.',
                    confirmButtonColor: '#162e54'
                });
                return;
            }

            if (!regexEmail.test(email)) {
                e.preventDefault();
                Swal.fire({
                    icon: 'warning',
                    title: 'Correo no admitido',
                    text: 'Solo se permiten correos @utez.edu.mx, @gmail.com, @hotmail.com, @yahoo.com o @outlook.com',
                    confirmButtonColor: '#162e54'
                });
                return;
            }
        });
    }
});

document.addEventListener("DOMContentLoaded", function() {
    let errorMsg = document.getElementById("serverError").value.trim();
    let successMsg = document.getElementById("serverMensaje").value.trim();

    if (errorMsg !== "" && errorMsg !== "null") {
        Swal.fire({
            icon: 'error',
            title: 'No se pudo ingresar',
            text: errorMsg,
            confirmButtonColor: '#1b365d'
        });
    }

    if (successMsg !== "" && successMsg !== "null") {
        Swal.fire({
            icon: 'success',
            title: '¡Éxito!',
            text: successMsg,
            confirmButtonColor: '#1b365d'
        });
    }
});