package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Organizador;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para OrganizadorDao")
public class OrganizadorDaoTest {

    private static OrganizadorDao organizadorDao;
    private static int idUsuarioAuxiliar;
    private static int idOrganizadorCreado;

    @BeforeAll
    static void setUpBeforeClass() {
        organizadorDao = new OrganizadorDao();

        // 1. Crear un usuario base (con id_rol = 2 para Organizador) para cumplir la restricción de FK
        String sqlUsuario = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, apellido_materno, correo_electronico, activo) " +
                "VALUES (2, 'OrgTest', 'Perez', 'Dao', 'test_org_" + System.currentTimeMillis() + "@events.com', 1)";

        try (Connection con = OracleConnectApp.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUsuario, new String[]{"ID_USUARIO"})) {

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idUsuarioAuxiliar = rs.getInt(1);
            }
        } catch (SQLException e) {
            fail("No se pudo insertar el usuario auxiliar para probar el Organizador: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Crear Organizador")
    void testCreate() {
        Organizador org = new Organizador();
        org.setIdUsuario(idUsuarioAuxiliar);
        org.setOrganizacion("Comité de Eventos Inicial");

        boolean creado = organizadorDao.create(org);
        assertTrue(creado, "El método create() debe retornar true al insertar en la tabla ORGANIZADOR");
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Obtener ID Organizador por ID Usuario")
    void testGetIdOrganizadorByUsuario() {
        idOrganizadorCreado = organizadorDao.getIdOrganizadorByUsuario(idUsuarioAuxiliar);

        assertTrue(idOrganizadorCreado > 0, "Debe retornar un id_organizador válido mayor a 0");
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Listar todos los Organizadores (getAllOrganizadores)")
    void testGetAllOrganizadores() {
        List<Organizador> lista = organizadorDao.getAllOrganizadores();

        assertNotNull(lista, "La lista de organizadores no debe ser nula");
        assertFalse(lista.isEmpty(), "La lista debe contener elementos");

        // Buscar nuestro organizador en la lista devuelta
        Organizador orgEncontrado = lista.stream()
                .filter(o -> o.getId() == idOrganizadorCreado)
                .findFirst()
                .orElse(null);

        assertNotNull(orgEncontrado, "El organizador recién creado debe estar en la lista");
        // Verificamos que el JOIN haya extraído correctamente los datos del Usuario
        assertEquals("OrgTest", orgEncontrado.getNombre(), "El nombre mapeado desde Usuario debe coincidir");
        assertEquals("Perez", orgEncontrado.getApellidoPaterno(), "El apellido paterno mapeado desde Usuario debe coincidir");
    }

    @Test
    @Order(4)
    @DisplayName("4. UPDATE - Actualizar nombre de la organización")
    void testActualizarOrganizacion() {
        boolean actualizado = organizadorDao.actualizarOrganizacion(idOrganizadorCreado, "Comité de Eventos Actualizado");
        assertTrue(actualizado, "actualizarOrganizacion() debe retornar true");

        // Verificamos el cambio usando la lista (ya que getById está vacío por ahora)
        List<Organizador> lista = organizadorDao.getAllOrganizadores();
        Organizador orgModificado = lista.stream()
                .filter(o -> o.getId() == idOrganizadorCreado)
                .findFirst()
                .orElse(null);

        assertNotNull(orgModificado);
        assertEquals("Comité de Eventos Actualizado", orgModificado.getOrganizacion());
    }

    @Test
    @Order(5)
    @DisplayName("5. READ - Verificar si tiene eventos activos")
    void testTieneEventosActivos() {
        // Al ser un organizador recién creado, no debería tener ningún evento activo asociado
        boolean tieneEventos = organizadorDao.tieneEventosActivos(idUsuarioAuxiliar);

        assertFalse(tieneEventos, "Un organizador nuevo no debe tener eventos activos, debe retornar false");
    }

    /* =========================================================
       PRUEBAS DE MÉTODOS AÚN NO IMPLEMENTADOS (EN DESARROLLO)
       ========================================================= */

    @Test
    @Order(6)
    @DisplayName("6. READ - Listar (getAll - Método Vacío)")
    void testGetAll() {
        List<Organizador> lista = organizadorDao.getAll();
        assertTrue(lista.isEmpty(), "Actualmente getAll() retorna una lista vacía");
    }

    @Test
    @Order(7)
    @DisplayName("7. READ - Consultar por ID (getById - Método Vacío)")
    void testGetById() {
        Organizador o = organizadorDao.getById(idOrganizadorCreado);
        assertNull(o, "Actualmente getById() retorna null");
    }

    @Test
    @Order(8)
    @DisplayName("8. UPDATE - Actualizar (update - Método Vacío)")
    void testUpdate() {
        Organizador o = new Organizador();
        boolean actualizado = organizadorDao.update(o);
        assertFalse(actualizado, "Actualmente update() retorna false");
    }

    @Test
    @Order(9)
    @DisplayName("9. DELETE - Eliminar (delete - Método Vacío)")
    void testDelete() {
        boolean eliminado = organizadorDao.delete(idOrganizadorCreado);
        assertFalse(eliminado, "Actualmente delete() retorna false");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // Limpieza de BD respetando la jerarquía de las Foreign Keys (Borrar al Organizador primero y al Usuario después)
        if (idUsuarioAuxiliar > 0) {
            String delOrganizador = "DELETE FROM ORGANIZADOR WHERE id_usuario = ?";
            String delUsuario = "DELETE FROM USUARIO WHERE id_usuario = ?";

            try (Connection con = OracleConnectApp.getConnection()) {
                // 1. Eliminar Organizador
                try (PreparedStatement psOrg = con.prepareStatement(delOrganizador)) {
                    psOrg.setInt(1, idUsuarioAuxiliar);
                    psOrg.executeUpdate();
                }
                // 2. Eliminar Usuario
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