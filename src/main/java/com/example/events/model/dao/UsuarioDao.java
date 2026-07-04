package com.example.events.model.dao;

import com.example.events.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    private static final List<Usuario> listaUsuarios = new ArrayList<>();
    public boolean create(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        for (Usuario u : listaUsuarios) {
            if (u.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                System.out.println("El correo ya está registrado.");
                return false;
            }
        }
        listaUsuarios.add(usuario);
        System.out.println("Usuario registrado con éxito: " + usuario.getEmail());
        return true;
    }
    public boolean login(String email, String password) {
        if (email == null || password == null) {
            return false;
        }
        for (Usuario u : listaUsuarios) {
            if (u.getEmail().trim().equalsIgnoreCase(email.trim()) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    public List<Usuario> getAll() {
        return listaUsuarios;
    }
}