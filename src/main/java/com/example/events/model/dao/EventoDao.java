package com.example.events.model.dao;

import com.example.events.model.models.Evento;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoDao implements Dao<Evento, Integer> {

    // Ruta del archivo CSV donde se guardarán los datos de prueba
    private static final String FILE_PATH = "eventos_test.csv";
    private static final String SEPARADOR = ",";

    public EventoDao() {
        // Al instanciar el DAO, verifica si el archivo existe. Si no, lo crea.
        File archivo = new File(FILE_PATH);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.out.println("Error al crear el archivo CSV: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean create(Evento entidad) {
        List<Evento> eventos = getAll();

        // Autoincrementar el ID (busca el mayor ID actual y le suma 1)
        int nuevoId = 1;
        for (Evento e : eventos) {
            if (e.getId() >= nuevoId) {
                nuevoId = e.getId() + 1;
            }
        }
        entidad.setId(nuevoId);
        eventos.add(entidad);

        return guardarTodos(eventos);
    }

    @Override
    public List<Evento> getAll() {
        List<Evento> datos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue; // Saltar líneas vacías

                String[] partes = linea.split(SEPARADOR);
                if (partes.length >= 7) {
                    Evento e = new Evento();
                    e.setId(Integer.parseInt(partes[0]));
                    e.setNombre(partes[1]);
                    e.setCategoria(partes[2]);
                    e.setCapacidad(Integer.parseInt(partes[3]));
                    e.setUbicacion(partes[4]);
                    e.setFecha(partes[5]);
                    e.setEstado(Boolean.parseBoolean(partes[6])); // true o false
                    datos.add(e);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer el CSV: " + e.getMessage());
        }
        return datos;
    }

    @Override
    public Evento getById(Integer id) {
        List<Evento> eventos = getAll();
        for (Evento e : eventos) {
            if (e.getId() == id) {
                return e; // Retorna el evento si coincide el ID
            }
        }
        return null; // Si no lo encuentra, retorna null
    }

    @Override
    public boolean update(Evento entidad) {
        List<Evento> eventos = getAll();
        boolean encontrado = false;

        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getId() == entidad.getId()) {
                eventos.set(i, entidad); // Reemplaza el evento viejo por el nuevo
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            return guardarTodos(eventos);
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        List<Evento> eventos = getAll();
        boolean removido = eventos.removeIf(e -> e.getId() == id); // Elimina de la lista si el ID coincide

        if (removido) {
            return guardarTodos(eventos); // Sobreescribe el archivo sin el elemento eliminado
        }
        return false;
    }

    /**
     * Método auxiliar privado que sobreescribe todo el archivo CSV
     * con la lista actual de eventos.
     */
    private boolean guardarTodos(List<Evento> eventos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Evento e : eventos) {
                String linea = e.getId() + SEPARADOR +
                        e.getNombre() + SEPARADOR +
                        e.getCategoria() + SEPARADOR +
                        e.getCapacidad() + SEPARADOR +
                        e.getUbicacion() + SEPARADOR +
                        e.getFecha() + SEPARADOR +
                        e.isEstado();
                bw.write(linea);
                bw.newLine();
            }
            return true;
        } catch (IOException ex) {
            System.out.println("Error al escribir en el CSV: " + ex.getMessage());
            return false;
        }
    }
}
