document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');
    const success = urlParams.get('success');

    if (error === 'reservas_activas') {
        Swal.fire({
            icon: 'warning',
            title: 'Acción bloqueada',
            text: 'No se puede modificar. Este usuario tiene reservas activas en el sistema.',
            confirmButtonColor: '#cc0000'
        });
        window.history.replaceState(null, null, window.location.pathname);

    } else if (error === 'eventos_activos') {
        Swal.fire({
            icon: 'warning',
            title: 'Acción bloqueada',
            text: 'No se puede modificar. Este usuario tiene eventos próximos o activos en el sistema.',
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

    const inputBusqueda = document.getElementById('inputBusqueda');
    const selectFiltroRol = document.getElementById('selectFiltroRol');
    const selectFiltroEstado = document.getElementById('selectFiltroEstado');

    function filtrarTabla() {
        let textoBusqueda = inputBusqueda ? inputBusqueda.value.toLowerCase() : '';
        let filtroRol = selectFiltroRol ? selectFiltroRol.value : 'todos';
        let filtroEstado = selectFiltroEstado ? selectFiltroEstado.value : 'todos';

        let filas = document.querySelectorAll('#tablaUsuarios tbody tr.fila-usuario');

        filas.forEach(fila => {
            let nombre = fila.querySelector('.item-nombre').textContent.toLowerCase();
            let correo = fila.querySelector('.item-correo').textContent.toLowerCase();

            let selectRolFila = fila.querySelector('.select-rol-admin');
            let rolUsuario = selectRolFila ? selectRolFila.value : '';

            let checkboxEstado = fila.querySelector('.check-estado-usuario');
            let estaActivo = checkboxEstado ? checkboxEstado.checked : false;

            let coincideTexto = nombre.includes(textoBusqueda) || correo.includes(textoBusqueda);
            let coincideRol = (filtroRol === 'todos') || (rolUsuario === filtroRol);
            let coincideEstado = (filtroEstado === 'todos') ||
                (filtroEstado === 'activo' && estaActivo) ||
                (filtroEstado === 'inactivo' && !estaActivo);

            if (coincideTexto && coincideRol && coincideEstado) {
                fila.style.display = '';
            } else {
                fila.style.display = 'none';
            }
        });
    }

    if (inputBusqueda) inputBusqueda.addEventListener('keyup', filtrarTabla);
    if (selectFiltroRol) selectFiltroRol.addEventListener('change', filtrarTabla);
    if (selectFiltroEstado) selectFiltroEstado.addEventListener('change', filtrarTabla);
});

function confirmarCambioEstado(checkbox, estabaActivo) {
    if (estabaActivo && !checkbox.checked) {
        Swal.fire({
            title: '¿Estás seguro?',
            text: '¿Estás seguro que quieres desactivar a este usuario?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#cc0000',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Aceptar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                checkbox.form.submit();
            } else {
                checkbox.checked = true;
            }
        });
    } else {
        checkbox.form.submit();
    }
}