package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import com.example.events.model.Usuario;
import com.example.events.model.models.Evento;
import com.example.events.model.dao.EventoDao;
import com.example.events.model.dao.OrganizadorDao;
import com.example.events.model.dao.CategoriaDao;
import com.example.events.model.dao.EspacioDao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "EventoServlet", value = "/evento")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,       // 1 MB
        maxFileSize = 1024 * 1024 * 10,        // 10 MB
        maxRequestSize = 1024 * 1024 * 15      // 15 MB
)
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

        // Formulario para crear un evento nuevo (organizador/admin)
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

        // Página principal según el rol de la sesión
        List<Evento> lista;
        String vista;

        if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
            lista = eventoDao.getAll();
            vista = "dashboard-admin.jsp";
        } else if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
            int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
            lista = eventoDao.getByOrganizador(idOrg);
            vista = "dashboard-organizador.jsp";
        } else {
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

                // Imagen del evento (opcional)
                Part filePart = request.getPart("img");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String uploadPath = getServletContext().getRealPath("")
                            + File.separator + "img" + File.separator + "eventos";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    filePart.write(uploadPath + File.separator + fileName);
                    ev.setImagenUrl("img/eventos/" + fileName);
                }

                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                ev.setIdOrganizador(idOrg);

                eventoDao.create(ev);
            } catch (NumberFormatException e) {
                System.err.println("Error en datos del evento: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/evento");
    }
}