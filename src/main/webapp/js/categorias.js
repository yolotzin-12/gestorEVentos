document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    const error = urlParams.get('error');

    if (success === 'publicar') {
        Swal.fire({
            title: '¡Evento Creado!',
            text: 'Tu evento se ha publicado con éxito.',
            icon: 'success',
            confirmButtonColor: '#0d8a5f'
        });
        cleanUrl();
    } else if (success === 'borrador') {
        Swal.fire({
            title: 'Borrador Guardado',
            text: 'El evento se guardó correctamente y no es visible al público.',
            icon: 'info',
            confirmButtonColor: '#162e54'
        });
        cleanUrl();
    } else if (success === 'deleted') {
        Swal.fire({
            title: 'Eliminado',
            text: 'El evento ha sido eliminado correctamente.',
            icon: 'success',
            confirmButtonColor: '#0d8a5f'
        });
        cleanUrl();
    } else if (success === 'edited') {
        Swal.fire({
            title: '¡Actualizado!',
            text: 'El evento se ha editado y actualizado con éxito.',
            icon: 'success',
            confirmButtonColor: '#0d8a5f'
        });
        cleanUrl();
    } else if (error) {
        let msg = 'Ocurrió un error al procesar tu solicitud.';
        if (error === 'create_failed') msg = 'No se pudo guardar el evento.';
        if (error === 'update_failed') msg = 'No se pudo actualizar el evento.';
        if (error === 'delete_failed') msg = 'No se pudo eliminar el evento (quizá tenga reservas asociadas).';
        if (error === 'invalid_data') msg = 'Los datos enviados son inválidos. Revisa el formulario.';

        Swal.fire({
            title: 'Error',
            text: msg,
            icon: 'error',
            confirmButtonColor: '#cc0000'
        });
        cleanUrl();
    }

    function cleanUrl() {
        if (window.history.replaceState) {
            const clean = window.location.protocol + "//" + window.location.host + window.location.pathname;
            window.history.replaceState(null, null, clean);
        }
    }

    const formEvento = document.querySelector('form[action="evento"]');
    if (formEvento) {
        formEvento.addEventListener('submit', function(e) {
            const fechaInput = document.getElementById('fecha');
            const capacidadInput = document.getElementById('capacidad');

            if (fechaInput && fechaInput.value) {
                const fechaSeleccionada = new Date(fechaInput.value);
                const fechaActual = new Date();

                if (fechaSeleccionada < fechaActual) {
                    e.preventDefault();
                    Swal.fire({
                        title: 'Fecha inválida',
                        text: 'La fecha y hora del evento debe ser posterior al momento actual.',
                        icon: 'warning',
                        confirmButtonColor: '#162e54'
                    });
                    return;
                }
            }

            if (capacidadInput && parseInt(capacidadInput.value) <= 0) {
                e.preventDefault();
                Swal.fire({
                    title: 'Capacidad no válida',
                    text: 'El evento debe aceptar al menos a 1 persona.',
                    icon: 'warning',
                    confirmButtonColor: '#162e54'
                });
                return;
            }
        });
    }
});

function confirmarEliminarEvento(idEvento, nombreEvento) {
    Swal.fire({
        title: '¿Eliminar Evento?',
        html: `¿Estás seguro de que deseas eliminar el evento <b>"${nombreEvento}"</b>?<br>Esta acción no se puede deshacer.`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#cc0000',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Creamos un formulario dinámico para enviar la petición POST que el Servlet espera
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'evento';

            const inputAction = document.createElement('input');
            inputAction.type = 'hidden';
            inputAction.name = 'action';
            inputAction.value = 'delete';
            form.appendChild(inputAction);

            const inputId = document.createElement('input');
            inputId.type = 'hidden';
            inputId.name = 'id';
            inputId.value = idEvento;
            form.appendChild(inputId);

            document.body.appendChild(form);
            form.submit();
        }
    });
}

function guardarCategoria() {
    const nombreInput = document.getElementById('nombreCategoria').value;
    const mensajeDiv = document.getElementById('mensajeCategoria');

    if (!nombreInput.trim()) {
        mensajeDiv.innerHTML = '<span class="text-danger">El nombre no puede estar vacío.</span>';
        return;
    }

    const data = { nombre: nombreInput };

    fetch('api/categoria', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.status === 'success') {
                Swal.fire({
                    title: '¡Categoría Guardada!',
                    text: result.message,
                    icon: 'success',
                    confirmButtonColor: '#0d8a5f'
                }).then(() => {
                    window.location.href = 'evento?action=crear';
                });
            } else {
                mensajeDiv.innerHTML = `<span class="text-danger"><i class="bi bi-exclamation-triangle"></i> ${result.message}</span>`;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            mensajeDiv.innerHTML = '<span class="text-danger">Ocurrió un error.</span>';
        });
}

function guardarEspacio() {
    const nombre = document.getElementById("nombreEspacio").value;
    const ubicacion = document.getElementById("ubicacionEspacio").value;

    if (!nombre.trim()) {
        Swal.fire({
            title: 'Campo Requerido',
            text: 'El nombre del espacio es obligatorio.',
            icon: 'warning',
            confirmButtonColor: '#162e54'
        });
        return;
    }

    fetch('api/espacio', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombreEspacio: nombre,
            ubicacion: ubicacion
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.status === 'success') {
                Swal.fire({
                    title: '¡Espacio Agregado!',
                    text: data.message,
                    icon: 'success',
                    confirmButtonColor: '#0d8a5f'
                }).then(() => {
                    window.location.href = 'evento?action=crear';
                });
            } else {
                Swal.fire({
                    title: 'Error',
                    text: data.message,
                    icon: 'error',
                    confirmButtonColor: '#cc0000'
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({
                title: 'Error',
                text: 'Ocurrió un error al intentar guardar el espacio.',
                icon: 'error',
                confirmButtonColor: '#cc0000'
            });
        });
}

function previsualizarImagen(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(e) {
            document.getElementById('preview').src = e.target.result;
            document.getElementById('preview').classList.remove('d-none');
            document.getElementById('btnCambiar').classList.remove('d-none');
            document.getElementById('cajaBoton').classList.add('d-none');
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function borrarCategoria() {
    const select = document.getElementById('selectEliminarCat');
    const mensajeDiv = document.getElementById('mensajeEliminarCat');
    const id = select.value;

    if (!id) {
        mensajeDiv.innerHTML = '<span class="text-danger">Por favor, selecciona una categoría.</span>';
        return;
    }

    Swal.fire({
        title: '¿Eliminar categoría?',
        text: '¿Estás seguro de que deseas eliminar esta categoría?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#cc0000',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('api/categoria?id=' + id, {
                method: 'DELETE'
            })
                .then(response => response.json())
                .then(result => {
                    if (result.status === 'success') {
                        Swal.fire({
                            title: 'Eliminado',
                            text: result.message,
                            icon: 'success',
                            confirmButtonColor: '#0d8a5f'
                        }).then(() => {
                            window.location.reload();
                        });
                    } else {
                        mensajeDiv.innerHTML = `<span class="text-danger"><i class="bi bi-exclamation-triangle"></i> ${result.message}</span>`;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    mensajeDiv.innerHTML = '<span class="text-danger">Ocurrió un error de red.</span>';
                });
        }
    });
}

function borrarEspacio() {
    const select = document.getElementById('selectEliminarEsp');
    const mensajeDiv = document.getElementById('mensajeEliminarEsp');
    const id = select.value;

    if (!id) {
        mensajeDiv.innerHTML = '<span class="text-danger">Por favor, selecciona un espacio.</span>';
        return;
    }

    Swal.fire({
        title: '¿Eliminar espacio?',
        text: '¿Estás seguro de que deseas eliminar este espacio?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#cc0000',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch('api/espacio?id=' + id, {
                method: 'DELETE'
            })
                .then(response => response.json())
                .then(result => {
                    if (result.status === 'success') {
                        Swal.fire({
                            title: 'Eliminado',
                            text: result.message,
                            icon: 'success',
                            confirmButtonColor: '#0d8a5f'
                        }).then(() => {
                            window.location.reload();
                        });
                    } else {
                        mensajeDiv.innerHTML = `<span class="text-danger"><i class="bi bi-exclamation-triangle"></i> ${result.message}</span>`;
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    mensajeDiv.innerHTML = '<span class="text-danger">Ocurrió un error de red.</span>';
                });
        }
    });
}

