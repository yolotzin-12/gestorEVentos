package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.TokenRecuperacion;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para TokenRecuperacionDao")
public class TokenRecuperacionDaoTest {

    private static TokenRecuperacionDao tokenDao;
    private static int idUsuarioAux;
    private static String hashPrueba;
    private static int idTokenGenerado;

    @BeforeAll
    static void setUpBeforeClass() {
        tokenDao = new TokenRecuperacionDao();
        long time = System.currentTimeMillis();

        // Simulamos el hash encriptado que generaría tu lógica de negocio
        hashPrueba = "HASH_TEST_" + time;

        // Creamos el usuario base con todos los campos obligatorios para no disparar el ORA-01400
        String sqlUsuario = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, apellido_materno, correo_electronico, activo) " +
                "VALUES (3, 'TokenUser', 'Paterno', 'Materno', 'token_" + time + "@events.com', 1)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUsuario, new String[]{"ID_USUARIO"})) {

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idUsuarioAux = rs.getInt(1);
            }
        } catch (SQLException e) {
            fail("No se pudo preparar el usuario para el token de prueba: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Crear un Token de Recuperación")
    void testCrearToken() {
        boolean creado = tokenDao.crear(idUsuarioAux, hashPrueba);
        assertTrue(creado, "El método crear() debe retornar true al insertar el token exitosamente");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Validar Token Activo")
    void testValidarTokenActivo() {
        TokenRecuperacion token = tokenDao.validar(hashPrueba);

        assertNotNull(token, "El token recién creado debe ser válido (no nulo)");
        assertEquals(hashPrueba, token.getTokenHash(), "El hash devuelto debe coincidir con el consultado");
        assertFalse(token.isUsado(), "El token nuevo debe tener su estado 'usado' en falso (0)");

        // Guardamos el ID del token para la siguiente prueba
        idTokenGenerado = token.getId();
        assertTrue(idTokenGenerado > 0, "El ID del token debe ser mayor a 0");
    }

    @Test
    @Order(3)
    @DisplayName("3. UPDATE - Marcar el Token como usado")
    void testMarcarUsado() {
        boolean marcado = tokenDao.marcarUsado(idTokenGenerado);
        assertTrue(marcado, "marcarUsado() debe retornar true al actualizar el registro en la BD");
    }

    @Test
    @Order(4)
    @DisplayName("4. READ - Rechazar Token que ya fue usado")
    void testRechazarTokenUsado() {
        // Al intentar validar el mismo hash después de haberlo marcado como usado, debe retornar null
        TokenRecuperacion tokenInvalido = tokenDao.validar(hashPrueba);
        assertNull(tokenInvalido, "validar() debe retornar null si el token ya fue utilizado");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // Limpiamos los registros, respetando la Foreign Key (Token primero, Usuario después)
        if (idUsuarioAux > 0) {
            String delToken = "DELETE FROM TOKEN_RECUPERACION WHERE id_usuario = ?";
            String delUsuario = "DELETE FROM USUARIO WHERE id_usuario = ?";

            try (Connection con = OracleConnectApp.getConnection()) {
                try (PreparedStatement psToken = con.prepareStatement(delToken)) {
                    psToken.setInt(1, idUsuarioAux);
                    psToken.executeUpdate();
                }
                try (PreparedStatement psUsu = con.prepareStatement(delUsuario)) {
                    psUsu.setInt(1, idUsuarioAux);
                    psUsu.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}