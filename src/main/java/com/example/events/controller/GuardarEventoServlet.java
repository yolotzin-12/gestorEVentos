package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.events.model.models.Evento;
import com.example.events.model.dao.EventoDao;

import java.io.IOException;

@WebServlet(name = "GuardarEventoServlet", value = "/guardarEvento")
public class GuardarEventoServlet extends HttpServlet {

    private final EventoDao dao = new EventoDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String accion = request.getParameter("accion");
        String idStr = request.getParameter("id");
        String categoria = request.getParameter("categoria");
        String nombre = request.getParameter("nombre") != null ? request.getParameter("nombre") : request.getParameter("nomEvent");
        String fechaHora = request.getParameter("fechaHora") != null ? request.getParameter("fechaHora") : request.getParameter("fecha");
        String capacidadStr = request.getParameter("capacidadMaxima") != null ? request.getParameter("capacidadMaxima") : request.getParameter("capacidad");
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");

        HttpSession session = request.getSession(false);
        int idOrganizador = 1;

        if (session != null && session.getAttribute("idUsuario") != null) {
            idOrganizador = (int) session.getAttribute("idUsuario");
        }

        Evento evento = new Evento();
        evento.setCategoria(categoria);
        evento.setNombre(nombre);
        evento.setFechaHora(fechaHora);
        evento.setCapacidadMaxima(capacidadStr != null && !capacidadStr.isBlank() ? Integer.parseInt(capacidadStr) : 0);
        evento.setDescripcion(descripcion);
        evento.setEstado(estado != null ? estado : "Disponible");
        evento.setIdOrganizador(idOrganizador);
        evento.setIdEspacio(1);

        boolean guardado = false;

        if ("actualizar".equalsIgnoreCase(accion) && idStr != null && !idStr.isBlank()) {
            evento.setId(Integer.parseInt(idStr));
            guardado = dao.update(evento);
        } else {
            guardado = dao.create(evento);
        }

        if (guardado) {
            response.sendRedirect(request.getContextPath() + "/evento");
        } else {
            request.setAttribute("error", "Ocurrió un error al procesar el evento.");
            String destino = "actualizar".equalsIgnoreCase(accion) ? "editarEvent.jsp" : "crearEvent.jsp";
            request.getRequestDispatcher(destino).forward(request, response);
        }
    }
}