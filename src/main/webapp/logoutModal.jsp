<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="modal fade" id="modalConfirmarLogout" tabindex="-1" aria-labelledby="modalConfirmarLogoutLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content border-0 shadow-lg rounded-4">

      <div class="modal-body p-5 text-center">

        <!-- Icono de advertencia -->
        <div class="d-flex justify-content-center align-items-center mx-auto mb-4"
             style="width: 85px; height: 85px; border-radius: 50%; border: 4px solid #f8bb86;">
          <i class="bi bi-exclamation-lg" style="color: #f8bb86; font-size: 3.5rem;"></i>
        </div>

        <!-- Título -->
        <h3 class="fw-bold mb-3" style="color: #545454;" id="modalConfirmarLogoutLabel">
          ¿Estás seguro?
        </h3>

        <!-- Mensaje -->
        <p class="text-muted mb-5 fs-5">
          ¿Estás seguro de que quieres cerrar sesión?
        </p>

        <!-- Botones -->
        <div class="d-flex justify-content-center gap-3">
          <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger px-4 py-2 fw-semibold rounded-3 shadow-sm">
            Sí, cerrar sesión
          </a>
          <button type="button" class="btn btn-secondary px-4 py-2 fw-semibold rounded-3 shadow-sm" data-bs-dismiss="modal">
            Cancelar
          </button>
        </div>

      </div>
    </div>
  </div>
</div>