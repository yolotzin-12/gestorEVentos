document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const success = urlParams.get('success');

    if (error === 'reservas_activas') {
        Swal.fire({
            icon: 'warning',
            title: 'Acción bloqueada',
            text: 'No se puede cambiar el rol. Este usuario ya tiene reservas activas en el sistema.',
            confirmButtonColor: '#cc0000'
        });
        window.history.replaceState(null, null, window.location.pathname);

    } else if (success === 'rol_actualizado') {
        Swal.fire({
            icon: 'success',
            title: 'Rol actualizado',
            text: 'Los privilegios del usuario se han modificado correctamente.',
            confirmButtonColor: '#0d8a5f'
        });
        window.history.replaceState(null, null, window.location.pathname);

    } else if (success === 'estado_actualizado') {
        Swal.fire({
            icon: 'success',
            title: 'Estado actualizado',
            text: 'El estado del usuario se ha modificado correctamente.',
            confirmButtonColor: '#0d8a5f'
        });
        window.history.replaceState(null, null, window.location.pathname);
    }
});

document.getElementById('inputBusqueda')?.addEventListener('keyup', function() {
    let filtro = this.value.toLowerCase();
    let filas = document.querySelectorAll('#tablaUsuarios tbody tr.fila-usuario');

    filas.forEach(fila => {
        let nombre = fila.querySelector('.item-nombre').textContent.toLowerCase();
        let correo = fila.querySelector('.item-correo').textContent.toLowerCase();

        if (nombre.includes(filtro) || correo.includes(filtro)) {
            fila.style.display = '';
        } else {
            fila.style.display = 'none';
        }
    });
});