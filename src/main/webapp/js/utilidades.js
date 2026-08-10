
if (window.history.replaceState) {
    window.history.replaceState(null, null, window.location.href);
}

window.addEventListener('pageshow', function(event) {
    if (event.persisted) {
        // Busca todos los formularios de la página y los resetea
        const formularios = document.querySelectorAll('form');
        formularios.forEach(form => form.reset());
    }
});
function togglePassword(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');

    if (input.type === 'password') {
        input.type = 'text';
        icon.classList.remove('bi-eye-fill');
        icon.classList.add('bi-eye-slash-fill');
    } else {
        input.type = 'password';
        icon.classList.remove('bi-eye-slash-fill');
        icon.classList.add('bi-eye-fill');
    }
}