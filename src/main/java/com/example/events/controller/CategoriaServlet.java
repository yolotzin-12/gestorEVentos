package com.example.events.controller;

import com.example.events.model.dao.CategoriaDao;
import com.example.events.model.models.Categoria;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "CategoriaServlet", value = "/api/categoria")
public class CategoriaServlet extends HttpServlet {

    private final CategoriaDao categoriaDao = new CategoriaDao();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            BufferedReader reader = request.getReader();
            Categoria nuevaCategoria = gson.fromJson(reader, Categoria.class);

            if (nuevaCategoria != null && nuevaCategoria.getNombre() != null && !nuevaCategoria.getNombre().trim().isEmpty()) {
                boolean exito = categoriaDao.insertCategoria(nuevaCategoria.getNombre().trim());
                if (exito) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Categoría creada correctamente.");
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Error al crear la categoría (quizás ya existe).");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "El nombre de la categoría está vacío.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error interno del servidor.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
                jsonResponse.addProperty("message", "ID de categoría no proporcionado.");
                response.getWriter().write(jsonResponse.toString());
                return;
            }

            int idCategoria = Integer.parseInt(idParam);
            String resultado = categoriaDao.eliminarCategoria(idCategoria);

            switch (resultado) {
                case "success":
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Categoría eliminada exitosamente.");
                    break;
                case "in_use":
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "No se puede eliminar: hay eventos que usan esta categoría.");
                    break;
                default:
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Error al eliminar la categoría en la BD.");
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