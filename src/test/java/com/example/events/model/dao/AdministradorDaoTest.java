package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.Usuario;
import com.example.events.model.models.Administrador;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para AdministradorDao")
public class AdministradorDaoTest {

    private static AdministradorDao administradorDao;
    private static UsuarioDao usuarioDao;
    private static Usuario usuarioAuxiliar;
    private static int idAdminCreado;

    @BeforeAll
    static void setUpBeforeClass() {
        administradorDao = new AdministradorDao();
        usuarioDao = new UsuarioDao();

        // Creamos un usuario completo usando UsuarioDao para garantizar que cumpla con contraseñas, roles y tablas relacionadas
        usuarioAuxiliar = new Usuario();
        usuarioAuxiliar.setNombre("TestTemp");
        usuarioAuxiliar.setApellidoPaterno("Prueba");
        usuarioAuxiliar.setApellidoMaterno("Dao");
        usuarioAuxiliar.setEmail("admin_test_" + System.currentTimeMillis() + "@events.com");
        usuarioAuxiliar.setPassword("Pass1234");
        usuarioAuxiliar.setTelefono("7771112233");

        boolean usuarioCreado = usuarioDao.create(usuarioAuxiliar);
        assertTrue(usuarioCreado, "Debe crearse el usuario auxiliar antes de probar AdministradorDao");
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Crear Administrador")
    void testCreate() {
        Administrador admin = new Administrador();
        admin.setIdUsuario(usuarioAuxiliar.getId());
        admin.setNivelAcceso("total");

        boolean resultado = administradorDao.create(admin);

        assertTrue(resultado, "create() debe retornar true al insertar en Oracle");
        assertTrue(admin.getId() > 0, "El ID del administrador debe ser mayor a 0");

        idAdminCreado = admin.getId();
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Consultar por ID (getById)")
    void testGetById() {
        Administrador admin = administradorDao.getById(idAdminCreado);

        assertNotNull(admin, "El administrador recién insertado debe existir");
        assertEquals(idAdminCreado, admin.getId());
        assertEquals("TestTemp", admin.getNombre(), "Debe mapear el nombre del usuario vía JOIN");
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Obtener ID Admin por Usuario")
    void testGetIdAdminByUsuario() {
        int idAdminObtenido = administradorDao.getIdAdminByUsuario(usuarioAuxiliar.getId());
        assertEquals(idAdminCreado, idAdminObtenido);
    }

    @Test
    @Order(4)
    @DisplayName("4. READ - Listar todos (getAll)")
    void testGetAll() {
        List<Administrador> lista = administradorDao.getAll();

        assertNotNull(lista, "La lista no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener elementos");

        boolean existe = lista.stream().anyMatch(a -> a.getId() == idAdminCreado);
        assertTrue(existe, "El administrador creado debe encontrarse en la lista");
    }

    @Test
    @Order(5)
    @DisplayName("5. UPDATE - Actualizar nivel de acceso")
    void testUpdate() {
        Administrador admin = administradorDao.getById(idAdminCreado);
        admin.setNivelAcceso("parcial");

        boolean actualizado = administradorDao.update(admin);
        assertTrue(actualizado, "update() debe retornar true");

        Administrador adminModificado = administradorDao.getById(idAdminCreado);
        assertEquals("parcial", adminModificado.getNivelAcceso());
    }

    @Test
    @Order(6)
    @DisplayName("6. DELETE - Eliminar Administrador")
    void testDelete() {
        boolean eliminado = administradorDao.delete(idAdminCreado);
        assertTrue(eliminado, "delete() debe retornar true");

        Administrador adminEliminado = administradorDao.getById(idAdminCreado);
        assertNull(adminEliminado, "El administrador ya no debe existir tras eliminarlo");
    }

    @AfterAll
    static void tearDownAfterClass() {
        if (usuarioAuxiliar != null && usuarioAuxiliar.getId() > 0) {
            String delAsistente = "DELETE FROM ASISTENTE WHERE id_usuario = ?";
            String delContrasena = "DELETE FROM CONTRASENA WHERE id_usuario = ?";
            String delUsuario = "DELETE FROM USUARIO WHERE id_usuario = ?";

            try (Connection con = OracleConnectApp.getConnection()) {
                // 1. Borrar hijos primero (Asistente y Contraseña)
                try (PreparedStatement ps1 = con.prepareStatement(delAsistente)) {
                    ps1.setInt(1, usuarioAuxiliar.getId());
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = con.prepareStatement(delContrasena)) {
                    ps2.setInt(1, usuarioAuxiliar.getId());
                    ps2.executeUpdate();
                }
                // 2. Borrar al padre (Usuario)
                try (PreparedStatement ps3 = con.prepareStatement(delUsuario)) {
                    ps3.setInt(1, usuarioAuxiliar.getId());
                    ps3.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}