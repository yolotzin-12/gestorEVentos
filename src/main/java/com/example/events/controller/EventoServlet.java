package com.example.events.controller;

import com.example.events.model.dao.AsistenteDao;
import com.example.events.model.dao.CategoriaDao;
import com.example.events.model.dao.EspacioDao;
import com.example.events.model.dao.EventoDao;
import com.example.events.model.dao.OrganizadorDao;
import com.example.events.model.dao.ReservaDao;
import com.example.events.model.models.Evento;
import com.example.events.model.models.Reserva;
import com.example.events.model.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
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
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioSesion == null && session != null) {
            usuarioSesion = (Usuario) session.getAttribute("user");
        }

        if ("gestion".equals(action) || "misEventos".equals(action)) {
            List<Evento> lista;
            if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
                // Admin ve TODOS los eventos
                lista = eventoDao.getAllAdmin();
            } else if (usuarioSesion != null && usuarioSesion.getIdRol() == 2) {
                // Organizador ve SOLO sus eventos con métricas de reservas
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                lista = eventoDao.getByOrganizadorConReservas(idOrg);
            } else {
                lista = eventoDao.getAll();
            }

            request.setAttribute("listaEventos", lista);
            request.getRequestDispatcher("gestion-eventos.jsp").forward(request, response);
        }
        else if ("cancelar".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int idEvento = Integer.parseInt(idParam);

                if (!puedeGestionar(usuarioSesion, idEvento)) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=forbidden");
                    return;
                }

                eventoDao.cambiarEstado(idEvento, "Cancelado");
            }
            response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
        }
        else if ("limpiarHistorial".equals(action)) {
            if (usuarioSesion != null) {
                int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                eventoDao.limpiarHistorialOrganizador(idOrg);
            }
            response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
        }
        else if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int id = Integer.parseInt(idParam);

                if (!puedeGestionar(usuarioSesion, id)) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=forbidden");
                    return;
                }

                eventoDao.delete(id);
            }
            response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
        }
        else if ("crear".equals(action)) {
            EspacioDao espDao = new EspacioDao();
            request.setAttribute("listaEspacios", espDao.getAllEspacios());

            CategoriaDao catDao = new CategoriaDao();
            request.setAttribute("listaCategorias", catDao.getCategoriasActivas());

            OrganizadorDao oDao = new OrganizadorDao();
            request.setAttribute("listaOrganizadores", oDao.getAllOrganizadores());

            if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
                request.getRequestDispatcher("crearEventAdmin.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("crearEvent.jsp").forward(request, response);
            }
        }
        else if ("editar".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int idEvento = Integer.parseInt(idParam);

                if (!puedeGestionar(usuarioSesion, idEvento)) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=forbidden");
                    return;
                }

                Evento evento = eventoDao.getById(idEvento);
                request.setAttribute("evento", evento);

                EspacioDao espDao = new EspacioDao();
                request.setAttribute("listaEspacios", espDao.getAllEspacios());

                CategoriaDao catDao = new CategoriaDao();
                request.setAttribute("listaCategorias", catDao.getCategoriasActivas());

                request.getRequestDispatcher("EditarEvent.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
            }
        }
        else if ("detalle".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int idEvento = Integer.parseInt(idParam);
                Evento evento = eventoDao.getById(idEvento);
                request.setAttribute("evento", evento);

                String origen = request.getParameter("origen");
                request.setAttribute("origenNavegacion", origen != null ? origen : "principal");

                request.getRequestDispatcher("detalle-evento.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
            }
        }
        else if ("reservar".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                int idEvento = Integer.parseInt(idParam);
                Evento evento = eventoDao.getById(idEvento);

                if (evento == null) {
                    response.sendRedirect(request.getContextPath() + "/evento");
                    return;
                }

                request.setAttribute("evento", evento);

                // Si el usuario logueado ya tiene una reserva activa para este
                // evento, se lo indicamos a la vista para que muestre el aviso
                // y el botón de cancelar en vez del formulario de reserva.
                if (usuarioSesion != null) {
                    AsistenteDao asisDao = new AsistenteDao();
                    int idAsistente = asisDao.getIdAsistenteByUsuario(usuarioSesion.getId());
                    ReservaDao reservaDao = new ReservaDao();
                    Reserva reservaActiva = reservaDao.getReservaActivaDeUsuario(idEvento, idAsistente);
                    request.setAttribute("reservaActiva", reservaActiva);
                }

                request.getRequestDispatcher("reservar.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/evento");
            }
        }
        else {
            List<Evento> lista = eventoDao.getAll();
            request.setAttribute("listaEventos", lista);
            request.getRequestDispatcher("eventos.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        Usuario usuarioSesion = (session != null) ? (Usuario) session.getAttribute("usuario") : null;

        if (usuarioSesion == null && session != null) {
            usuarioSesion = (Usuario) session.getAttribute("user");
        }

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));

            if (!puedeGestionar(usuarioSesion, id)) {
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=forbidden");
                return;
            }

            boolean eliminado = eventoDao.delete(id);

            if (eliminado) {
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion&success=deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=delete_failed");
            }
            return;
        }
        else if ("actualizar".equals(action)) {
            try {
                int idEvento = Integer.parseInt(request.getParameter("id"));

                if (!puedeGestionar(usuarioSesion, idEvento)) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=forbidden");
                    return;
                }

                Evento ev = eventoDao.getById(idEvento);

                ev.setNombre(request.getParameter("nombre"));
                ev.setDescripcion(request.getParameter("descripcion"));
                ev.setIdCategoria(Integer.parseInt(request.getParameter("idCategoria")));
                ev.setCapacidadMaxima(Integer.parseInt(request.getParameter("capacidad")));
                ev.setIdEspacio(Integer.parseInt(request.getParameter("idEspacio")));
                ev.setEstado(request.getParameter("estado") != null ? request.getParameter("estado") : "Disponible");

                String fechaRaw = request.getParameter("fecha");
                if (fechaRaw != null && fechaRaw.contains("T")) {
                    fechaRaw = fechaRaw.replace("T", " ") + ":00";
                }
                ev.setFechaHora(fechaRaw);

                Part filePart = request.getPart("img");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + "img" + File.separator + "eventos";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String filePath = uploadPath + File.separator + fileName;
                    filePart.write(filePath);
                    ev.setImagenUrl("img/eventos/" + fileName);
                }

                boolean actualizado = eventoDao.update(ev);

                if (actualizado) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&success=edited");
                } else {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=update_failed");
                }
                return;
            } catch (Exception e) {
                System.err.println("Error editando evento: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=invalid_data");
                return;
            }
        }
        else if ("publicar".equalsIgnoreCase(action) || "borrador".equalsIgnoreCase(action)) {
            try {
                Evento ev = new Evento();
                ev.setNombre(request.getParameter("nombre"));
                ev.setDescripcion(request.getParameter("descripcion"));

                String strCat = request.getParameter("idCategoria");
                if (strCat != null && !strCat.isEmpty()) {
                    ev.setIdCategoria(Integer.parseInt(strCat));
                }

                ev.setCapacidadMaxima(Integer.parseInt(request.getParameter("capacidad")));

                String fechaRaw = request.getParameter("fecha");
                if (fechaRaw != null && fechaRaw.contains("T")) {
                    fechaRaw = fechaRaw.replace("T", " ") + ":00";
                }
                ev.setFechaHora(fechaRaw);

                // Asignación de estado: Si la acción es borrador -> "Borrador", de lo contrario -> "Disponible"
                if ("borrador".equalsIgnoreCase(action)) {
                    ev.setEstado("Borrador");
                } else {
                    ev.setEstado("Disponible");
                }

                ev.setIdEspacio(Integer.parseInt(request.getParameter("idEspacio")));

                Part filePart = request.getPart("img");
                if (filePart != null && filePart.getSize() > 0) {
                    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                    String uploadPath = getServletContext().getRealPath("") + File.separator + "img" + File.separator + "eventos";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdirs();

                    String filePath = uploadPath + File.separator + fileName;
                    filePart.write(filePath);

                    ev.setImagenUrl("img/eventos/" + fileName);
                }

                if (usuarioSesion != null && usuarioSesion.getIdRol() == 1) {
                    String idOrgParam = request.getParameter("idOrganizador");
                    if (idOrgParam != null && !idOrgParam.isEmpty()) {
                        ev.setIdOrganizador(Integer.parseInt(idOrgParam));
                    }
                } else if (usuarioSesion != null) {
                    int idOrg = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
                    ev.setIdOrganizador(idOrg);
                }

                boolean creado = eventoDao.create(ev);

                if (creado) {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&success=" + action);
                } else {
                    response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=create_failed");
                }
                return;

            } catch (NumberFormatException e) {
                System.err.println("Error procesando datos del evento: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/evento?action=gestion&error=invalid_data");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/evento?action=gestion");
    }

    private boolean puedeGestionar(Usuario usuarioSesion, int idEvento) {
        if (usuarioSesion == null) return false;

        if (usuarioSesion.getIdRol() == 1) return true;

        if (usuarioSesion.getIdRol() == 2) {
            Evento evento = eventoDao.getById(idEvento);
            if (evento == null) return false;

            int idOrgSesion = orgDao.getIdOrganizadorByUsuario(usuarioSesion.getId());
            return idOrgSesion == evento.getIdOrganizador();
        }

        return false;
    }
}