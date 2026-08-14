package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Asistente;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para AsistenteDao")
public class AsistenteDaoTest {

    private static AsistenteDao asistenteDao;
    private static int idUsuarioAuxiliar;
    private static int idAsistenteCreado;

    @BeforeAll
    static void setUpBeforeClass() {
        asistenteDao = new AsistenteDao();

        // Agregamos 'id_rol' y 'activo' en el INSERT para cumplir con las reglas NOT NULL de Oracle y evitar el ORA-01400
        String sqlUsuario = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, apellido_materno, correo_electronico, activo) " +
                "VALUES (3, 'AsisTemp', 'Prueba', 'Dao', 'test_asist_" + System.currentTimeMillis() + "@events.com', 1)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUsuario, new String[]{"ID_USUARIO"})) {

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idUsuarioAuxiliar = rs.getInt(1);
            }
        } catch (SQLException e) {
            fail("No se pudo insertar el usuario auxiliar para probar el Asistente: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Crear Asistente")
    void testCreate() {
        Asistente asistente = new Asistente();
        asistente.setIdUsuario(idUsuarioAuxiliar);
        asistente.setTelefono("7779998888");

        boolean creado = asistenteDao.create(asistente);
        assertTrue(creado, "El método create() debe retornar true al insertar el asistente en Oracle");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Obtener ID Asistente por ID Usuario")
    void testGetIdAsistenteByUsuario() {
        idAsistenteCreado = asistenteDao.getIdAsistenteByUsuario(idUsuarioAuxiliar);

        assertTrue(idAsistenteCreado > 0, "Debe retornar un id_asistente válido mayor a 0");
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Verificar si tiene reservas activas")
    void testTieneReservasActivas() {
        boolean tieneReservas = asistenteDao.tieneReservasActivas(idUsuarioAuxiliar);
        assertFalse(tieneReservas, "Un asistente nuevo no debe tener reservas activas, debe retornar false");
    }

    /* =========================================================
       PRUEBAS DE MÉTODOS AÚN NO IMPLEMENTADOS (MOCKS)
       ========================================================= */

    @Test
    @Order(4)
    @DisplayName("4. READ - Listar todos (getAll - Método Vacío)")
    void testGetAll() {
        List<Asistente> lista = asistenteDao.getAll();
        assertTrue(lista.isEmpty(), "Actualmente getAll() retorna una lista vacía");
    }

    @Test
    @Order(5)
    @DisplayName("5. READ - Consultar por ID (getById - Método Vacío)")
    void testGetById() {
        Asistente a = asistenteDao.getById(idAsistenteCreado);
        assertNull(a, "Actualmente getById() retorna null");
    }

    @Test
    @Order(6)
    @DisplayName("6. UPDATE - Actualizar (update - Método Vacío)")
    void testUpdate() {
        Asistente a = new Asistente();
        boolean actualizado = asistenteDao.update(a);
        assertFalse(actualizado, "Actualmente update() retorna false");
    }

    @Test
    @Order(7)
    @DisplayName("7. DELETE - Eliminar (delete - Método Vacío)")
    void testDelete() {
        boolean eliminado = asistenteDao.delete(idAsistenteCreado);
        assertFalse(eliminado, "Actualmente delete() retorna false");
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (idUsuarioAuxiliar > 0) {
            String delAsistente = "DELETE FROM ASISTENTE WHERE id_usuario = ?";
            String delUsuario = "DELETE FROM USUARIO WHERE id_usuario = ?";

            try (Connection con = OracleConnectApp.getConnection()) {
                // 1. Eliminar el Asistente recién creado
                try (PreparedStatement psAsis = con.prepareStatement(delAsistente)) {
                    psAsis.setInt(1, idUsuarioAuxiliar);
                    psAsis.executeUpdate();
                }
                // 2. Eliminar el Usuario temporal
                try (PreparedStatement psUsu = con.prepareStatement(delUsuario)) {
                    psUsu.setInt(1, idUsuarioAuxiliar);
                    psUsu.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}