document.addEventListener("DOMContentLoaded", function() {
    // 1. Obtenemos la ruta principal y el parámetro 'action' exacto de la URL actual
    const currentPath = window.location.pathname;
    const urlParams = new URLSearchParams(window.location.search);
    const currentAction = urlParams.get('action'); // Puede ser null, 'gestion', 'crear', etc.

    // 2. Seleccionamos todos los enlaces de la navbar
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');

    navLinks.forEach(link => {
        link.classList.remove('active');

        const linkHref = link.getAttribute('href') || "";

        if (currentPath.includes('/usuarios') && linkHref.includes('/usuarios')) {
            link.classList.add('active');
        }
        else if (currentPath.includes('/reserva') && linkHref.includes('/reserva')) {
            link.classList.add('active');
        }
        else if (currentPath.includes('/evento')) {

            if (currentAction === 'gestion' && linkHref.includes('action=gestion')) {
                link.classList.add('active');
            }
            else if (currentAction === 'crear' && linkHref.includes('action=crear')) {
                link.classList.add('active');
            }
            else if (!currentAction && !linkHref.includes('action=')) {
                link.classList.add('active');
            }
        }
    });
});