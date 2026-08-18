package com.example.events.DB;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class OracleConnectApp {

    // Las variables ahora no tienen valor por defecto, se llenarán dinámicamente
    private static String JDBC_URL;
    private static String USERNAME;
    private static String PASSWORD;
    private static String WALLET_LOCATION;

    static {
        try {
            // 1. Cargar el archivo credentials.properties
            InputStream input = OracleConnectApp.class.getClassLoader().getResourceAsStream("credentials.properties");
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo credentials.properties en la carpeta resources");
            }

            Properties config = new Properties();
            config.load(input);

            // 2. Asignar los valores leídos a las variables
            JDBC_URL = config.getProperty("db.url");
            USERNAME = config.getProperty("db.user");
            PASSWORD = config.getProperty("db.password");

            // 3. Detectar el Sistema Operativo para la ruta de la Wallet
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // Ruta dinámica para probar localmente en Windows
                WALLET_LOCATION = new File(OracleConnectApp.class.getClassLoader().getResource("wallet").toURI()).getAbsolutePath();
            } else {
                WALLET_LOCATION = "/opt/oracle/wallet";
            }
            System.setProperty("oracle.net.tns_admin", WALLET_LOCATION);

        } catch (Exception e) {
            throw new RuntimeException("Error fatal: No se pudo configurar la conexión a la base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Forzar a Tomcat a cargar el driver de Oracle
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de Oracle. Revisa Maven.", e);
        }

        Properties props = new Properties();
        props.setProperty("user", USERNAME);
        props.setProperty("password", PASSWORD);

        return DriverManager.getConnection(JDBC_URL, props);
    }
}