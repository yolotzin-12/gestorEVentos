package com.example.events;
import com.example.events.DB.OracleConnectApp;
import java.sql.Connection;

public class testconnection {
    public static void main(String[] args) {
        try (Connection conn = OracleConnectApp.getConnection()) {
            System.out.println("CONEXION EXITOSA: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("ERROR AL CONECTAR:");
            e.printStackTrace();
        }
    }
}