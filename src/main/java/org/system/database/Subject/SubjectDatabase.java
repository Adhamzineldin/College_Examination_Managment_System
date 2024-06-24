package org.system.database.Subject;

import org.system.database.Account.AccountDatabase;
import org.system.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class SubjectDatabase extends Database {

    public SubjectDatabase() {
        openConnection();
        try {
            // Create table if not exists
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Subjects (" +
                    "subject_id INTEGER PRIMARY KEY," +
                    "subject_name TEXT UNIQUE NOT NULL ," +
                    "lecturer_id INTEGER NOT NULL ," +
                    "student_ids TEXT" +
                    ")");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void addSubject(Subject subject) throws IllegalArgumentException {
        boolean error = false;
        try {
            // Open the database connection
            openConnection();

            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Subjects(subject_name, lecturer_id, student_ids) VALUES (?, ?, ?)");
            if (AccountDatabase.isIdWithRoleNotPresent(subject.getLecturer_id(), "lecturer")) {
                throw new SQLException("Lecturer id " + subject.getLecturer_id() + " does not exist");
            } else {
                for (int i = 0; i < subject.getStudent_ids().size(); i++) {
                    if (AccountDatabase.isIdWithRoleNotPresent(subject.getStudent_ids().get(i), "student")) {
                        throw new IllegalArgumentException("Student id " + subject.getStudent_ids().get(i) + " does not exist");
                    }
                }
            }

            preparedStatement.setString(1, subject.getSubject_name());
            preparedStatement.setInt(2, subject.getLecturer_id());
            // Convert ArrayList of integers to comma-separated string
            String studentIdsString = String.join(",", subject.getStudent_ids().stream().map(String::valueOf).toArray(String[]::new));
            preparedStatement.setString(3, studentIdsString);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            // Close the database connection
            closeConnection();
        }
    }


    public static Subject getSubjectById(int subjectId) {
        String query = "SELECT * FROM Subjects WHERE subject_id = ?";
        Subject subject = null;
        try {
            openConnection();

            if (connection == null) {
                throw new SQLException("Failed to establish a database connection.");
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, subjectId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if subject exists
                    if (resultSet.next()) {
                        int subject_id = resultSet.getInt("subject_id");
                        String subject_name = resultSet.getString("subject_name");
                        int lecturer_id = resultSet.getInt("lecturer_id");
                        // Retrieve student IDs from comma-separated string
                        String studentIdsString = resultSet.getString("student_ids");
                        String[] studentIdsArray = studentIdsString.split(",");
                        ArrayList<Integer> student_ids = new ArrayList<>();
                        for (String id : studentIdsArray) {
                            if (!Objects.equals(id, "")) {
                                student_ids.add(Integer.parseInt(id));
                            }
                        }
                        subject = new Subject(subject_id, subject_name, lecturer_id, student_ids);
                    }
                }
            } finally {
                closeConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subject;
    }


    public static void deleteSubject(int subjectId) {
        try {
            openConnection();
            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM subjects WHERE subject_id = ?");
            preparedStatement.setInt(1, subjectId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void updateSubject(Subject subject) {
        try {
            // Check if the subject exists
            if (getSubjectById(subject.getSubject_id()) == null) {
                throw new IllegalArgumentException("Subject does not exist");
            }

            // Check if the new subject name is already present and belongs to another subject
            if (!isUniqueSubjectName(subject.getSubject_name(), subject.getSubject_id())) {
                throw new IllegalArgumentException("Subject name must be unique");
            }

            openConnection();
            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE subjects SET subject_name = ?, lecturer_id = ?, student_ids = ? WHERE subject_id = ?");
            preparedStatement.setString(1, subject.getSubject_name());
            preparedStatement.setInt(2, subject.getLecturer_id());
            // Convert ArrayList of integers to comma-separated string
            String studentIdsString = String.join(",", subject.getStudent_ids().stream().map(String::valueOf).toArray(String[]::new));
            preparedStatement.setString(3, studentIdsString);
            preparedStatement.setInt(4, subject.getSubject_id());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private static boolean isUniqueSubjectName(String subjectName, int subjectId) throws SQLException {
        openConnection();
        boolean result = false;

        // Prepare statement to check if the subject name is unique
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM subjects WHERE subject_name = ? AND subject_id != ?");
        preparedStatement.setString(1, subjectName);
        preparedStatement.setInt(2, subjectId);
        ResultSet resultSet = preparedStatement.executeQuery();

        // Check if any other subject with the same name exists (excluding the current subject)
        if (resultSet.next()) {
            result = resultSet.getInt(1) == 0;
        }
        closeConnection();

        return result;
    }

    public static ArrayList<Subject> getAllSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();
        try {
            // Open the database connection
            openConnection();

            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM subjects");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                int subject_id = resultSet.getInt("subject_id");
                String subject_name = resultSet.getString("subject_name");
                int lecturer_id = resultSet.getInt("lecturer_id");
                // Retrieve student IDs from comma-separated string
                String studentIdsString = resultSet.getString("student_ids");
                String[] studentIdsArray = studentIdsString.split(",");
                ArrayList<Integer> student_ids = new ArrayList<>();
                for (String id : studentIdsArray) {
                    if (!Objects.equals(id, "")) {
                        student_ids.add(Integer.parseInt(id));
                    }
                }
                // Create a new Subject object and add it to the list
                Subject subject = new Subject(subject_id, subject_name, lecturer_id, student_ids);
                subjects.add(subject);
            }

            // Close the prepared statement and the result set
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            closeConnection();
        }
        return subjects;
    }

    public static ArrayList<Subject> getAllStudentSubjects(int studentId) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try {
            // Open the database connection
            openConnection();

            // Prepare statement
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM subjects");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                int subject_id = resultSet.getInt("subject_id");
                String subject_name = resultSet.getString("subject_name");
                int lecturer_id = resultSet.getInt("lecturer_id");
                // Retrieve student IDs from comma-separated string
                String studentIdsString = resultSet.getString("student_ids");
                String[] studentIdsArray = studentIdsString.split(",");
                ArrayList<Integer> student_ids = new ArrayList<>();
                for (String id : studentIdsArray) {
                    if (!Objects.equals(id, "")) {
                        student_ids.add(Integer.parseInt(id));
                    }
                }
                // Check if the student ID is in the list of student IDs
                if (student_ids.contains(studentId)) {
                    // Create a new Subject object and add it to the list
                    Subject subject = new Subject(subject_id, subject_name, lecturer_id, student_ids);
                    subjects.add(subject);
                }
            }

            // Close the prepared statement and the result set
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            closeConnection();
        }
        return subjects;
    }

    public static ArrayList<Subject> getAllLecturerSubjects(int lecturerId) {
        ArrayList<Subject> subjects = new ArrayList<>();
        try {
            // Open the database connection
            openConnection();

            // Prepare statement with query to filter by lecturer_id
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM subjects WHERE lecturer_id = ?");
            preparedStatement.setInt(1, lecturerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                int subjectId = resultSet.getInt("subject_id");
                String subjectName = resultSet.getString("subject_name");
                int lecturerIdFromDB = resultSet.getInt("lecturer_id");

                // Retrieve student IDs from comma-separated string
                String studentIdsString = resultSet.getString("student_ids");
                String[] studentIdsArray = studentIdsString.split(",");
                ArrayList<Integer> studentIds = new ArrayList<>();
                for (String id : studentIdsArray) {
                    if (!Objects.equals(id, "")) {
                        studentIds.add(Integer.parseInt(id));
                    }
                }

                // Create a new Subject object and add it to the list
                Subject subject = new Subject(subjectId, subjectName, lecturerIdFromDB, studentIds);
                subjects.add(subject);
            }

            // Close the prepared statement and the result set
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the database connection
            closeConnection();
        }
        return subjects;
    }


}
