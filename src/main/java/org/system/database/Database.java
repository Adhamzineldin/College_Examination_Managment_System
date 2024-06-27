package org.system.database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    protected static Connection connection = null;
    protected static String dbPath;
    protected static String url;

    static {
        // Set the path to the database file
        dbPath = new File("src/main/java/org/system/database/database.db").getAbsolutePath();
        url = "jdbc:sqlite:" + dbPath;

        // Ensure the database file exists, creating it if necessary
        ensureDatabaseFileExists();
    }

    // Method to ensure the database file exists
    private static void ensureDatabaseFileExists() {
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            try {
                if (dbFile.getParentFile().mkdirs()) {
                    System.out.println("Created directory: " + dbFile.getParentFile());
                }
                if (dbFile.createNewFile()) {
                    System.out.println("Created new database file: " + dbPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected static void openConnection() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static byte[] serializeObject(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static Object deserializeObject(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        openConnection();
        // You can add code here to test the connection or perform database operations
        closeConnection();
    }
}
