package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.models.Evento;
import com.example.events.model.dao.EventoDao;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "EventoServlet", value = "/evento")
public class EventoServlet extends HttpServlet {

    private final EventoDao eventoDao = new EventoDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Evento> lista = eventoDao.getAll();
        request.setAttribute("listaEventos", lista);
        request.getRequestDispatcher("gestion-eventos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            String nombre = request.getParameter("nombre");
            //String categoria = request.getParameter("categoria");
            int capacidad = Integer.parseInt(request.getParameter("capacidad"));
            //String ubicacion = request.getParameter("ubicacion");
           // String fecha = request.getParameter("fecha");
            //boolean estado = request.getParameter("estado") != null;
            String img = request.getParameter("img");
            String categoria = request.getParameter("categoria");
            String ubicacion = request.getParameter("ubicacion");
            String fecha = request.getParameter("fecha");
            boolean estado = request.getParameter("estado") != null;

            Evento nuevaEvento = new Evento();
            nuevaEvento.setNombre(nombre);
            nuevaEvento.setCategoria(categoria);
            nuevaEvento.setCapacidad(capacidad);
            nuevaEvento.setUbicacion(ubicacion);
            nuevaEvento.setFecha(fecha);
            nuevaEvento.setEstado(estado);

            eventoDao.create(nuevaEvento);
        } catch (NumberFormatException e) {
            System.err.println("Error al transformar datos numéricos en el registro: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect("evento");
    }
}