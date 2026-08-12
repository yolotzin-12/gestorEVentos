let tiempoInactividad;

function reiniciarTemporizador() {
    clearTimeout(tiempoInactividad);

    tiempoInactividad = setTimeout(mostrarAlertaExpiracion, 30 * 60 * 1000);
}

function mostrarAlertaExpiracion() {
    Swal.fire({
        icon: 'warning',
        title: 'Sesión expirada',
        text: 'Tu sesión se ha cerrado por inactividad.',
        confirmButtonColor: '#162e54',
        confirmButtonText: 'Ok',
        allowOutsideClick: false,
        allowEscapeKey: false
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = 'logout';
        }
    });
}

window.onload = reiniciarTemporizador;
document.onmousemove = reiniciarTemporizador;
document.onkeypress = reiniciarTemporizador;
document.onclick = reiniciarTemporizador;
document.onscroll = reiniciarTemporizador;

window.addEventListener('pageshow', function (event) {
    var historyTraversal = event.persisted ||
        (typeof window.performance != 'undefined' &&
            window.performance.getEntriesByType("navigation")[0].type === 'back_forward');

    if (historyTraversal) {
        window.location.reload();
    }
});

function confirmarCierreSesion(event) {
    event.preventDefault();

    Swal.fire({
        title: '¿Cerrar sesión?',
        text: '¿Estás seguro de que deseas salir del sistema?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#162e54',
        confirmButtonText: 'Sí, salir',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = 'logout';
        }
    });
}