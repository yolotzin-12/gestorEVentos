package com.example.events.controller;

import com.example.events.model.dao.CategoriaDao;
import com.example.events.model.dao.EspacioDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
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
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 15 )   // 15 MB

public class EventoServlet extends HttpServlet {

    private final EventoDao eventoDao = new EventoDao();
    private final OrganizadorDao orgDao = new OrganizadorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Usuario usuarioSesion = (Usuario) request.getSession(false).getAttribute("usuario");

        // 1. Mostrar pantalla para CREAR EVENTO
        if ("crear".equals(action)) {
            EspacioDao espDao = new EspacioDao();
            request.setAttribute("listaEspacios", espDao.getAllEspacios());

            CategoriaDao catDao = new CategoriaDao();
            request.setAttribute("listaCategorias", catDao.getCategoriasActivas());

            OrganizadorDao orgDao = new OrganizadorDao();
            request.setAttribute("listaOrganizadores", orgDao.getAllOrganizadores());

            if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
                request.getRequestDispatcher("crearEventAdmin.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("crearEvent.jsp").forward(request, response);
            }
        }
        // 2. MOSTRAR DETALLE DEL EVENTO (Al hacer clic en una tarjeta)
        else if ("detalle".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int idEvento = Integer.parseInt(idParam);
                Evento evento = eventoDao.getById(idEvento); // Método que busca por id en tu EventoDao
                request.setAttribute("evento", evento);
                request.getRequestDispatcher("detalle-evento.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/evento");
            }
        }
        // 3. LISTADO GENERAL O DE ORGANIZADOR
        else {
            List<Evento> lista;

            if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                lista = eventoDao.getByOrganizador(idOrg);
            } else {
                lista = eventoDao.getAll();
            }

            request.setAttribute("listaEventos", lista);
            request.getRequestDispatcher("eventos.jsp").forward(request, response);
        }
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

                String strCat = request.getParameter("idCategoria");
                if (strCat != null && !strCat.isEmpty()) {
                    ev.setIdCategoria(Integer.parseInt(strCat));
                }

                ev.setCapacidadMaxima(Integer.parseInt(request.getParameter("capacidad")));

                // Formateo de fecha y hora proveniente de datetime-local (YYYY-MM-DDTHH:MM -> YYYY-MM-DD HH:MM:00)
                String fechaRaw = request.getParameter("fecha");
                if (fechaRaw != null && fechaRaw.contains("T")) {
                    fechaRaw = fechaRaw.replace("T", " ") + ":00";
                }
                ev.setFechaHora(fechaRaw);

                ev.setEstado("publicar".equals(action) ? "Disponible" : "Borrador");
                ev.setIdEspacio(Integer.parseInt(request.getParameter("idEspacio")));

                // Procesamiento de la imagen
                jakarta.servlet.http.Part filePart = request.getPart("img");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                    String uploadPath = getServletContext().getRealPath("") + java.io.File.separator + "img" + java.io.File.separator + "eventos";
                    java.io.File uploadDir = new java.io.File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdir();

                    String filePath = uploadPath + java.io.File.separator + fileName;
                    filePart.write(filePath);

                    ev.setImagenUrl("img/eventos/" + fileName);
                }

                if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
                    ev.setIdOrganizador(Integer.parseInt(request.getParameter("idOrganizador")));
                } else if (usuarioSesion != null) {
                    int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                    ev.setIdOrganizador(idOrg);
                }

                eventoDao.create(ev);
            } catch (NumberFormatException e) {
                System.err.println("Error procesando datos del evento: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/evento");
    }
}