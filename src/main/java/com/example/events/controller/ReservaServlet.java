package com.example.events.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.events.model.models.Reserva;
import com.example.events.model.dao.ReservaDao;
import java.io.IOException;

@WebServlet(name = "ReservaServlet", value = "/reserva")
public class ReservaServlet extends HttpServlet {

    private final ReservaDao reservaDao = new ReservaDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("reserva.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String nombre = request.getParameter("nombre");
        String matricula = request.getParameter("matricula");
        String carrera = request.getParameter("carrera");
        String email = request.getParameter("email");
        String asistencia = request.getParameter("asistencia");

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setNombre(nombre);
        nuevaReserva.setMatricula(matricula);
        nuevaReserva.setCarrera(carrera);
        nuevaReserva.setEmail(email);
        nuevaReserva.setAsistencia(asistencia);

        reservaDao.create(nuevaReserva);

        response.sendRedirect("paginaPrincipal.jsp");
    }
}