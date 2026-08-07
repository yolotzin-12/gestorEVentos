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
        // Configurar la respuesta para que sea JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonResponse = new JsonObject();

        try {
            // 1. Leer el JSON del cuerpo de la petición
            BufferedReader reader = request.getReader();
            Categoria nuevaCategoria = gson.fromJson(reader, Categoria.class);

            // 2. Validar y guardar en BD
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

        // 3. Enviar respuesta JSON al Frontend
        response.getWriter().write(jsonResponse.toString());
    }
}