package com.example.events.listener;

import com.example.events.model.Usuario;
import com.example.events.utils.SessionRegistry;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Usuario usuario = (Usuario) se.getSession().getAttribute("usuario");

        if (usuario != null) {
            SessionRegistry.removerSesion(usuario.getId());
        }
    }
}