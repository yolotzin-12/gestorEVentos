package com.example.events.DB;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class OracleConnectApp {
    private static final String JDBC_URL = "jdbc:oracle:thin:@zna2k9jj9m6dqdww_medium";
    private static final String USERNAME = "ADMIN";
    private static final String PASSWORD = "Silvanaescamilla170707@";
    private static final String WALLET_LOCATION;
    static {
        try {
            WALLET_LOCATION = new File( OracleConnectApp.class.getClassLoader().getResource("wallet").toURI() ).getAbsolutePath();
            System.setProperty("oracle.net.tns_admin", WALLET_LOCATION);
        }
        catch (Exception e) { throw new RuntimeException("No se pudo ubicar la carpeta wallet", e);
        }
    }
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties(); props.setProperty("user", USERNAME); props.setProperty("password", PASSWORD);
        return DriverManager.getConnection(JDBC_URL, props);
    }
}