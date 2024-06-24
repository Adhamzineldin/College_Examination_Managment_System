package org.system.database.Account;

import org.system.database.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.system.Modules.User.TitleCase.toTitleCase;

public class AccountDatabase extends Database {

    public AccountDatabase() {
        createTable();
        if (!isEmailUsed("Mohalya3@gmail.com")) {
            Account account = new Account("Adham Zineldin", "Mohalya3@gmail.com", "A251m2006", "admin", "activated");
            insertAccount(account);
        }

    }

    private void createTable() {
        try {
            // Load the SQLite JDBC driver class
            Class.forName("org.sqlite.JDBC");

            // Establish a connection to the database
            connection = DriverManager.getConnection(url);

            // Create a table if it doesn't exist
            String sql = "CREATE TABLE IF NOT EXISTS Accounts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name TEXT NOT NULL,"
                    + "mail TEXT NOT NULL UNIQUE,"
                    + "password TEXT NOT NULL,"
                    + "userRole TEXT NOT NULL,"
                    + "isVerified BOOLEAN NOT NULL DEFAULT false,"
                    + "status TEXT NOT NULL)";

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertAccount(Account account) throws IllegalArgumentException {
        if (account == null) {
            throw new IllegalArgumentException("Account object cannot be null.");
        }

        String name = toTitleCase(account.getName());
        String mail = toTitleCase(account.getMail());
        String password = account.getPassword();
        String userRole = account.getUserRole();
        String status = account.getStatus();

        // Validate userRole
        if (!userRole.equalsIgnoreCase("admin") && !userRole.equalsIgnoreCase("student") && !userRole.equalsIgnoreCase("lecturer")) {
            throw new IllegalArgumentException("Invalid userRole. Must be 'admin', 'student', or 'lecturer'.");
        }

        // Validate status
        if (!status.equalsIgnoreCase("activated") && !status.equalsIgnoreCase("deactivated")) {
            throw new IllegalArgumentException("Invalid status. Must be 'activated' or 'deactivated'.");
        }

        openConnection();

        if (isEmailUsed(mail)) {
            throw new IllegalArgumentException("The email address is already in use.");
        }

        String sql = "INSERT INTO Accounts(name, mail, password, userRole, status) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, mail);
            pstmt.setString(3, password);
            pstmt.setString(4, userRole);
            pstmt.setString(5, status);
            pstmt.executeUpdate();
            System.out.println("Account inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void deleteAccount(String mail) {
        openConnection();
        mail = toTitleCase(mail);
        String sql = "DELETE FROM Accounts WHERE mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Account deleted successfully.");
            } else {
                System.out.println("No account found with the provided email.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public static void updateAccount(Account updatedAccount) {
        if (updatedAccount == null) {
            throw new IllegalArgumentException("Updated account object cannot be null.");
        }

        openConnection();

        try {
            // Check if the account exists
            if (!isAccountIdExists(updatedAccount.getId())) {
                throw new IllegalArgumentException("Account with ID " + updatedAccount.getId() + " does not exist.");
            }

            // Check if the new email address is already in use
            if (!isEmailUsed(updatedAccount.getMail())) {
                throw new IllegalArgumentException("The new email address is already in use.");
            }

            // Prepare statement
            String sql = "UPDATE Accounts SET name = ?, mail = ?, password = ?, userRole = ?, status = ? WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, updatedAccount.getName());
                pstmt.setString(2, updatedAccount.getMail());
                pstmt.setString(3, updatedAccount.getPassword());
                pstmt.setString(4, updatedAccount.getUserRole());
                pstmt.setString(5, updatedAccount.getStatus());
                pstmt.setInt(6, updatedAccount.getId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows <= 0) {
                    System.out.println("No account found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private static boolean isEmailUsed(String mail) {
        mail = toTitleCase(mail);
        String sql = "SELECT COUNT(*) FROM Accounts WHERE mail = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static boolean isAccountIdExists(int accountId) {
        String sql = "SELECT COUNT(*) FROM Accounts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isIdWithRoleNotPresent(int id, String userRole) {
        openConnection();
        String sql = "SELECT COUNT(*) FROM Accounts WHERE id = ? AND userRole = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, userRole);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) <= 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
        return true;
    }

    public static Account checkLogin(String mail, String password) {
        openConnection();
        mail = toTitleCase(mail);
        ;
        String sql = "SELECT * FROM Accounts WHERE mail = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mail);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"), rs.getString("name"), rs.getString("mail"),
                            rs.getString("password"), rs.getString("userRole"),
                            rs.getString("status"), rs.getBoolean("isVerified"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
        return null; // Return null if login fails
    }

    public static void verifyAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account object cannot be null.");
        }

        openConnection();

        try {
            // Check if the account exists
            if (!isAccountIdExists(account.getId())) {
                throw new IllegalArgumentException("Account with ID " + account.getId() + " does not exist.");
            }

            // Prepare statement
            String sql = "UPDATE Accounts SET isVerified = 1 WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, account.getId());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Account verified successfully.");
                } else {
                    System.out.println("No account found with the provided ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public static List<Account> searchAccounts(String searchField, String searchValue) {
        openConnection();
        List<Account> accounts = new ArrayList<>();
        searchValue = searchValue.toLowerCase(Locale.ROOT);
        String sql = "SELECT * FROM Accounts WHERE " + searchField + " = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, searchValue);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String mail = rs.getString("mail");
                    String password = rs.getString("password");
                    String userRole = rs.getString("userRole");
                    String status = rs.getString("status");
                    boolean verificationStatus = rs.getBoolean("isVerified");

                    Account account = new Account(id, name, mail, password, userRole, status, verificationStatus);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        return accounts;
    }

    public static List<Account> getAllNonAdminAccounts() {
        openConnection();
        List<Account> accounts = new ArrayList<>();

        String sql = "SELECT * FROM Accounts WHERE userRole != 'admin'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mail = rs.getString("mail");
                String password = rs.getString("password");
                String userRole = rs.getString("userRole");
                String status = rs.getString("status");
                boolean verificationStatus = rs.getBoolean("isVerified");

                Account account = new Account(id, name, mail, password, userRole, status, verificationStatus);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }

        return accounts;
    }


}
