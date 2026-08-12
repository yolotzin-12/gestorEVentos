package com.example.events.controller;

import com.example.events.model.dao.EspacioDao;
import com.example.events.model.models.Espacio;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "EspacioServlet", value = "/api/espacio")
public class EspacioServlet extends HttpServlet {

    private final EspacioDao espacioDao = new EspacioDao();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

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
                }
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "El nombre del espacio es obligatorio.");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error interno.");
        }
        response.getWriter().write(jsonResponse.toString());
    }

    // NUEVO MÉTODO DODelete
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

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
}