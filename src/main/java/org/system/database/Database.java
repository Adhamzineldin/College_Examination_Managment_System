package org.system.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    protected static Connection connection = null;
    protected static final String url = "jdbc:sqlite:src/main/java/org/system/database/database.db";

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

    // Method to deserialize a byte array to an object
    protected static Object deserializeObject(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    // Save object to SQLite database with an ID
//    protected static void saveObject(int id, Object obj) {
//        try {
//            openConnection();
//            PreparedStatement pstmt = connection.prepareStatement("INSERT OR REPLACE INTO exam(exam_id, questions) VALUES(?, ?)");
//
//            // Serialize object
//            byte[] serializedObject = serializeObject(obj);
//
//            // Set parameters
//            pstmt.setInt(1, id);
//            pstmt.setBytes(2, serializedObject);
//
//            pstmt.executeUpdate();
//            pstmt.close();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Retrieve object from SQLite database using an ID
//    protected static Object retrieveObject(int id) {
//        try {
//            openConnection();
//            PreparedStatement pstmt = connection.prepareStatement("SELECT questions FROM exam WHERE exam_id = ?");
//            pstmt.setInt(1, id);
//
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                byte[] data = rs.getBytes("questions");
//
//                // Deserialize object
//                Object obj = deserializeObject(data);
//
//                rs.close();
//                pstmt.close();
//                connection.close();
//
//                return obj;
//            }
//
//            rs.close();
//            pstmt.close();
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static void main(String[] args) {
    }

}
