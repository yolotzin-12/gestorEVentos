package com.example.events.utils;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {

    private static final Map<Integer, HttpSession> sesionesActivas = new ConcurrentHashMap<>();

    public static void registrarSesion(Integer idUsuario, HttpSession session) {
        sesionesActivas.put(idUsuario, session);
    }

    public static void removerSesion(Integer idUsuario) {
        sesionesActivas.remove(idUsuario);
    }

    public static void expulsarUsuario(Integer idUsuario) {
        HttpSession session = sesionesActivas.remove(idUsuario);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException e) {

            }
        }
    }
}