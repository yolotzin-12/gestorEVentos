package com.example.events.controller.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// El asterisco (*) indica que el filtro analizará ABSOLUTAMENTE TODAS las páginas y servlets
@WebFilter(urlPatterns = {"/*"})
public class FiltroAutenticacion extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Obtener la ruta actual que el usuario está intentando visitar
        String requestURI = request.getRequestURI();

        // Obtener la sesión actual (sin crear una nueva)
        HttpSession session = request.getSession(false);

        // Verificar si el usuario tiene una sesión activa con el atributo "usuario"
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        // Definir qué rutas son públicas (no requieren login)
        boolean isLoginJsp = requestURI.endsWith("login.jsp");
        boolean isLoginServlet = requestURI.endsWith("LoginServlet");
        // Permitir archivos estáticos (CSS, imágenes) para que la página de login no pierda el diseño
        boolean isStaticResource = requestURI.contains("/css/") || requestURI.contains("/img/") || requestURI.contains("/js/");

        if (loggedIn) {
            // CASO 1: El usuario SÍ está logueado
            if (isLoginJsp || isLoginServlet) {
                // Si ya está logueado e intenta ir al login, lo mandamos al inicio
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                // Si va a cualquier otra página, lo dejamos pasar
                chain.doFilter(req, res);
            }
        } else {
            // CASO 2: El usuario NO está logueado
            if (isLoginJsp || isLoginServlet || isStaticResource) {
                // Lo dejamos pasar únicamente si va al login o necesita los estilos CSS
                chain.doFilter(req, res);
            } else {
                // Si intenta entrar a index.jsp, EventoServlet, etc., lo redirigimos al login
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }
    }
}