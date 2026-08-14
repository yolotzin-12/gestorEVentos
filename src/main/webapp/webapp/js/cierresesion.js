let tiempoInactividad;

function reiniciarTemporizador() {
    clearTimeout(tiempoInactividad);
    tiempoInactividad = setTimeout(mostrarAlertaExpiracion, 1 * 60 * 1000);
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

function esTextoInvalido(texto) {
    const muchasConsonantes = /[bcdfghjklmnpqrstvwxyz]{5,}/i;
    const caracterRepetido = /(.)\1{4,}/i;
    const tecleoAlAzar = /asdf|qwer|zxcv|hjkl|uiop|nm,\./i;
    const sinVocales = /\b[^aeiouáéíóúü\s]{6,}\b/i;

    return muchasConsonantes.test(texto) ||
        caracterRepetido.test(texto) ||
        tecleoAlAzar.test(texto) ||
        sinVocales.test(texto);
}

document.addEventListener('change', function(e) {
    if (e.target.matches('input[type="text"], textarea')) {
        const val = e.target.value.toLowerCase();

        const malasPalabras = [
            "puto", "puta", "pendejo", "pendeja", "mierda", "cabron", "cabrón", "cabrona",
            "chinga", "chingar", "chingada", "chingon", "chingón", "pinche", "idiota",
            "estupido", "estúpido", "estupida", "estúpida", "verga", "vergas", "vergazo",
            "culero", "culera", "joto", "maricon", "maricón", "zorra", "perra",
            "mamada", "mamon", "mamón", "mamona", "pito", "panocha", "putamadre",
            "imbecil", "imbécil", "joder", "gilipollas", "concha", "huevon", "huevón"
        ];

        if (malasPalabras.some(palabra => val.includes(palabra))) {
            e.target.value = "";
            Swal.fire({
                title: 'Lenguaje inapropiado',
                text: 'Se detectó lenguaje inapropiado. Por favor, utiliza un lenguaje adecuado.',
                icon: 'warning',
                confirmButtonColor: '#162e54'
            });
        } else if (val.trim() !== "" && esTextoInvalido(val)) {
            e.target.value = "";
            Swal.fire({
                title: 'Texto inválido',
                text: 'El texto ingresado parece no tener sentido o contiene demasiados caracteres repetidos.',
                icon: 'warning',
                confirmButtonColor: '#162e54'
            });
        }
    }
});

document.addEventListener('submit', function(e) {
    const textElements = e.target.querySelectorAll('input[type="text"]:not([type="email"]):not([type="password"]), textarea');
    let textoTotal = "";
    let textoInvalido = false;

    textElements.forEach(el => {
        if (el.value) {
            textoTotal += el.value.toLowerCase() + " ";
            if (esTextoInvalido(el.value)) {
                textoInvalido = true;
            }
        }
    });

    if (textoTotal.trim() !== "") {
        const malasPalabras = [
            "puto", "puta", "pendejo", "pendeja", "mierda", "cabron", "cabrón", "cabrona",
            "chinga", "chingar", "chingada", "chingon", "chingón", "pinche", "idiota",
            "estupido", "estúpido", "estupida", "estúpida", "verga", "vergas", "vergazo",
            "culero", "culera", "joto", "maricon", "maricón", "zorra", "perra",
            "mamada", "mamon", "mamón", "mamona", "pito", "panocha", "putamadre",
            "imbecil", "imbécil", "joder", "gilipollas", "concha", "huevon", "huevón"
        ];

        const contieneMalasPalabras = malasPalabras.some(palabra => {
            const regex = new RegExp(`\\b${palabra}\\b`, 'i');
            return regex.test(textoTotal);
        });

        if (contieneMalasPalabras) {
            e.preventDefault();
            Swal.fire({
                title: 'Lenguaje inapropiado',
                text: 'Se detectó lenguaje inapropiado en el formulario. Revisa los campos.',
                icon: 'warning',
                confirmButtonColor: '#162e54'
            });
            return;
        }

        if (textoInvalido) {
            e.preventDefault();
            Swal.fire({
                title: 'Texto inválido',
                text: 'Uno o más campos parecen no tener sentido o contienen caracteres repetidos al azar.',
                icon: 'warning',
                confirmButtonColor: '#162e54'
            });
            return;
        }
    }
});