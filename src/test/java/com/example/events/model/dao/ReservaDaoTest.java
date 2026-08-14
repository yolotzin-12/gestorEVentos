package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Reserva;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para ReservaDao")
public class ReservaDaoTest {

    private static ReservaDao reservaDao;

    // IDs de las dependencias requeridas (Todo el ecosistema SRAE)
    private static int idCategoriaAux;
    private static int idEspacioAux;
    private static int idUsuarioOrgAux;
    private static int idOrganizadorAux;
    private static int idEventoAux;
    private static int idUsuarioAsisAux;
    private static int idAsistenteAux;

    // ID de la Reserva que probaremos
    private static int idReservaCreada;

    @BeforeAll
    static void setUpBeforeClass() {
        reservaDao = new ReservaDao();
        long time = System.currentTimeMillis();

        String sqlCat = "INSERT INTO CATEGORIA (nombre) VALUES ('CatRes_" + time + "')";
        String sqlEspacio = "INSERT INTO ESPACIO (nombre_espacio, ubicacion) VALUES ('Foro_Res', 'Planta 1')";

        // CORRECCIÓN ORA-01400: Agregamos apellido_paterno y apellido_materno obligatorios
        String sqlUsuOrg = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, apellido_materno, correo_electronico, activo) " +
                "VALUES (2, 'OrgRes', 'PaternoOrg', 'MaternoOrg', 'org_" + time + "@events.com', 1)";
        String sqlOrg = "INSERT INTO ORGANIZADOR (id_usuario, organizacion) VALUES (?, 'Comité Test')";

        String sqlEvento = "INSERT INTO EVENTO (id_organizador, id_espacio, id_categoria, nombre, capacidad_maxima, capacidad_disponible, fecha_hora, estado) " +
                "VALUES (?, ?, ?, 'Concierto Test', 50, 50, TO_TIMESTAMP('2030-12-31 20:00:00', 'YYYY-MM-DD HH24:MI:SS'), 'Disponible')";

        // CORRECCIÓN ORA-01400: Agregamos apellido_paterno y apellido_materno obligatorios
        String sqlUsuAsis = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, apellido_materno, correo_electronico, activo) " +
                "VALUES (3, 'AsisRes', 'PaternoAsis', 'MaternoAsis', 'asis_" + time + "@events.com', 1)";
        String sqlAsis = "INSERT INTO ASISTENTE (id_usuario, telefono) VALUES (?, '7771234567')";

        try (Connection con = OracleConnectApp.getConnection()) {
            idCategoriaAux = insertarYObtenerId(con, sqlCat, "ID_CATEGORIA");
            idEspacioAux = insertarYObtenerId(con, sqlEspacio, "ID_ESPACIO");

            idUsuarioOrgAux = insertarYObtenerId(con, sqlUsuOrg, "ID_USUARIO");
            idOrganizadorAux = insertarConFK(con, sqlOrg, idUsuarioOrgAux, "ID_ORGANIZADOR");

            try (PreparedStatement ps = con.prepareStatement(sqlEvento, new String[]{"ID_EVENTO"})) {
                ps.setInt(1, idOrganizadorAux);
                ps.setInt(2, idEspacioAux);
                ps.setInt(3, idCategoriaAux);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idEventoAux = rs.getInt(1);
            }

            idUsuarioAsisAux = insertarYObtenerId(con, sqlUsuAsis, "ID_USUARIO");
            idAsistenteAux = insertarConFK(con, sqlAsis, idUsuarioAsisAux, "ID_ASISTENTE");

        } catch (SQLException e) {
            fail("Falló la preparación del ecosistema en BD: " + e.getMessage());
        }
    }

    private static int insertarYObtenerId(Connection con, String sql, String pkColumn) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, new String[]{pkColumn})) {
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    private static int insertarConFK(Connection con, String sql, int fk, String pkColumn) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, new String[]{pkColumn})) {
            ps.setInt(1, fk);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : fk;
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Generar una nueva Reserva")
    void testCreate() {
        Reserva reserva = new Reserva();
        reserva.setIdEvento(idEventoAux);
        reserva.setIdAsistente(idAsistenteAux);

        boolean creado = reservaDao.create(reserva);

        assertTrue(creado, "Debe retornar true al procesar la reserva exitosamente");
        assertTrue(reserva.getId() > 0, "Debe generar y asignar un ID de reserva válido");
        assertNotNull(reserva.getCodigoReserva(), "Debe autogenerar un código de reserva (ej. SRAE-XXXXXX)");

        idReservaCreada = reserva.getId();
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Consultar reserva por ID (getById)")
    void testGetById() {
        Reserva reserva = reservaDao.getById(idReservaCreada);

        assertNotNull(reserva);
        assertEquals(idReservaCreada, reserva.getId());
        assertEquals("Reservado", reserva.getEstado(), "Al crearse, el estado inicial debe ser 'Reservado'");
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Consultar detalle completo (getDetalleById)")
    void testGetDetalleById() {
        Reserva detalle = reservaDao.getDetalleById(idReservaCreada);

        assertNotNull(detalle, "El detalle de la reserva no debe ser nulo");
        assertEquals("Concierto Test", detalle.getNombreEvento(), "Debe hacer JOIN correctamente con EVENTO");
        assertEquals("Foro_Res", detalle.getNombreEspacio(), "Debe hacer JOIN correctamente con ESPACIO");
    }

    @Test
    @Order(4)
    @DisplayName("4. READ - Buscar por asistente y filtro (getByAsistenteConFiltro)")
    void testGetByAsistenteConFiltro() {
        List<Reserva> lista = reservaDao.getByAsistenteConFiltro(idAsistenteAux, "Reservado", null);

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "Debe existir al menos la reserva que acabamos de crear");
        assertEquals(idReservaCreada, lista.get(0).getId());
    }

    @Test
    @Order(5)
    @DisplayName("5. UPDATE - Cancelar reserva")
    void testCancelar() {
        boolean cancelado = reservaDao.cancelar(idReservaCreada);
        assertTrue(cancelado, "El método cancelar() debe retornar true");

        Reserva reservaValidacion = reservaDao.getById(idReservaCreada);
        assertEquals("Cancelado", reservaValidacion.getEstado(), "El estado de la reserva debe haber cambiado a 'Cancelado'");
    }

    @Test
    @Order(6)
    @DisplayName("6. DELETE - Limpiar historial del asistente")
    void testLimpiarHistorial() {
        boolean limpiado = reservaDao.limpiarHistorial(idAsistenteAux);
        assertTrue(limpiado, "limpiarHistorial() debe ejecutarse correctamente");

        Reserva eliminada = reservaDao.getById(idReservaCreada);
        assertNull(eliminada, "La reserva cancelada debió ser eliminada por la limpieza de historial");
    }

    @AfterAll
    static void tearDownAfterClass() {
        String delReserva = "DELETE FROM RESERVA WHERE id_reserva = ?";
        String delEvento = "DELETE FROM EVENTO WHERE id_evento = ?";
        String delOrg = "DELETE FROM ORGANIZADOR WHERE id_usuario = ?";
        String delAsis = "DELETE FROM ASISTENTE WHERE id_usuario = ?";
        String delUsu = "DELETE FROM USUARIO WHERE id_usuario IN (?, ?)";
        String delEspacio = "DELETE FROM ESPACIO WHERE id_espacio = ?";
        String delCat = "DELETE FROM CATEGORIA WHERE id_categoria = ?";

        try (Connection con = OracleConnectApp.getConnection()) {
            if (idReservaCreada > 0) try (PreparedStatement ps = con.prepareStatement(delReserva)) { ps.setInt(1, idReservaCreada); ps.executeUpdate(); }
            if (idEventoAux > 0) try (PreparedStatement ps = con.prepareStatement(delEvento)) { ps.setInt(1, idEventoAux); ps.executeUpdate(); }
            if (idAsistenteAux > 0) try (PreparedStatement ps = con.prepareStatement(delAsis)) { ps.setInt(1, idUsuarioAsisAux); ps.executeUpdate(); }
            if (idOrganizadorAux > 0) try (PreparedStatement ps = con.prepareStatement(delOrg)) { ps.setInt(1, idUsuarioOrgAux); ps.executeUpdate(); }
            if (idUsuarioOrgAux > 0 && idUsuarioAsisAux > 0) {
                try (PreparedStatement ps = con.prepareStatement(delUsu)) {
                    ps.setInt(1, idUsuarioOrgAux);
                    ps.setInt(2, idUsuarioAsisAux);
                    ps.executeUpdate();
                }
            }
            if (idEspacioAux > 0) try (PreparedStatement ps = con.prepareStatement(delEspacio)) { ps.setInt(1, idEspacioAux); ps.executeUpdate(); }
            if (idCategoriaAux > 0) try (PreparedStatement ps = con.prepareStatement(delCat)) { ps.setInt(1, idCategoriaAux); ps.executeUpdate(); }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}