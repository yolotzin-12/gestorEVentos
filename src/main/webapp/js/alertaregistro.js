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