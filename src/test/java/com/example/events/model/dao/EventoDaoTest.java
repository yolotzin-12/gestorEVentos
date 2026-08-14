package com.example.events.model.dao;

import com.example.events.DB.OracleConnectApp;
import com.example.events.model.models.Evento;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas Unitarias para EventoDao")
public class EventoDaoTest {

    private static EventoDao eventoDao;

    // IDs de las dependencias requeridas para cumplir las Foreign Keys
    private static int idCategoriaAux;
    private static int idEspacioAux;
    private static int idUsuarioAux;
    private static int idOrganizadorAux;

    // ID del Evento que será evaluado a lo largo del CRUD
    private static int idEventoCreado;

    @BeforeAll
    static void setUpBeforeClass() {
        eventoDao = new EventoDao();

        String sqlCat = "INSERT INTO CATEGORIA (nombre) VALUES ('Cat_Test_" + System.currentTimeMillis() + "')";
        String sqlEspacio = "INSERT INTO ESPACIO (nombre_espacio, ubicacion) VALUES ('Espacio_Test', 'Edificio Central')";
        String sqlUsuario = "INSERT INTO USUARIO (id_rol, nombre, apellido_paterno, correo_electronico, activo) " +
                "VALUES (2, 'OrgTest', 'Perez', 'org_" + System.currentTimeMillis() + "@events.com', 1)";
        String sqlOrg = "INSERT INTO ORGANIZADOR (id_usuario, organizacion) VALUES (?, 'Comité Test')";

        try (Connection con = OracleConnectApp.getConnection()) {
            // 1. Crear Categoría Auxiliar
            try (PreparedStatement ps = con.prepareStatement(sqlCat, new String[]{"ID_CATEGORIA"})) {
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idCategoriaAux = rs.getInt(1);
            }

            // 2. Crear Espacio Auxiliar
            try (PreparedStatement ps = con.prepareStatement(sqlEspacio, new String[]{"ID_ESPACIO"})) {
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idEspacioAux = rs.getInt(1);
            }

            // 3. Crear Usuario Auxiliar (Rol Organizador)
            try (PreparedStatement ps = con.prepareStatement(sqlUsuario, new String[]{"ID_USUARIO"})) {
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idUsuarioAux = rs.getInt(1);
            }

            // 4. Crear Organizador vinculado al Usuario
            try (PreparedStatement ps = con.prepareStatement(sqlOrg, new String[]{"ID_ORGANIZADOR"})) {
                ps.setInt(1, idUsuarioAux);
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) idOrganizadorAux = rs.getInt(1);
                else idOrganizadorAux = idUsuarioAux; // Fallback común si la PK no es autogenerada en ORGANIZADOR
            }
        } catch (SQLException e) {
            fail("Falló la preparación de dependencias en BD: " + e.getMessage());
        }
    }

    @Test
    @Order(1)
    @DisplayName("1. CREATE - Crear nuevo Evento")
    void testCreate() {
        Evento evento = new Evento();
        evento.setIdOrganizador(idOrganizadorAux);
        evento.setIdEspacio(idEspacioAux);
        evento.setIdCategoria(idCategoriaAux);
        evento.setNombre("Evento Tecnológico de Prueba");
        evento.setDescripcion("Prueba unitaria completa");
        evento.setCapacidadMaxima(100);
        evento.setFechaHora("2026-12-31 15:30:00");
        // IMPORTANTE: Lo definimos "Disponible" para que getAll() lo pueda encontrar en las siguientes pruebas
        evento.setEstado("Disponible");
        evento.setImagenUrl("img/test.jpg");

        boolean creado = eventoDao.create(evento);

        assertTrue(creado, "create() debe retornar true al guardar el evento correctamente");
        assertTrue(evento.getId() > 0, "El ID del evento generado debe asignarse al objeto (ser mayor a 0)");

        idEventoCreado = evento.getId();
    }

    @Test
    @Order(2)
    @DisplayName("2. READ - Consultar evento por ID (getById)")
    void testGetById() {
        Evento evento = eventoDao.getById(idEventoCreado);

        assertNotNull(evento, "El evento recién insertado debe existir");
        assertEquals(idEventoCreado, evento.getId());
        assertEquals("Evento Tecnológico de Prueba", evento.getNombre());
        assertEquals(100, evento.getCapacidadDisponible(), "Al iniciar, la capacidad disponible debe ser igual a la máxima");
    }

    @Test
    @Order(3)
    @DisplayName("3. READ - Listar todos los eventos (getAll)")
    void testGetAll() {
        List<Evento> lista = eventoDao.getAll();

        assertNotNull(lista, "La lista general no debe ser nula");

        // Verificamos que nuestro evento esté presente
        boolean existe = lista.stream().anyMatch(e -> e.getId() == idEventoCreado);
        assertTrue(existe, "El evento creado debe estar presente en getAll(), ya que su estado es 'Disponible'");
    }

    @Test
    @Order(4)
    @DisplayName("4. READ - Obtener eventos por Organizador (getByOrganizador)")
    void testGetByOrganizador() {
        List<Evento> listaOrg = eventoDao.getByOrganizador(idOrganizadorAux);

        assertNotNull(listaOrg);
        assertFalse(listaOrg.isEmpty(), "Debe existir al menos el evento asociado a este organizador de prueba");
        assertEquals("Evento Tecnológico de Prueba", listaOrg.get(0).getNombre());
    }

    @Test
    @Order(5)
    @DisplayName("5. READ - Obtener eventos con reservas (getByOrganizadorConReservas)")
    void testGetByOrganizadorConReservas() {
        List<Evento> lista = eventoDao.getByOrganizadorConReservas(idOrganizadorAux);

        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        // Al ser nuevo, verificamos el conteo de la subconsulta
        assertEquals(0, lista.get(0).getTotalReservas(), "Un evento recién creado debe registrar 0 reservas");
    }

    @Test
    @Order(6)
    @DisplayName("6. UPDATE - Actualizar datos del Evento")
    void testUpdate() {
        Evento eventoAEditar = eventoDao.getById(idEventoCreado);
        eventoAEditar.setNombre("Nombre de Evento Modificado");
        eventoAEditar.setCapacidadMaxima(150);
        // Según tu código update, se vuelve a enviar la fechaHora en formato YYYY-MM-DD HH24:MI:SS
        eventoAEditar.setFechaHora("2026-12-31 16:00:00");

        boolean actualizado = eventoDao.update(eventoAEditar);
        assertTrue(actualizado, "update() debe retornar true al modificar el registro");

        Evento eventoVerificado = eventoDao.getById(idEventoCreado);
        assertEquals("Nombre de Evento Modificado", eventoVerificado.getNombre());
        // Tu DAO resetea la capacidad_disponible = capacidad_maxima en el update
        assertEquals(150, eventoVerificado.getCapacidadDisponible());
    }

    @Test
    @Order(7)
    @DisplayName("7. UPDATE - Gestión de Disponibilidad (decrementar/incrementar)")
    void testDisponibilidad() {
        try (Connection con = OracleConnectApp.getConnection()) {

            // Prueba de Decrementar (Simulando una reserva)
            boolean decrementado = eventoDao.decrementarDisponibilidad(idEventoCreado, con);
            assertTrue(decrementado, "Debe retornar true al restar 1 a la capacidad");

            Evento evDec = eventoDao.getById(idEventoCreado);
            assertEquals(149, evDec.getCapacidadDisponible(), "La capacidad debe haber bajado de 150 a 149");

            // Prueba de Incrementar (Simulando cancelación de reserva)
            boolean incrementado = eventoDao.incrementarDisponibilidad(idEventoCreado, con);
            assertTrue(incrementado, "Debe retornar true al sumar 1 a la capacidad");

            Evento evInc = eventoDao.getById(idEventoCreado);
            assertEquals(150, evInc.getCapacidadDisponible(), "La capacidad debe volver a 150");

        } catch (SQLException e) {
            fail("Error al probar la gestión de disponibilidad: " + e.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("8. DELETE - Eliminar el Evento")
    void testDelete() {
        boolean eliminado = eventoDao.delete(idEventoCreado);
        assertTrue(eliminado, "delete() debe retornar true al borrar el evento");

        Evento evEliminado = eventoDao.getById(idEventoCreado);
        assertNull(evEliminado, "Al consultar el evento eliminado debe retornar null");
    }

    @AfterAll
    static void tearDownAfterClass() {
        // La limpieza debe realizarse respetando el orden de las dependencias foráneas
        String delEvento = "DELETE FROM EVENTO WHERE id_evento = ?";
        String delOrg = "DELETE FROM ORGANIZADOR WHERE id_usuario = ?";
        String delUsu = "DELETE FROM USUARIO WHERE id_usuario = ?";
        String delEspacio = "DELETE FROM ESPACIO WHERE id_espacio = ?";
        String delCat = "DELETE FROM CATEGORIA WHERE id_categoria = ?";

        try (Connection con = OracleConnectApp.getConnection()) {
            if (idEventoCreado > 0) {
                try (PreparedStatement ps = con.prepareStatement(delEvento)) {
                    ps.setInt(1, idEventoCreado);
                    ps.executeUpdate();
                }
            }
            if (idUsuarioAux > 0) {
                try (PreparedStatement ps = con.prepareStatement(delOrg)) {
                    ps.setInt(1, idUsuarioAux);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(delUsu)) {
                    ps.setInt(1, idUsuarioAux);
                    ps.executeUpdate();
                }
            }
            if (idEspacioAux > 0) {
                try (PreparedStatement ps = con.prepareStatement(delEspacio)) {
                    ps.setInt(1, idEspacioAux);
                    ps.executeUpdate();
                }
            }
            if (idCategoriaAux > 0) {
                try (PreparedStatement ps = con.prepareStatement(delCat)) {
                    ps.setInt(1, idCategoriaAux);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}