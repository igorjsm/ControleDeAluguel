package com.github.igorjsm.controledealuguel.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    // Caminho do banco de dados SQLite3
    private static final String URL = "jdbc:sqlite:src/main/resources/database/database.db";

    // Classe de conexão, um erro é levantado caso a conexão falhe
    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // Retorna uma objeto da classe "Connection" capaz de se comunicar com o banco
        // de dados
        return conn;
    }
}
