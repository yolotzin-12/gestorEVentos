package com.example.events.model.dao;

import com.example.events.model.Usuario;
import com.example.events.DB.OracleConnectApp;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para UsuarioDao")
public class UsuarioDaoTest {

    private static UsuarioDao usuarioDao;
    private static Usuario usuarioPrueba;
    private static String correoUnico;
    private static String passwordPrueba = "Password123!";

    @BeforeAll
    static void setUpBeforeClass() {
        usuarioDao = new UsuarioDao();
        // Usamos un timestamp para generar un correo único cada que se corra la prueba
        correoUnico = "user_test_" + System.currentTimeMillis() + "@events.com";

        usuarioPrueba = new Usuario();
        usuarioPrueba.setNombre("Juan");
        usuarioPrueba.setApellidoPaterno("Pérez");
        usuarioPrueba.setApellidoMaterno("Gómez");
        usuarioPrueba.setEmail(correoUnico);
        usuarioPrueba.setPassword(passwordPrueba);
        usuarioPrueba.setTelefono("7771234567");
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Registro completo de usuario")
    void testCreate() {
        boolean creado = usuarioDao.create(usuarioPrueba);

        assertTrue(creado, "El registro de usuario debe retornar true");
        assertTrue(usuarioPrueba.getId() > 0, "Se debe haber asignado un id_usuario válido de Oracle");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Autenticación / Login correcto")
    void testLoginExitoso() {
        Usuario logueado = usuarioDao.login(correoUnico, passwordPrueba);

        assertNotNull(logueado, "El login no debe retornar null con credenciales correctas");
        assertEquals(usuarioPrueba.getId(), logueado.getId());
        assertEquals("Juan", logueado.getNombre());
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Obtener por Email (getByEmail)")
    void testGetByEmail() {
        Usuario consultado = usuarioDao.getByEmail(correoUnico);

        assertNotNull(consultado, "El usuario debe encontrarse por correo electrónico");
        assertEquals(usuarioPrueba.getId(), consultado.getId());
    }

    @Test
    @Order(4)
    @DisplayName("4. READ - Obtener por ID (getById)")
    void testGetById() {
        Usuario consultado = usuarioDao.getById(usuarioPrueba.getId());

        assertNotNull(consultado, "El usuario debe encontrarse por su ID");
        assertEquals("Juan", consultado.getNombre());
    }

    @Test
    @Order(5)
    @DisplayName("5. READ - Obtener lista completa (getAll)")
    void testGetAll() {
        List<Usuario> lista = usuarioDao.getAll();

        assertNotNull(lista, "La lista de usuarios no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener registros");

        boolean presente = lista.stream().anyMatch(u -> u.getId() == usuarioPrueba.getId());
        assertTrue(presente, "El usuario registrado debe figurar en getAll()");
    }

    @Test
    @Order(6)
    @DisplayName("6. UPDATE - Actualizar contraseña de perfil")
    void testCambiarContrasenaPerfil() {
        String nuevaPassword = "Nuevapassword456!";
        boolean cambio = usuarioDao.cambiarContrasenaPerfil(usuarioPrueba.getId(), passwordPrueba, nuevaPassword);

        assertTrue(cambio, "El cambio de contraseña debe ser exitoso");

        // Verificar que puede loguearse con la nueva contraseña
        Usuario logueadoNuevasCredenciales = usuarioDao.login(correoUnico, nuevaPassword);
        assertNotNull(logueadoNuevasCredenciales, "Debe permitir el acceso con la nueva contraseña");
    }

    @Test
    @Order(7)
    @DisplayName("7. UPDATE - Deshabilitar usuario (deshabilitar / cambiarEstado)")
    void testDeshabilitar() {
        boolean deshabilitado = usuarioDao.deshabilitar(usuarioPrueba.getId());
        assertTrue(deshabilitado, "deshabilitar() debe retornar true");

        Usuario u = usuarioDao.getByEmail(correoUnico);
        assertFalse(u.isActivo(), "El usuario debe figurar como inactivo (activo = 0)");
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (usuarioPrueba != null && usuarioPrueba.getId() > 0) {
            String delAsistente = "DELETE FROM ASISTENTE WHERE id_usuario = ?";
            String delContrasena = "DELETE FROM CONTRASENA WHERE id_usuario = ?";
            String delUsuario = "DELETE FROM USUARIO WHERE id_usuario = ?";

            try (Connection con = OracleConnectApp.getConnection()) {
                // 1. Borrar hijos primero (Asistente y Contraseña)
                try (PreparedStatement ps1 = con.prepareStatement(delAsistente)) {
                    ps1.setInt(1, usuarioPrueba.getId());
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = con.prepareStatement(delContrasena)) {
                    ps2.setInt(1, usuarioPrueba.getId());
                    ps2.executeUpdate();
                }
                // 2. Borrar al padre (Usuario)
                try (PreparedStatement ps3 = con.prepareStatement(delUsuario)) {
                    ps3.setInt(1, usuarioPrueba.getId());
                    ps3.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}