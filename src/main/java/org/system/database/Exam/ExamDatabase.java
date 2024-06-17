package org.system.database.Exam;

import org.system.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        Integer subject_id = exam.getSubjectID();
        HashMap<Integer, HashMap<Integer, Integer>> subjectAnswers = exam.getAnswers();
        HashMap<Integer, Integer> subjectGrades = exam.getGrades();
        ArrayList<Integer> idsOfWhoJoined = exam.getIdsOfWhoJoined();
        HashMap<Integer, HashMap<Integer, Integer>> answers = exam.getAnswers();
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
                        byte[] serializedObject = serializeObject(exam);
                        // subject_id exists and subject name is fetched, proceed with the insertion
                        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                            insertStmt.setString(1, subject);
                            insertStmt.setInt(2, subject_id);
                            insertStmt.setString(3, idsOfWhoJoined.toString());
                            insertStmt.setString(4, answers.toString());
                            insertStmt.setString(5, grades.toString());
                            insertStmt.setBytes(6, serializedObject);
                            insertStmt.setInt(7, duration); // Set duration parameter
                            insertStmt.executeUpdate();
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


    public static List<Exam> getExamsBySubjectId(int subjectId) {
        String sql = "SELECT * FROM exam WHERE subject_id = ?";
        List<Exam> exams = new ArrayList<>();

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
        ExamDatabase db = new ExamDatabase();
        ArrayList<Integer> idsOfWhoJoined = new ArrayList<>();
        idsOfWhoJoined.add(1);
        idsOfWhoJoined.add(2);
        idsOfWhoJoined.add(3);
        HashMap<Integer, HashMap<Integer, Integer>> answers = new HashMap<>();
        HashMap<Integer, Integer> ahh = new HashMap<>();
        ahh.put(1, 1);
        ahh.put(2, 1);
        answers.put(1, ahh);
        HashMap<Integer, Integer> grades = new HashMap<>();
        grades.put(1, 1);
        grades.put(2, 2);
        Exam exam = new Exam(1, "math", idsOfWhoJoined, answers, grades, 10);
        Question question = new Question("Whats my name");
        Question question2 = new Question("Whats my name");
        Option option1 = new Option("Adham");
        Option option2 = new Option("Youssef");
        question.addOption(option1);
        question.addOption(option2);
        question2.addOption(option1);
        question2.addOption(option2);
        exam.addQuestion(question);
        exam.addQuestion(question2);
//        insertExam(exam);


        getExamsBySubjectId(1).get(1).printExamDetails();


    }
}
