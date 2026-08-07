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
import com.example.events.model.dao.CategoriaDao;
import com.example.events.model.dao.EspacioDao;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EventoServlet", value = "/evento")
public class EventoServlet extends HttpServlet {

    private final EventoDao eventoDao = new EventoDao();
    private final OrganizadorDao orgDao = new OrganizadorDao();
    private final CategoriaDao categoriaDao = new CategoriaDao();
    private final EspacioDao espacioDao = new EspacioDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuarioSesion = (Usuario) request.getSession(false).getAttribute("usuario");
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        // Formulario para crear un evento nuevo (solo organizador)
        if ("nuevo".equals(action)) {
            request.setAttribute("listaCategorias", categoriaDao.getAll());
            request.setAttribute("listaEspacios", espacioDao.getAll());
            request.getRequestDispatcher("crearEvent.jsp").forward(request, response);
            return;
        }

        // Detalle de un evento específico
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Evento evento = eventoDao.getById(id);
                request.setAttribute("evento", evento);
                request.getRequestDispatcher("detalle-evento.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/evento");
            }
            return;
        }

        // Página principal según el rol de la sesión: cada rol tiene su propia vista
        List<Evento> lista;
        String vista;

        if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
            // Administrador: ve todos los eventos disponibles
            lista = eventoDao.getAll();
            vista = "dashboard-admin.jsp";
        } else if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
            // Organizador: ve únicamente sus propios eventos
            int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
            lista = eventoDao.getByOrganizador(idOrg);
            vista = "dashboard-organizador.jsp";
        } else {
            // Asistente: ve todos los eventos disponibles
            lista = eventoDao.getAll();
            vista = "index.jsp";
        }

        request.setAttribute("listaEventos", lista);
        request.getRequestDispatcher(vista).forward(request, response);
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
                ev.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                ev.setIdEspacio(Integer.parseInt(request.getParameter("idEspacio")));
                ev.setEstado("publicar".equals(action) ? "Disponible" : "Borrador");

                // Obtener idOrganizador desde la sesión
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                ev.setIdOrganizador(idOrg);

                eventoDao.create(ev);
            } catch (NumberFormatException e) {
                System.err.println("Error en datos del evento: " + e.getMessage());
            }
        }

        // Siempre regresa a la página principal (según el rol) para reflejar el cambio
        response.sendRedirect(request.getContextPath() + "/evento");
    }
}
