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
                alert(result.message);
                window.location.href = 'evento?action=crear';
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
        alert("El nombre del espacio es obligatorio.");
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
                alert(data.message);
                window.location.href = 'evento?action=crear';
            } else {
                alert("Error: " + data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}

function previsualizarImagen(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(e) {
            // Asigna la imagen cargada
            document.getElementById('preview').src = e.target.result;

            // Muestra la imagen y el botón de cambiar, oculta el botón principal
            document.getElementById('preview').classList.remove('d-none');
            document.getElementById('btnCambiar').classList.remove('d-none');
            document.getElementById('cajaBoton').classList.add('d-none');
        }

        reader.readAsDataURL(input.files[0]);
    }
}