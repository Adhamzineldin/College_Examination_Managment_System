package org.system.Modules.User;

import org.system.Modules.Admin.Administrative;
import org.system.database.Account.Account;
import org.system.database.Subject.Subject;
import org.system.database.Subject.SubjectDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static org.system.Modules.User.TitleCase.toTitleCase;

public class AdminMenu {

    public static void adminLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
        System.out.println("1) Alter email and password");
        System.out.println("2) Manage students");
        System.out.println("3) Manage lecturers");
        System.out.println("4) Manage subjects");
        System.out.println("5) Exit");
        System.out.println("Input Option: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                alterEmailAndPass(account);
                break;
            case 2:
                manageStudents(account);
                break;
            case 3:
                manageLecturers(account);
                break;
            case 4:

                manageSubjects(account);
            case 5:
                System.out.println("Thank you for using the system");
                System.exit(0);


        }


    }

    private static boolean inputAdminPassword(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();
        if (Objects.equals(password, account.getPassword())) {
            return true;
        }
        return false;
    }

    private static void manageSubjects(Account account) {
        System.out.println("1) Add Subject");
        System.out.println("2) Remove Subject");
        System.out.println("3) Add Student to subject");
        System.out.println("4) Remove Student to subject");
        System.out.println("5) Assign lecturer to subject");
        System.out.println("6) Back");
        System.out.println("Input Option: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                addSubject(account);
                break;
            case 2:
                removeSubject(account);
                break;
            case 3:
                addStudentToSubject(account);
                break;
            case 4:
                removeStudentToSubject(account);
                break;
            case 5:
                assignLecturerToSubject(account);
                break;
            case 6:
                adminLogin(account);
                break;


        }
    }

    private static void assignLecturerToSubject(Account account) {
        ArrayList<Subject> subjects = SubjectDatabase.getAllSubjects();
        for (Subject subject : subjects) {
            subject.printSubject();
        }
        System.out.println("Enter Subject id to assign lecturer to subject: ");
        Scanner sc = new Scanner(System.in);
        int subjectId = sc.nextInt();
        System.out.println("Enter lecturer id to add to subject: ");
        int studentId = sc.nextInt();

        try {
            Administrative.changeSubjectLecturer(subjectId, studentId);
            System.out.println("Subject " + subjectId + " has been assigned to " + studentId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        manageSubjects(account);
    }

    private static void addStudentToSubject(Account account) {
        ArrayList<Subject> subjects = SubjectDatabase.getAllSubjects();
        for (Subject subject : subjects) {
            subject.printSubject();
        }
        System.out.println("Enter Subject id to add student to subject: ");
        Scanner sc = new Scanner(System.in);
        int subjectId = sc.nextInt();
        System.out.println("Enter student id to add to subject: ");
        int studentId = sc.nextInt();

        try {
            Administrative.add_student(subjectId, studentId);
            System.out.println("Student " + studentId + " has been assigned to " + subjectId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        manageSubjects(account);
    }

    private static void removeStudentToSubject(Account account) {
        ArrayList<Subject> subjects = SubjectDatabase.getAllSubjects();
        for (Subject subject : subjects) {
            subject.printSubject();
        }
        System.out.println("Enter Subject id to remove student from subject: ");
        Scanner sc = new Scanner(System.in);
        int subjectId = sc.nextInt();
        System.out.println("Enter student id to remove to subject: ");
        int studentId = sc.nextInt();

        try {
            if (inputAdminPassword(account)) {
                Administrative.remove_student(subjectId, studentId);
                System.out.println("Removed student " + studentId + " from subject " + subjectId);
            } else {
                System.out.println("Wrong password");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        manageSubjects(account);
    }


    private static void addSubject(Account account) {
        try {
            System.out.println("Enter Subject Name: ");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();
            System.out.println("Enter lecturer id: ");
            int lecturerId = sc.nextInt();
            int buff = 1;
            ArrayList<Integer> student_ids = new ArrayList<>();
            while (buff != 0) {
                System.out.println("Enter student id to add to the subject and 0 to stop: ");
                buff = sc.nextInt();
                if (buff != 0 && !student_ids.contains(buff)) {
                    student_ids.add(buff);
                }
            }
            Administrative.createSubject(name, lecturerId, student_ids);
            System.out.println("Subject created successfully: ");
            manageSubjects(account);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeSubject(Account account) {
        try {
            ArrayList<Subject> subjects = SubjectDatabase.getAllSubjects();
            for (Subject subject : subjects) {
                subject.printSubject();
            }
            System.out.println("Enter Subject id to remove from the subject and 0 to stop: ");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();
            if (id == 0) {
                manageSubjects(account);
            }
            System.out.println("Enter subject id again to remove: ");
            int subjectId = sc.nextInt();
            if (id != subjectId) {
                System.out.println("Subject id does not match");
                manageSubjects(account);
            }
            if (inputAdminPassword(account)) {
                Administrative.removeSubject(id);
                System.out.println("Subject removed successfully");
            } else {
                System.out.println("Wrong password");
            }
            manageSubjects(account);
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
                adminLogin(account);
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
                adminLogin(account);
            } else {
                System.out.println("Passwords do not match");
                alterEmailAndPass(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addUser(Account account, String userRole) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.printf("Enter %s Name: ", toTitleCase(userRole));
            String name = sc.nextLine();
            System.out.printf("Enter %s Email: ", toTitleCase(userRole));
            String email = sc.nextLine();
            System.out.printf("Enter %s Password: ", toTitleCase(userRole));
            String password = sc.nextLine();
            int isVerified = 0;
            String status = "activated";
            Account newUser = new Account(name, email, password, userRole, status);
            if (!Objects.equals(name, "") && !Objects.equals(email, "null") && !Objects.equals(password, "null")) {
                Administrative.addUser(newUser);
            } else {
                System.out.println("Please enter a valid input");
            }

            if (userRole.equalsIgnoreCase("student")) {
                manageStudents(account);
            } else {
                manageLecturers(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userRole.equalsIgnoreCase("student")) {
                manageStudents(account);
            } else {
                manageLecturers(account);
            }
        }
    }

    private static void removeUser(Account account, String userRole) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.printf("Enter %s Email you want to remove: ", toTitleCase(userRole));
            String email = sc.nextLine();
            System.out.printf("Confirm %s Email: ", toTitleCase(userRole));
            String emailConfirmation = sc.nextLine();
            if (Objects.equals(emailConfirmation, email)) {

                if (inputAdminPassword(account)) {
                    Administrative.deleteUser(email);
                } else {
                    System.out.println("Wrong Passwords");
                }
                if (userRole.equalsIgnoreCase("student")) {
                    manageStudents(account);
                } else {
                    manageLecturers(account);
                }
            } else {
                System.out.println("emails are not equal try again");
                if (userRole.equalsIgnoreCase("student")) {
                    manageStudents(account);
                } else {
                    manageLecturers(account);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userRole.equalsIgnoreCase("student")) {
                manageStudents(account);
            } else {
                manageLecturers(account);
            }
        }
    }

    private static void editUser(Account account, String userRole) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.printf("Enter %S ID: ", toTitleCase(userRole));
            String id = sc.nextLine();
            Account userAccount = Administrative.getUserById(id, userRole);
            if (userAccount != null) {
                System.out.println("Enter 0 to keep default: ");
                System.out.println("Enter user Name current = " + userAccount.getName() + " :");
                String name = sc.nextLine();
                System.out.println("Enter user email current = " + userAccount.getMail() + " :");
                String email = sc.nextLine();
                System.out.println("Enter user role current = " + userAccount.getUserRole() + " :");
                String role = sc.nextLine();
                if (!Objects.equals(name, "0") && !Objects.equals(name, "")) {
                    userAccount.setName(name);
                }
                if (!Objects.equals(email, "0") && !Objects.equals(email, "")) {
                    userAccount.setMail(email);
                }
                if (!Objects.equals(role, "0") && !Objects.equals(role, "")) {
                    userAccount.setUserRole(role);
                }
                Administrative.updateUser(userAccount);
                System.out.println("Your account has been updated");
                if (userRole.equalsIgnoreCase("student")) {
                    manageStudents(account);
                } else {
                    manageLecturers(account);
                }
            } else {
                System.out.printf("%s does not exist \n", toTitleCase(userRole));
                if (userRole.equalsIgnoreCase("student")) {
                    manageStudents(account);
                } else {
                    manageLecturers(account);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private static void searchUser(Account account, String userRole) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.printf("Enter %s ID: ", toTitleCase(userRole));
            String id = sc.nextLine();
            Account userAccount = Administrative.getUserById(id, userRole);
            if (userAccount != null) {
                userAccount.toString();
                manageStudents(account);
            } else {
                System.out.printf("%s does not exist \n", toTitleCase(userRole));
                if (userRole.equalsIgnoreCase("student")) {
                    manageStudents(account);
                } else {
                    manageLecturers(account);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (userRole.equalsIgnoreCase("student")) {
                manageStudents(account);
            } else {
                manageLecturers(account);
            }
        }


    }

    private static void manageStudents(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1) Add Student");
        System.out.println("2) Remove Student");
        System.out.println("3) Edit Student");
        System.out.println("4) List Students");
        System.out.println("5) Search Student");
        System.out.println("6) BACK");
        System.out.println("Input Option: ");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                addUser(account, "student");
                break;
            case 2:
                removeUser(account, "student");
                break;
            case 3:
                editUser(account, "student");
                break;
            case 4:
                Administrative.getUserByRole("student");
                manageStudents(account);
                break;
            case 5:
                searchUser(account, "student");
            case 6:
                adminLogin(account);
                break;


        }

    }


    private static void manageLecturers(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1) Add lecturer");
        System.out.println("2) Remove lecturer");
        System.out.println("3) Edit lecturer");
        System.out.println("4) List lecturers");
        System.out.println("5) Search lecturer");
        System.out.println("6) BACK");
        System.out.println("Input Option: ");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                addUser(account, "lecturer");
                break;
            case 2:
                removeUser(account, "lecturer");
                break;
            case 3:
                editUser(account, "lecturer");
                break;
            case 4:
                Administrative.getUserByRole("lecturer");
                manageLecturers(account);
                break;
            case 5:
                searchUser(account, "lecturer");
            case 6:
                adminLogin(account);
                break;


        }

    }

}
