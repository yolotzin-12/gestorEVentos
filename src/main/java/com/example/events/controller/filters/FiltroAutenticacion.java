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

@WebFilter(urlPatterns = {"/*"})
public class FiltroAutenticacion extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("usuario") != null);

        // Páginas y servlets públicos (no requieren login)
        boolean isLoginJsp = requestURI.endsWith("login.jsp");
        boolean isRegistroJsp = requestURI.endsWith("registro.jsp");
        boolean isRecuperarJsp = requestURI.endsWith("recuperarContra.jsp");

        boolean isNuevaContraJsp = requestURI.endsWith("nuevaContra.jsp");
        boolean isContraActualizadaJsp = requestURI.endsWith("contraActualizada.jsp");

        boolean isRecuperarServlet  = requestURI.equals(contextPath + "/recuperar");
        boolean isRestablecerServlet = requestURI.equals(contextPath + "/restablecer");
        boolean isLoginServlet = requestURI.equals(contextPath + "/login");
        boolean isRegisterServlet = requestURI.equals(contextPath + "/register");
        boolean isStaticResource = requestURI.contains("/css/") || requestURI.contains("/img/") || requestURI.contains("/js/") || requestURI.contains("/assets/");

        boolean esRutaPublica = isLoginJsp || isRegistroJsp || isRecuperarJsp
                || isNuevaContraJsp || isContraActualizadaJsp // <--- Incluidas en la lista de públicas
                || isLoginServlet || isRegisterServlet || isStaticResource || isRecuperarServlet
                || isRestablecerServlet;

        if (loggedIn) {
            if (isLoginJsp || isRegistroJsp || isRecuperarJsp || isLoginServlet || isRegisterServlet) {
                response.sendRedirect(contextPath + "/index.jsp");
            } else {
                chain.doFilter(req, res);
            }
        } else {
            if (esRutaPublica) {
                chain.doFilter(req, res);
            } else {
                response.sendRedirect(contextPath + "/login.jsp");
            }
        }
    }
}