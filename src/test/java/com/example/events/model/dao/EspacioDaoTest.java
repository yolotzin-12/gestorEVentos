package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Espacio;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para EspacioDao")
public class EspacioDaoTest {

    private static EspacioDao espacioDao;
    private static String nombreEspacioPrueba;
    private static int idEspacioCreado;

    @BeforeAll
    static void setUpBeforeClass() {
        espacioDao = new EspacioDao();
        // Usamos milisegundos para garantizar un nombre único y evitar violar restricciones en la base de datos
        nombreEspacioPrueba = "Sala de Convenciones Test_" + System.currentTimeMillis();
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Insertar un nuevo espacio")
    void testInsertEspacio() {
        // Arrange: Preparamos el objeto Espacio con todos sus datos
        Espacio espacio = new Espacio();
        espacio.setNombreEspacio(nombreEspacioPrueba);
        espacio.setCapacidad(150);
        espacio.setUbicacion("Edificio Principal - Planta Baja");
        espacio.setHorario("08:00 - 18:00");
        espacio.setImagenUrl("ruta/imagen.jpg");

        // Act & Assert
        boolean insertado = espacioDao.insertEspacio(espacio);
        assertTrue(insertado, "El método insertEspacio() debe retornar true al insertar exitosamente");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Listar espacios y recuperar el ID generado")
    void testGetAllEspacios() {
        // Act
        List<Espacio> lista = espacioDao.getAllEspacios();

        // Assert: Validaciones generales de la lista
        assertNotNull(lista, "La lista de espacios no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener elementos (al menos el insertado)");

        // Buscamos nuestro espacio de prueba específico para capturar su ID
        Espacio espacioEncontrado = lista.stream()
                .filter(e -> nombreEspacioPrueba.equals(e.getNombreEspacio()))
                .findFirst()
                .orElse(null);

        assertNotNull(espacioEncontrado, "El espacio que insertamos debe aparecer en la lista de getAllEspacios()");
        assertEquals("Edificio Principal - Planta Baja", espacioEncontrado.getUbicacion(), "La ubicación mapeada debe coincidir");

        // Guardamos el ID para usarlo en la siguiente prueba
        idEspacioCreado = espacioEncontrado.getIdEspacio();
        assertTrue(idEspacioCreado > 0, "El ID del espacio debe ser un número válido mayor a 0");
    }

    @Test
    @Order(3)
    @DisplayName("3. DELETE - Eliminar el espacio")
    void testEliminarEspacio() {
        // 3.1 Probar eliminación exitosa
        String resultadoExitoso = espacioDao.eliminarEspacio(idEspacioCreado);
        assertEquals("success", resultadoExitoso, "Al eliminar correctamente, debe retornar 'success'");

        // 3.2 Probar eliminación de un registro que ya no existe en la base de datos
        String resultadoNoEncontrado = espacioDao.eliminarEspacio(idEspacioCreado);
        assertEquals("not_found", resultadoNoEncontrado, "Al intentar eliminar un registro previamente borrado, debe retornar 'not_found'");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // Limpieza de seguridad: Si alguna prueba falla (por ejemplo, el DELETE), nos aseguramos de borrar el registro para no ensuciar tu Oracle DB
        String sqlCleanup = "DELETE FROM Espacio WHERE nombre_espacio = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCleanup)) {
            ps.setString(1, nombreEspacioPrueba);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}