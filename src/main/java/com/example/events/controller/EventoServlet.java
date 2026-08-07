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

        // 1. Si el usuario quiere ver la pantalla de CREAR EVENTO
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
        else {
            List<Evento> lista;
            if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                lista = eventoDao.getByOrganizador(idOrg);
            } else {
                lista = eventoDao.getAll();
            }

            request.setAttribute("listaEventos", lista);
            request.getRequestDispatcher("gestion-eventos.jsp").forward(request, response);
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
                ev.setIdCategoria(Integer.parseInt(request.getParameter("categoria")));
                ev.setCapacidadMaxima(Integer.parseInt(request.getParameter("capacidad")));
                ev.setFechaHora(request.getParameter("fecha"));
                ev.setEstado("publicar".equals(action) ? "Disponible" : "Borrador");
                ev.setIdEspacio(Integer.parseInt(request.getParameter("idEspacio")));


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

                // --- NUEVA LÓGICA DE ASIGNACIÓN DE ORGANIZADOR ---
                if (usuarioSesion.getIdRol() == 1) {
                    // Si es Administrador: toma el ID del organizador seleccionado en el formulario
                    ev.setIdOrganizador(Integer.parseInt(request.getParameter("idOrganizador")));
                } else {
                    // Si es Organizador: busca su propio ID a partir de su sesión
                    int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                    ev.setIdOrganizador(idOrg);
                }
                // -------------------------------------------------

                eventoDao.create(ev);
            } catch (NumberFormatException e) {
                System.err.println("Error en datos del evento: " + e.getMessage());
            }
        }

        response.sendRedirect(request.getContextPath() + "/evento");
        }
    }