package org.system.Modules.Admin;

import org.system.database.Account.Account;
import org.system.database.Account.AccountDatabase;
import org.system.database.Subject.Subject;
import org.system.database.Subject.SubjectDatabase;

import java.util.ArrayList;
import java.util.List;

public class Administrative {
    public static void addUser(String name, String mail, String password, String userRole, String status) {
        try {
            Account newAccount = new Account(name, mail, password, userRole, status);
            AccountDatabase.insertAccount(newAccount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUser(String email) {
        AccountDatabase.deleteAccount(email);
    }

    public static void updateUser(String name, String mail, String password, String userRole, String status, int accountId) {
        try {
            // Create an Account object with the updated information
            Account updatedAccount = new Account(name, mail, password, userRole, status);
            updatedAccount.setId(accountId); // Set the ID for the updated account

            // Use the AccountDatabase to update the account
            AccountDatabase.updateAccount(updatedAccount);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void listUsers() {
        List<Account> accounts = AccountDatabase.getAllNonAdminAccounts();
        System.out.println("\n----------------USERS----------------");
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public static void getUserByEmail(String email) {
        List<Account> accounts = AccountDatabase.searchAccounts("mail", email);
        System.out.printf("\n----------------email = %s----------------\n", email);
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public static void getUserByRole(String role) {
        List<Account> accounts = AccountDatabase.searchAccounts("userRole", role);
        System.out.printf("\n----------------role = %s----------------\n", role);
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public static void getUserById(String id) {
        List<Account> accounts = AccountDatabase.searchAccounts("id", id);
        System.out.printf("\n----------------id = %s----------------\n", id);
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public static void getUserByName(String name) {
        List<Account> accounts = AccountDatabase.searchAccounts("name", name);
        System.out.printf("\n----------------name = %s----------------\n", name);
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
    }

    public static void createSubject(String subject_name, int lecturer_id, ArrayList<Integer> student_ids) {
        Subject subject = new Subject(subject_name, lecturer_id, student_ids);
        SubjectDatabase.addSubject(subject);
    }

    public static void changeSubjectLecturer(int subject_id, int lecturer_id) throws IllegalArgumentException {
        Subject subject = SubjectDatabase.getSubjectById(subject_id);
        if (subject != null) {
            subject.setLecturer_id(lecturer_id);
            SubjectDatabase.updateSubject(subject);
        } else {
            throw new IllegalArgumentException("The subject id does not exist");
        }

    }

    public static void add_student(int subject_id, int student_id) {
        Subject subject = SubjectDatabase.getSubjectById(subject_id);
        if (subject != null) {
            subject.addStudent_id(student_id);
            SubjectDatabase.updateSubject(subject);
        } else {
            throw new IllegalArgumentException("The subject id does not exist");
        }
    }

    public static void remove_student(int subject_id, int student_id) {
        Subject subject = SubjectDatabase.getSubjectById(subject_id);
        if (subject != null) {
            subject.removeStudent_id(student_id);
            SubjectDatabase.updateSubject(subject);
        } else {
            throw new IllegalArgumentException("The subject id does not exist");
        }
    }

    public static void change_subject_name(int subject_id, String new_name) {
        Subject subject = SubjectDatabase.getSubjectById(subject_id);
        if (subject != null) {
            subject.setSubject_name(new_name);
            SubjectDatabase.updateSubject(subject);
        } else {
            throw new IllegalArgumentException("The subject id does not exist");
        }
    }

    public static void change_student_ids(int subject_id, ArrayList<Integer> ids) {
        Subject subject = SubjectDatabase.getSubjectById(subject_id);
        if (subject != null) {
            subject.setStudent_ids(ids);
            SubjectDatabase.updateSubject(subject);
        } else {
            throw new IllegalArgumentException("The subject id does not exist");
        }
    }


}
