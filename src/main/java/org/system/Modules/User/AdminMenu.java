package org.system.Modules.User;

import org.system.Modules.Admin.Administrative;
import org.system.database.Account.Account;

import java.util.Scanner;

import static org.system.Modules.User.TitleCase.toTitleCase;

public class AdminMenu {

    public static void adminLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
        System.out.println("1) Alter email and password");
        System.out.println("2) Manage students and lecturers");
        System.out.println("3) Manage subjects");
        System.out.println("4) Exit");
        System.out.println("Input Option: ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                alterEmailAndPass(account);
                break;
            case 2:
                manageStudentsAndLecturers(account);

        }


    }


    private static void alterEmailAndPass(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 0 in mail to exit: ");
        System.out.println("Enter mail: ");
        String mail = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        System.out.println("Confirm password: ");
        String passwordConfirm = sc.nextLine();
        if (mail == "0") {
            adminLogin(account);
        }
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

    }

    private static void addUser(Account account, String userRole) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter student Name: ");
        String name = sc.nextLine();
        System.out.println("Enter student Email: ");
        String email = sc.nextLine();
        System.out.println("Enter student Password: ");
        String password = sc.nextLine();
        int isVerified = 0;
        String status = "activated";
        Account newUser = new Account(name, email, password, userRole, status);
        Administrative.addUser(newUser);
        manageStudents(account);
    }

    private static void removeUser(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter student Email you want to remove: ");
        String email = sc.nextLine();
        System.out.println("Confirm student Email: ");
        String emailConfirmation = sc.nextLine();
        if (emailConfirmation == email) {
            Administrative.deleteUser(email);
            manageStudents(account);
        } else {
            System.out.println("Wrong email try again");
            removeUser(account);
        }
    }

    private static void editUser(Account account, String userRole) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter %S ID: ", toTitleCase(userRole));
        String id = sc.nextLine();
        Account userAccount = Administrative.getUserById(id);
        if (userAccount != null) {
            System.out.println("Enter 0 to keep default: ");
            System.out.println("Enter user Name current = " + userAccount.getName() + " :");
            String name = sc.nextLine();
            System.out.println("Enter user email current = " + userAccount.getMail() + " :");
            String email = sc.nextLine();
            System.out.println("Enter user role current = " + userAccount.getUserRole() + " :");
            String role = sc.nextLine();
            if (name != "0") {
                account.setName(name);
            }
            if (email != "0") {
                account.setMail(email);
            }
            if (role != "0") {
                account.setUserRole(role);
            }
            Administrative.updateUser(account);
        } else {
            System.out.printf("%s does not exist", toTitleCase(userRole));
            manageStudents(account);
        }


    }

    private static void searchUser(Account account, String userRole) {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter %s ID: ", toTitleCase(userRole));
        String id = sc.nextLine();
        Account userAccount = Administrative.getUserById(id);
        if (userAccount != null) {
            userAccount.toString();
        } else {
            System.out.printf("%s does not exist \n", toTitleCase(userRole));
            manageStudents(account);
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
                removeUser(account);
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
                manageStudentsAndLecturers(account);
                break;


        }

    }


    private static void manageLecturers(Account account) {
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
                addUser(account, "lecturer");
                break;
            case 2:
                removeUser(account);
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
                manageStudentsAndLecturers(account);
                break;


        }

    }


    private static void manageStudentsAndLecturers(Account account) {
        Scanner sc = new Scanner(System.in);
        System.out.println("1) Manage Students ");
        System.out.println("2) Manage Lecturers ");
        System.out.println("3) BACK ");
        System.out.println("Input Option: ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1:
                manageStudents(account);
                break;
            case 2:
                manageLecturers(account);
                break;
            case 3:
                adminLogin(account);
                break;

        }
    }
}
