package org.system.Modules.User;

import org.system.Modules.Admin.Administrative;
import org.system.Modules.Lecturer.Lecturer;
import org.system.Modules.Student.Student;
import org.system.database.Account.Account;
import org.system.database.Account.AccountDatabase;
import org.system.database.Exam.Exam;
import org.system.database.Exam.ExamDatabase;
import org.system.database.Exam.Option;
import org.system.database.Exam.Question;
import org.system.database.Subject.Subject;

import java.util.*;

public class LecturerMenu {
    public static void lecturerLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
        System.out.println("1) Subjects");
        System.out.println("2) Change Email and Password");
        System.out.println("3) logout");
        System.out.println("4) Exit");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                subjects(account);
                break;
            case 2:
                alterEmailAndPass(account);
                break;
            case 3:
                User.start();
                break;
            case 4:
                System.out.println("Thank you for using the system");
                System.exit(0);
                break;

        }


    }

    private static <Stundent> void subjects(Account account) {
        Lecturer lecturer = new Lecturer(account.getName(), account.getId());
        ArrayList<Subject> subjects_array = lecturer.getCourses();
        int i = 1;
        System.out.println("0) BACK");
        for (Subject subject : subjects_array) {
            System.out.println(i + ") " + subject.getSubject_name());
            i++;
        }
        System.out.println("input subject you want");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 0) {
            lecturerLogin(account);
        }
        try {
            subject(account, subjects_array.get(choice - 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void subject(Account account, Subject subject) {
        System.out.println("1) Exams");
        System.out.println("2) Students");
        System.out.println("3) Grades");
        System.out.println("4) Back");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                exams(account, subject);
                break;
            case 2:
                for (Integer id : subject.getStudent_ids()) {
                    String name = AccountDatabase.searchAccounts("id", String.valueOf(id)).getFirst().getName();
                    System.out.println("Name: " + name + " ID:" + id);
                }
                subject(account, subject);
                break;
            case 3:
                studentGrades(account, subject);
            case 4:
                subjects(account);
                break;
        }


    }

    private static void studentGrades(Account account, Subject subject) {
        for (Integer id : subject.getStudent_ids()) {
            Account studentAccount = AccountDatabase.searchAccounts("id", String.valueOf(id)).getFirst();
            Student student = new Student(studentAccount.getName(), studentAccount.getId());
            System.out.println("Name: " + studentAccount.getName() + " ID: " + id + " Grade: " + student.getGradesById().get(subject.getSubject_id()) + "%");
        }
        System.out.println("0) Back");
        System.out.println("1) input student id you want to add a report too");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 0:
                subject(account, subject);
                break;

        }
    }

    private static void exam(Account account, Exam exam, Subject subject) {
        ExamDatabase.updateExam(exam.getId(), exam);
        System.out.println("1) Questions");
        System.out.println("2) Grades");
        System.out.println("3) Students");
        System.out.println("4) duration");
        System.out.println("5) Back");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                questions(account, exam, subject);
                break;
            case 2:
                examGrades(account, exam, subject);
                break;
            case 3:
                joinedExam(account, exam, subject);
                break;
            case 4:
                System.out.println("Exam duration: " + exam.getDuration());
                Scanner sc2 = new Scanner(System.in);
                System.out.println("Enter duration and 0 to keep default ");
                int duration = sc2.nextInt();
                if (duration == 0) {
                    exam(account, exam, subject);
                } else {
                    exam.setDuration(duration);
                    exam(account, exam, subject);
                }

            case 5:
                exams(account, subject);
                break;


        }
    }

    private static void joinedExam(Account account, Exam exam, Subject subject) {
        ArrayList<Integer> students = exam.getIdsOfWhoJoined();
        for (Integer id : students) {
            String name = AccountDatabase.searchAccounts("id", String.valueOf(id)).getFirst().getName();
            System.out.println("Name :" + name + " ID:" + id);
        }
        System.out.println("0) Back");
        System.out.println("1) Allow student to retake exam");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 0:
                exam(account, exam, subject);
                break;
            case 1:
                System.out.println("Input Student ID");
                int id = sc.nextInt();
                students.remove(id);
                exam.setIdsOfWhoJoined(students);
                ExamDatabase.updateExam(exam.getId(), exam);
                joinedExam(account, exam, subject);
                break;

        }
    }

    private static void examGrades(Account account, Exam exam, Subject subject) {
        HashMap<Integer, Integer> grades = exam.getGrades();
        for (Map.Entry<Integer, Integer> grade : grades.entrySet()) {
            String name = AccountDatabase.searchAccounts("id", String.valueOf(grade.getKey())).get(0).getName();
            System.out.println("Name :" + name + " ID:" + grade.getKey() + " Grade: " + grade.getValue());
        }

        System.out.println("0) Back");
        System.out.println("Input Id of grade you want to change");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 0) {
            exam(account, exam, subject);
        } else {
            System.out.println("Input Id of grade you want to change");
            int id = sc.nextInt();
            if (grades.containsKey(id)) {
                System.out.println("Grade Now: " + grades.get(id));
                System.out.println("Input the new grade");
                int newGrade = sc.nextInt();
                grades.put(id, newGrade);
                System.out.println("Grade Updated");
                ExamDatabase.updateExam(exam.getId(), exam);
            } else {
                System.out.println("Grade Not Found");
            }


        }
        examGrades(account, exam, subject);
    }

    private static void questions(Account account, Exam exam, Subject subject) {
        System.out.println("0) Add Question");
        System.out.println("-1) back");
        HashMap<Integer, Question> questionsMap = exam.getQuestions();
        for (Map.Entry<Integer, Question> question : questionsMap.entrySet()) {
            System.out.println(question.getKey() + ") " + question.getValue().getQuestion());
        }
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        if (choice == 0) {
            addQuestion(account, exam, subject);
        } else if (choice == -1) {
            exam(account, exam, subject);
        } else {
            try {
                question(account, questionsMap.get(choice), exam, subject);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


        }
    }

    private static void question(Account account, Question question, Exam exam, Subject subject) throws Exception {
        System.out.println(question.getQuestion());
        System.out.println("1) Manage Options");
        System.out.println("2) Change Question");
        System.out.println("3) Delete Question");
        System.out.println("4) Back");
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                optionsManager(account, question, exam, subject);
                break;
            case 2:
                System.out.println("Input the new question to replace");
                Scanner sc2 = new Scanner(System.in);
                String newQuestion = sc2.nextLine();
                question.setQuestion(newQuestion);
                ExamDatabase.updateExam(exam.getId(), exam);
                question(account, question, exam, subject);
                break;
            case 3:
                System.out.println("Are you sure you want to delete?");
                System.out.println("Enter your choice (1 = Delete, 0 = Back): ");
                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 0:
                        question(account, question, exam, subject);
                        break;
                    case 1:
                        exam.removeQuestion(question);
                        ExamDatabase.updateExam(exam.getId(), exam);
                        System.out.println("Question deleted successfully");
                        questions(account, exam, subject);
                }
            case 4:
                questions(account, exam, subject);
                break;

        }

    }

    private static void addQuestion(Account account, Exam exam, Subject subject) {
        System.out.println("Input the question");
        Scanner sc = new Scanner(System.in);
        String buff = sc.nextLine();
        Question question = new Question(buff);
        optionsManager(account, question, exam, subject);
    }

    private static void optionsManager(Account account, Question question, Exam exam, Subject subject) {
        for (Map.Entry<Integer, String> option : question.getOptions().entrySet()) {
            System.out.println("Option number: " + option.getKey() + ") " + option.getValue());
        }
        System.out.println("1) Add Option");
        System.out.println("2) Delete Option");
        System.out.println("3) Set Option as answer");
        System.out.println("4) Save question");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                System.out.println("Input the new option");
                Scanner sc2 = new Scanner(System.in);
                String optionText = sc2.nextLine();
                Option option = new Option(optionText);
                question.addOption(option);
                ExamDatabase.updateExam(exam.getId(), exam);
                optionsManager(account, question, exam, subject);
                break;
            case 2:
                System.out.println("Input the option number you want to delete ");
                int optionNumber = sc.nextInt();
                question.removeOption(optionNumber + 1);
                ExamDatabase.updateExam(exam.getId(), exam);
                optionsManager(account, question, exam, subject);
                break;
            case 3:
                System.out.println("Input the answer you want to set ");
                String answer = sc.next();
                question.setQuestion_answer(answer);
                ExamDatabase.updateExam(exam.getId(), exam);
                optionsManager(account, question, exam, subject);
                break;
            case 4:
                if (question.getQuestion_number() == 0) {
                    exam.addQuestion(question);
                }
                ExamDatabase.updateExam(exam.getId(), exam);
                questions(account, exam, subject);
                break;

        }
    }


    private static void addExam(Account account, Subject subject) {
        Exam exam = new Exam();
        exam.setSubjectID(subject.getSubject_id());
        ExamDatabase.insertExam(exam);
        exam(account, exam, subject);
    }

    private static void exams(Account account, Subject subject) {
        ArrayList<Exam> exams = ExamDatabase.getExamsBySubjectId(subject.getSubject_id());
        int i = 2;
        System.out.println("0) BACK");
        System.out.println("1) Add Exam");
        for (Exam exam : exams) {
            System.out.println(i + ") Exam's ID: " + exam.getId());
            i++;
        }
        System.out.println("input subject you want");
        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();
        if (choice == 0) {
            subject(account, subject);
        } else if (choice == 1) {
            addExam(account, subject);
        }
        try {
            exam(account, exams.get(choice - 2), subject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void alterEmailAndPass(Account account) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter 0 in mail to exit: ");

            System.out.println("Enter mail: ");
            String mail = sc.nextLine();
            if (Objects.equals(mail, "0")) {
                lecturerLogin(account);
            }
            System.out.println("Enter password: ");
            String password = sc.nextLine();
            System.out.println("Confirm password: ");
            String passwordConfirm = sc.nextLine();

            if (password.equals(passwordConfirm)) {
                account.setMail(mail);
                account.setPassword(password);
                Administrative.updateUser(account);
                System.out.println("Your account has been updated");
                lecturerLogin(account);
            } else {
                System.out.println("Passwords do not match");
                alterEmailAndPass(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
