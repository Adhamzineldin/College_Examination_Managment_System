package org.system.Modules.User;

import org.system.Modules.Student.Student;
import org.system.database.Account.Account;
import org.system.database.Exam.Exam;
import org.system.database.Exam.ExamDatabase;
import org.system.database.Exam.Question;
import org.system.database.Subject.Subject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StudentMenu {
    public static void studentLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
        Scanner input = new Scanner(System.in);
        System.out.println("1) Subjects");
        System.out.println("2) Accounts");
        System.out.println("3) Grades");
        System.out.println("4) logout");
        System.out.println("5) Exit");
        System.out.print("Enter your choice: ");
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                subjects(account);
                break;
            case 2:
                break;
            case 3:
                getGrades(account);
                break;
            case 4:
                User.start();
            case 5:
                System.exit(0);
        }

    }

    private static void getGrades(Account account) {
        Student student = new Student(account.getName(), account.getId());
        for (Map.Entry<Subject, Integer> entry : student.getGrades().entrySet()) {
            System.out.println(entry.getKey().getSubject_name() + " : " + entry.getValue() + "%");
        }
    }


    private static void getExamAnswers(Account account, Exam exam) {
        for (Question question : exam.getQuestions().values()) {
            displayQuestion(question);
            System.out.println("Your answer: " + exam.getAnswers().get(account.getId()).get(question.getQuestion_number()));
            System.out.println("Correct answer: " + question.getQuestion_answer());
        }
    }

    private static void enterExam(Account account, Exam exam) {
        LocalDateTime startTime = LocalDateTime.now();
        int examDurationMinutes = exam.getDuration(); // Assuming this method exists

        HashMap<Integer, Question> questions = exam.getQuestions(); // Assuming this method exists
        Scanner scanner = new Scanner(System.in);

        int currentQuestionIndex = 1;
        LocalDateTime endTime = startTime.plusMinutes(examDurationMinutes);

        while (LocalDateTime.now().isBefore(endTime)) {
            if (currentQuestionIndex > questions.size()) {
                break;
            }

            Question currentQuestion = questions.get(currentQuestionIndex);
            displayQuestion(currentQuestion);
            String choice = scanner.nextLine();
            recordAnswer(account, currentQuestion, choice, exam);
            currentQuestionIndex++;
        }

        closeExam(account, exam);
        scanner.close();
    }

    private static void recordAnswer(Account account, Question question, String answer, Exam exam) {
        exam.addAnswer(account, question, answer);
        ExamDatabase.updateExam(exam.getId(), exam);
    }

    private static void displayQuestion(Question question) {
        System.out.println(question.getQuestion_number() + ") " + question.getQuestion());
        HashMap<Integer, String> options = question.getOptions();
        for (Integer key : options.keySet()) {
            System.out.println(key + ". " + options.get(key));
        }
        System.out.print("Enter your choice: ");
    }

    private static void closeExam(Account account, Exam exam) {
        // Implement the logic to close the exam here
        exam.calcGrade(account);
        System.out.println("Exam completed!");
        System.out.println("Grade before review is:  " + exam.getGrades().get(account.getId()) + "%");
        exam.addIdWhoJoined(account.getId());
        ExamDatabase.updateExam(exam.getId(), exam);
    }


    private static void subject(Account account, Subject subject) {
        ArrayList<Exam> exams = ExamDatabase.getExamsBySubjectId(subject.getSubject_id());
        int i = 1;
        for (Exam exam : exams) {

            if (exam.getIdsOfWhoJoined().contains(account.getId())) {
                System.out.println(i + ") Exam Done " + ": " + exam.getGrades().get(account.getId()) + "%");
            } else {
                System.out.println(i + ") Exam Not Done");
            }
        }
        if (exams.isEmpty()) {
            System.out.println("No exams found for this subject");
            subjects(account);
        }
        System.out.println("Input exam you want to access");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        Exam examChoice = exams.get(choice - 1);
        if (examChoice.getIdsOfWhoJoined().contains(account.getId())) {
            getExamAnswers(account, examChoice);
        } else {
            enterExam(account, examChoice);
        }

    }

    private static <Stundent> void subjects(Account account) {
        Student student = new Student(account.getName(), account.getId());
        ArrayList<Subject> subjects_array = student.getCourses();
        int i = 1;
        for (Subject subject : subjects_array) {
            System.out.println(i + ") " + subject.getSubject_name());
            i++;
        }
        System.out.println("input subject you want");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        try {
            subject(account, subjects_array.get(choice - 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
