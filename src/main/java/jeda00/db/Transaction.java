package jeda00.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction {

    public interface Callback {

        void call() throws Exception;

    }

    private Connection connection;

    public Transaction(Connection connection) {
        this.connection = connection;
    }

    public boolean run(Callback callback) {
        Statement stmt;

        try {
            stmt = connection.createStatement();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        try {
            stmt.execute("BEGIN TRANSACTION");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }

        try {
            callback.call();
        } catch (Exception e1) {
            System.err.println(e1.getMessage());
            try {
                stmt.execute("ROLLBACK TRANSACTION");
                return false;
            } catch (SQLException e2) {
                System.err.println(e2.getMessage());
                return false;
            }
        }

        try {
            stmt.execute("COMMIT TRANSACTION");
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public static boolean run(Connection connection, Callback callback) {
        return new Transaction(connection).run(callback);
    }

}
