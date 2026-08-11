// Quita el chip de ubicación sin mover el resto de la barra de filtros
function quitarUbicacion() {
    var chip = document.getElementById('filtroUbicacion');
    if (chip) {
        chip.classList.add('oculto');
    }
}

// Filtro en vivo: oculta las tarjetas de evento que no coincidan con el texto escrito,
// comparando tanto el nombre del evento como su ubicación (coincidencia parcial).
document.addEventListener('DOMContentLoaded', function () {
    var input = document.querySelector('.buscador-evento input[name="buscar"]');
    var contenedor = document.getElementById('listaEventosGrid');

    if (!input || !contenedor) {
        return;
    }

    var tarjetas = contenedor.querySelectorAll('[data-nombre-evento]');

    input.addEventListener('input', function () {
        var texto = input.value.trim().toLowerCase();
        var visibles = 0;

        tarjetas.forEach(function (tarjeta) {
            var nombre = tarjeta.getAttribute('data-nombre-evento') || '';
            var ubicacion = tarjeta.getAttribute('data-ubicacion-evento') || '';
            var coincide = nombre.indexOf(texto) !== -1 || ubicacion.indexOf(texto) !== -1;
            tarjeta.style.display = coincide ? '' : 'none';
            if (coincide) visibles++;
        });

        var mensajeVacio = document.getElementById('sinResultadosBusqueda');
        if (mensajeVacio) {
            mensajeVacio.style.display = (visibles === 0 && tarjetas.length > 0) ? '' : 'none';
        }
    });
});