package jeda00.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connection {

    private static Connection instance;

    private java.sql.Connection connection;

    private Connection() {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");

            Statement stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static java.sql.Connection get() {
        if (instance == null) {
            instance = new Connection();
        }

        return instance.connection;
    }

    public static void reset() {
        if (instance == null) return;

        try {
            instance.connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        instance = null;
    }

}
