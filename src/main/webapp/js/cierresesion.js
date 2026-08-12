let tiempoInactividad;

function reiniciarTemporizador() {
    clearTimeout(tiempoInactividad);

    // Configurar el temporizador para 30 minutos (1800000 milisegundos)
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