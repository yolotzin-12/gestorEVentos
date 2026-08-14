document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formularioRecuperar');
    const contraNew = document.getElementById('contraNew');
    const confirmarContra = document.getElementById('confirmarContra');
    const listaRequisitos = document.getElementById('listaRequisitos');
    const errorCoincidencia = document.getElementById('errorCoincidencia');

    const toggleButtons = document.querySelectorAll('.toggle-password');
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Obtener el ID del input asociado al botón
            const targetId = this.getAttribute('data-target');
            const input = document.getElementById(targetId);
            const icon = this.querySelector('i');

            // Cambiar el tipo de input y el icono
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            } else {
                input.type = 'password';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            }
        });
    });

    function validarCoincidencia(input1, input2, msjError) {
        if (input1.value !== input2.value && input2.value.length > 0) {
            msjError.style.display = 'block';
            return false;
        } else {
            msjError.style.display = 'none';
            return true;
        }
    }

    if (form && contraNew && confirmarContra) {
        contraNew.addEventListener('input', function () {
            const valor = this.value;

            if (listaRequisitos) {
                if (valor.length === 0) {
                    listaRequisitos.style.display = 'none';
                } else {
                    listaRequisitos.style.display = 'block';
                }

                const reglas = {
                    'req-longitud': valor.length >= 8,
                    'req-mayuscula': /[A-Z]/.test(valor),
                    'req-minuscula': /[a-z]/.test(valor),
                    'req-numero': /[0-9]/.test(valor)
                };

                for (let id in reglas) {
                    const li = document.getElementById(id);
                    if (li) {
                        const icono = li.querySelector('i');
                        if (reglas[id]) {
                            li.classList.add('cumplido');
                            li.classList.remove('no-cumplido');
                            if (icono) icono.className = 'bi bi-check-circle-fill me-1';
                        } else {
                            li.classList.add('no-cumplido');
                            li.classList.remove('cumplido');
                            if (icono) icono.className = 'bi bi-x-circle-fill me-1';
                        }
                    }
                }
            }

            if (confirmarContra.value.length > 0) {
                validarCoincidencia(contraNew, confirmarContra, errorCoincidencia);
            }
        });

        confirmarContra.addEventListener('input', function () {
            validarCoincidencia(contraNew, confirmarContra, errorCoincidencia);
        });

        form.addEventListener('submit', function(event) {
            let isValid = true;
            const valor = contraNew.value;

            const cumpleReglas = valor.length >= 8 &&
                /[A-Z]/.test(valor) &&
                /[a-z]/.test(valor) &&
                /[0-9]/.test(valor);

            if (!cumpleReglas) {
                contraNew.classList.add('is-invalid');
                isValid = false;
            } else {
                contraNew.classList.remove('is-invalid');
            }

            if (contraNew.value !== confirmarContra.value) {
                validarCoincidencia(contraNew, confirmarContra, errorCoincidencia);
                confirmarContra.classList.add('is-invalid');
                confirmarContra.focus();
                isValid = false;
            } else {
                confirmarContra.classList.remove('is-invalid');
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});