document.addEventListener('DOMContentLoaded', () => {
    const errorMsgInput = document.getElementById('serverErrorRegistro');

    if (errorMsgInput && errorMsgInput.value.trim() !== "") {
        const mensaje = errorMsgInput.value.trim();

        if (mensaje.toLowerCase().includes("registrado") || mensaje.toLowerCase().includes("existe")) {
            Swal.fire({
                title: '¡Este correo ya está registrado!',
                text: '¿Olvidaste tu contraseña?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#162e54',
                cancelButtonColor: '#6c757d',
                confirmButtonText: 'Recuperar contraseña',
                cancelButtonText: 'Cancelar',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = 'recuperarContra.jsp';
                }
            });
        } else {
            Swal.fire({
                title: 'Aviso',
                text: mensaje,
                icon: 'error',
                confirmButtonColor: '#058971'
            });
        }
    }
});

    var campoPassword = document.getElementById('txtPassword');
    var listaRequisitos = document.getElementById('listaRequisitos');
    var campoConfirmacionPassword = document.getElementById('txtPasswordConfirmacion');
    var mensajeCoincidenciaPassword = document.getElementById('mensajeCoincidenciaPassword');

    var campoCorreo = document.getElementById('txtCorreo');
    var campoConfirmacionCorreo = document.getElementById('txtCorreoConfirmacion');
    var mensajeCoincidenciaEmail = document.getElementById('mensajeCoincidenciaEmail');


    campoPassword.addEventListener('input', function () {
    var valor = this.value;

    if (valor.length === 0) {
    listaRequisitos.style.display = 'none';
} else {
    listaRequisitos.style.display = 'block';
}

    var reglas = {
    'req-longitud': valor.length >= 8,
    'req-mayuscula': /[A-Z]/.test(valor),
    'req-minuscula': /[a-z]/.test(valor),
    'req-numero': /[0-9]/.test(valor)
};

    for (var id in reglas) {
    var li = document.getElementById(id);
    var icono = li.querySelector('i');
    if (reglas[id]) {
    li.classList.add('cumplido');
    li.classList.remove('no-cumplido');
    icono.className = 'bi bi-check-circle-fill';
} else {
    li.classList.add('no-cumplido');
    li.classList.remove('cumplido');
    icono.className = 'bi bi-x-circle-fill';
}
}

    if (campoConfirmacionPassword.value.length > 0) {
    validarCoincidencia(campoPassword, campoConfirmacionPassword, mensajeCoincidenciaPassword);
}
});


    function validarCoincidencia(campoOriginal, campoConfirmacion, elementoMensaje) {
    if (campoConfirmacion.value.length === 0) {
    elementoMensaje.style.display = 'none';
    return;
}

    elementoMensaje.style.display = 'block';

    if (campoConfirmacion.value === campoOriginal.value) {
    elementoMensaje.textContent = '✓ Coinciden';
    elementoMensaje.className = 'mensaje-coincidencia ok';
} else {
    elementoMensaje.textContent = '✕ No coinciden';
    elementoMensaje.className = 'mensaje-coincidencia error';
}
}

    campoConfirmacionPassword.addEventListener('input', function () {
    validarCoincidencia(campoPassword, campoConfirmacionPassword, mensajeCoincidenciaPassword);
});

    campoCorreo.addEventListener('input', function () {
    if (campoConfirmacionCorreo.value.length > 0) {
    validarCoincidencia(campoCorreo, campoConfirmacionCorreo, mensajeCoincidenciaEmail);
}
});

    campoConfirmacionCorreo.addEventListener('input', function () {
    validarCoincidencia(campoCorreo, campoConfirmacionCorreo, mensajeCoincidenciaEmail);
});

    document.getElementById('formRegistro').addEventListener('submit', function (e) {
    if (campoCorreo.value !== campoConfirmacionCorreo.value) {
    e.preventDefault();
    validarCoincidencia(campoCorreo, campoConfirmacionCorreo, mensajeCoincidenciaEmail);
    campoConfirmacionCorreo.focus();
    return;
}
    if (campoPassword.value !== campoConfirmacionPassword.value) {
    e.preventDefault();
    validarCoincidencia(campoPassword, campoConfirmacionPassword, mensajeCoincidenciaPassword);
    campoConfirmacionPassword.focus();
}
});
