package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.Usuario;
import com.example.events.model.models.Evento;
import com.example.events.model.dao.EventoDao;
import com.example.events.model.dao.OrganizadorDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EventoServlet", value = "/evento")
public class EventoServlet extends HttpServlet {

    private final EventoDao eventoDao = new EventoDao();
    private final OrganizadorDao orgDao = new OrganizadorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioSesion = (Usuario) request.getSession(false).getAttribute("usuario");
        List<Evento> lista;

        // Si es organizador, muestra solo sus eventos; si no, muestra todos los disponibles
        if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
            int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
            lista = eventoDao.getByOrganizador(idOrg);
        } else {
            lista = eventoDao.getAll();
        }

        request.setAttribute("listaEventos", lista);
        request.getRequestDispatcher("gestion-eventos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        Usuario usuarioSesion = (Usuario) request.getSession(false).getAttribute("usuario");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            eventoDao.delete(id);

        } else if ("publicar".equals(action) || "borrador".equals(action)) {
            try {
                Evento ev = new Evento();
                ev.setNombre(request.getParameter("nombre"));
                ev.setDescripcion(request.getParameter("descripcion"));
                ev.setCapacidadMaxima(Integer.parseInt(request.getParameter("capacidad")));
                ev.setFechaHora(request.getParameter("fecha"));
                ev.setEstado("publicar".equals(action) ? "Disponible" : "Borrador");

                // Obtener idOrganizador desde la sesión
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                ev.setIdOrganizador(idOrg);
                ev.setIdEspacio(1); // ajusta cuando tengas el selector de espacio

                eventoDao.create(ev);
            } catch (NumberFormatException e) {
                System.err.println("Error en datos del evento: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/evento");
    }
}