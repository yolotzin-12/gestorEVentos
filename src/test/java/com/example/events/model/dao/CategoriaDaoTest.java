package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Categoria;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para CategoriaDao")
public class CategoriaDaoTest {

    private static CategoriaDao categoriaDao;
    private static String nombreCategoriaPrueba;
    private static int idCategoriaCreada;

    @BeforeAll
    static void setUpBeforeClass() {
        categoriaDao = new CategoriaDao();
        // Usamos milisegundos para garantizar que el nombre de la categoría no se repita y viole restricciones UNIQUE
        nombreCategoriaPrueba = "CatTest_" + System.currentTimeMillis();
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Insertar una nueva categoría")
    void testInsertCategoria() {
        boolean insertado = categoriaDao.insertCategoria(nombreCategoriaPrueba);
        assertTrue(insertado, "El método insertCategoria() debe retornar true al insertar exitosamente");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Listar categorías activas y recuperar el ID generado")
    void testGetCategoriasActivas() {
        List<Categoria> lista = categoriaDao.getCategoriasActivas();

        assertNotNull(lista, "La lista de categorías no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener elementos (al menos la que acabamos de crear)");

        // Buscamos nuestra categoría de prueba en la lista para obtener su ID
        Categoria categoriaEncontrada = lista.stream()
                .filter(c -> c.getNombre().equals(nombreCategoriaPrueba))
                .findFirst()
                .orElse(null);

        assertNotNull(categoriaEncontrada, "La categoría insertada debe aparecer en getCategoriasActivas()");

        // Guardamos el ID para usarlo en la prueba de eliminar
        idCategoriaCreada = categoriaEncontrada.getIdCategoria();
        assertTrue(idCategoriaCreada > 0, "El ID de la categoría debe ser mayor a 0");
    }

    @Test
    @Order(3)
    @DisplayName("3. DELETE - Eliminar la categoría")
    void testEliminarCategoria() {
        // 3.1 Probar eliminación exitosa
        String resultadoExitoso = categoriaDao.eliminarCategoria(idCategoriaCreada);
        assertEquals("success", resultadoExitoso, "Al eliminar correctamente, debe retornar 'success'");

        // 3.2 Probar eliminación de un ID que ya no existe
        String resultadoNoEncontrado = categoriaDao.eliminarCategoria(idCategoriaCreada);
        assertEquals("not_found", resultadoNoEncontrado, "Al intentar eliminar un registro inexistente, debe retornar 'not_found'");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // Como medida de seguridad, en caso de que la prueba falle a la mitad, limpiamos por nombre
        String sqlCleanup = "DELETE FROM Categoria WHERE nombre = ?";
        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlCleanup)) {
            ps.setString(1, nombreCategoriaPrueba);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}