package com.example.events.controller;

import com.example.events.model.Usuario;
import com.example.events.model.dao.EspacioDao;
import com.example.events.model.models.Espacio;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EspacioServlet", value = "/api/espacio")
public class EspacioServlet extends HttpServlet {

    private final EspacioDao espacioDao = new EspacioDao();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String idParam = request.getParameter("id");

        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int idEspacio = Integer.parseInt(idParam);
                Espacio espacio = espacioDao.getEspacioById(idEspacio);

                if (espacio != null) {
                    response.getWriter().write(gson.toJson(espacio));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject err = new JsonObject();
                    err.addProperty("status", "error");
                    err.addProperty("message", "Espacio no encontrado.");
                    response.getWriter().write(err.toString());
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject err = new JsonObject();
                err.addProperty("status", "error");
                err.addProperty("message", "Formato de ID inválido.");
                response.getWriter().write(err.toString());
            }
        } else {
            List<Espacio> lista = espacioDao.getAllEspacios();
            response.getWriter().write(gson.toJson(lista));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if (!tieneAcceso(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "No tienes permisos para realizar esta acción.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            BufferedReader reader = request.getReader();
            Espacio nuevoEspacio = gson.fromJson(reader, Espacio.class);

            if (nuevoEspacio != null && nuevoEspacio.getNombreEspacio() != null && !nuevoEspacio.getNombreEspacio().trim().isEmpty()) {
                boolean exito = espacioDao.insertEspacio(nuevoEspacio);
                if (exito) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Espacio creado correctamente.");
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Error al crear el espacio en la BD.");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "El nombre del espacio es obligatorio.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error interno.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if (!tieneAcceso(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "No tienes permisos para realizar esta acción.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            BufferedReader reader = request.getReader();
            Espacio espacio = gson.fromJson(reader, Espacio.class);

            if (espacio == null || espacio.getIdEspacio() <= 0) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "ID de espacio no proporcionado o inválido.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            if (espacio.getNombreEspacio() == null || espacio.getNombreEspacio().trim().isEmpty()) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "El nombre del espacio es obligatorio.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            boolean exito = espacioDao.actualizarEspacio(espacio);

            if (exito) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Espacio actualizado correctamente.");
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "No se pudo actualizar el espacio (verifica que exista).");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error interno.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        response.getWriter().write(jsonResponse.toString());
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        if (!tieneAcceso(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "No tienes permisos para realizar esta acción.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "ID de espacio no proporcionado.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            int idEspacio = Integer.parseInt(idParam);
            String resultado = espacioDao.eliminarEspacio(idEspacio);

            switch (resultado) {
                case "success":
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Espacio eliminado exitosamente.");
                    break;
                case "in_use":
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "No se puede eliminar: hay eventos asignados a este espacio.");
                    break;
                default:
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Error al eliminar el espacio en la BD.");
                    break;
            }
        } catch (NumberFormatException e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Formato de ID inválido.");
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error interno.");
        }

        response.getWriter().write(jsonResponse.toString());
    }


    private boolean tieneAcceso(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) return false;

        return usuario.getIdRol() == 1;
    }
}