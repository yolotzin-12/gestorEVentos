package com.example.events.controller;

import com.example.events.model.dao.OrganizadorDao;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "OrganizadorServlet", value = "/api/organizador")
public class OrganizadorServlet extends HttpServlet {

    private final OrganizadorDao dao = new OrganizadorDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        try {
            BufferedReader reader = request.getReader();
            JsonObject jsonRequest = JsonParser.parseReader(reader).getAsJsonObject();

            int idOrganizador = jsonRequest.get("idOrganizador").getAsInt();
            String organizacion = jsonRequest.get("organizacion").getAsString();

            boolean actualizado = dao.actualizarOrganizacion(idOrganizador, organizacion);

            if (actualizado) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Organización asignada correctamente.");
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "No se pudo actualizar la organización.");
            }
        } catch (Exception e) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Error en el servidor: " + e.getMessage());
        }

        response.getWriter().write(jsonResponse.toString());
    }
}