package org.system.database.Exam;

import org.system.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ExamDatabase extends Database {

    public ExamDatabase() {
        createExamTable();
    }

    public void createExamTable() {
        String sql = "CREATE TABLE IF NOT EXISTS exam (" +
                "exam_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "exam_subject VARCHAR(255)," +
                "subject_id INTEGER," +
                "ids_of_who_joined TEXT," +
                "answers TEXT," +
                "grades TEXT," +
                "exam TEXT," +
                "duration INTEGER" + // Add duration field
                ");";

        try {
            openConnection();
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public static void insertExam(Exam exam) {
        String checkSql = "SELECT subject_name FROM Subjects WHERE subject_id = ?";
        String insertSql = "INSERT INTO exam (exam_subject, subject_id, ids_of_who_joined, answers, grades, exam, duration) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateSql = "UPDATE exam SET exam = ? WHERE exam_id = ?";
        Integer subject_id = exam.getSubjectID();
        ArrayList<Integer> idsOfWhoJoined = exam.getIdsOfWhoJoined();
        HashMap<Integer, HashMap<Integer, String>> answers = exam.getAnswers();
        HashMap<Integer, Integer> grades = exam.getGrades();
        int duration = exam.getDuration();

        try {
            openConnection();

            // Check if the subject_id exists in the subject table and fetch the subject name
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, subject_id);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String subject = rs.getString("subject_name");

                        // Insert the exam without the serialized object
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                            insertStmt.setString(1, subject);
                            insertStmt.setInt(2, subject_id);
                            insertStmt.setString(3, idsOfWhoJoined.toString());
                            insertStmt.setString(4, answers.toString());
                            insertStmt.setString(5, grades.toString());
                            insertStmt.setBytes(6, null); // Placeholder for serialized object
                            insertStmt.setInt(7, duration); // Set duration parameter
                            insertStmt.executeUpdate();

                            // Retrieve the generated key
                            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    int examId = generatedKeys.getInt(1);
                                    // Set the generated exam ID in the Exam object
                                    exam.setId(examId);

                                    // Serialize the updated Exam object
                                    byte[] serializedObject = serializeObject(exam);

                                    // Update the database entry with the serialized object
                                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                                        updateStmt.setBytes(1, serializedObject);
                                        updateStmt.setInt(2, examId);
                                        updateStmt.executeUpdate();
                                    }
                                } else {
                                    throw new SQLException("Creating exam failed, no ID obtained.");
                                }
                            }
                        }
                    } else {
                        // subject_id does not exist, handle the error
                        System.out.println("Error: subject_id " + subject_id + " does not exist in the Subjects table.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void updateExam(int examId, Exam exam) {
        String updateSql = "UPDATE exam SET exam_subject = ?, subject_id = ?, ids_of_who_joined = ?, answers = ?, grades = ?, exam = ?, duration = ? WHERE exam_id = ?";
        Integer subject_id = exam.getSubjectID();
        ArrayList<Integer> idsOfWhoJoined = exam.getIdsOfWhoJoined();
        HashMap<Integer, HashMap<Integer, String>> answers = exam.getAnswers();
        HashMap<Integer, Integer> grades = exam.getGrades();
        int duration = exam.getDuration();

        try {
            openConnection();

            // Check if the subject_id exists in the subject table and fetch the subject name
            String checkSql = "SELECT subject_name FROM Subjects WHERE subject_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, subject_id);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        String subject = rs.getString("subject_name");

                        // Serialize the Exam object
                        byte[] serializedObject = serializeObject(exam);

                        // Update the exam in the database
                        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                            updateStmt.setString(1, subject);
                            updateStmt.setInt(2, subject_id);
                            updateStmt.setString(3, idsOfWhoJoined.toString());
                            updateStmt.setString(4, answers.toString());
                            updateStmt.setString(5, grades.toString());
                            updateStmt.setBytes(6, serializedObject);
                            updateStmt.setInt(7, duration);
                            updateStmt.setInt(8, examId);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        // subject_id does not exist, handle the error
                        System.out.println("Error: subject_id " + subject_id + " does not exist in the Subjects table.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void deleteExam(int exam_id) {
        openConnection();
        String sql = "DELETE FROM exam WHERE exam_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, exam_id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exam deleted successfully.");
            } else {
                System.out.println("No account found with the provided email.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }


    public static Exam getExamById(int examId) {
        String sql = "SELECT * FROM exam WHERE exam_id = ?";
        Exam exam = null;

        try {
            openConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, examId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {

                        exam = (Exam) deserializeObject(rs.getBytes("exam"));
                        if (exam != null) {
                            exam.setId(rs.getInt("exam_id"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return exam;
    }


    public static ArrayList<Exam> getExamsBySubjectId(int subjectId) {
        String sql = "SELECT * FROM exam WHERE subject_id = ?";
        ArrayList<Exam> exams = new ArrayList<>();

        try {
            openConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, subjectId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Exam exam = (Exam) deserializeObject(rs.getBytes("exam"));
                        if (exam != null) {
                            // Add to the list
                            exam.setId(rs.getInt("exam_id"));
                            exams.add(exam);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

        return exams;
    }

    public static void main(String[] args) {
        ExamDatabase examDatabase = new ExamDatabase();
        examDatabase.createExamTable();


        Option options1 = new Option("Option A");
        Option options2 = new Option("Option A");
        Option options3 = new Option("Option A");
        Option options4 = new Option("Option A");

        Option options11 = new Option("Option 1");
        Option options22 = new Option("Option 2");
        Option options33 = new Option("Option 3");
        Option options44 = new Option("Option 4");

        Question question1 = new Question("What is 2 + 2?");
        question1.addOption(options1);
        question1.addOption(options2);
        question1.addOption(options3);
        question1.addOption(options4);

        Question question2 = new Question("What is the capital of France?");
        question2.addOption(options11);
        question2.addOption(options22);
        question2.addOption(options33);
        question2.addOption(options44);


        question1.setQuestion_answer(options1);
        question2.setQuestion_answer(options22);

        Exam exam = new Exam();
        exam.addQuestion(question1);
        exam.addQuestion(question2);
        exam.setSubjectID(2);
        exam.setDuration(10);


        // Insert the exam into the database
        ExamDatabase.insertExam(exam);
    }

}
