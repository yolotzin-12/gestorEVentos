package com.example.events.model.dao;

import com.example.events.model.models.Reserva;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReservaDao implements Dao<Reserva, Integer> {

    private static final String FILE_PATH = "reservas_test.csv";
    private static final String SEPARADOR = ",";

    public ReservaDao() {
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo CSV de reservas: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean create(Reserva entidad) {
        List<Reserva> reservas = getAll();

        int nuevoId = 1;
        for (Reserva r : reservas) {
            if (r.getId() >= nuevoId) {
                nuevoId = r.getId() + 1;
            }
        }
        entidad.setId(nuevoId);
        reservas.add(entidad);

        return guardarTodos(reservas);
    }

    @Override
    public List<Reserva> getAll() {
        List<Reserva> datos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(SEPARADOR);
                if (partes.length >= 6) {
                    Reserva r = new Reserva();
                    r.setId(Integer.parseInt(partes[0]));
                    r.setNombre(partes[1]);
                    r.setMatricula(partes[2]);
                    r.setCarrera(partes[3]);
                    r.setEmail(partes[4]);
                    r.setAsistencia(partes[5]);
                    datos.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el CSV de reservas: " + e.getMessage());
        }
        return datos;
    }

    @Override
    public Reserva getById(Integer id) {
        for (Reserva r : getAll()) {
            if (r.getId() == id) return r;
        }
        return null;
    }

    @Override
    public boolean update(Reserva entidad) {
        List<Reserva> reservas = getAll();
        boolean encontrado = false;
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == entidad.getId()) {
                reservas.set(i, entidad);
                encontrado = true;
                break;
            }
        }
        return encontrado && guardarTodos(reservas);
    }

    @Override
    public boolean delete(Integer id) {
        List<Reserva> reservas = getAll();
        boolean removido = reservas.removeIf(r -> r.getId() == id);
        return removido && guardarTodos(reservas);
    }

    private boolean guardarTodos(List<Reserva> reservas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Reserva r : reservas) {
                String linea = r.getId() + SEPARADOR +
                        r.getNombre() + SEPARADOR +
                        r.getMatricula() + SEPARADOR +
                        r.getCarrera() + SEPARADOR +
                        r.getEmail() + SEPARADOR +
                        r.getAsistencia();
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir en el CSV de reservas: " + ex.getMessage());
            return false;
        }
    }
}