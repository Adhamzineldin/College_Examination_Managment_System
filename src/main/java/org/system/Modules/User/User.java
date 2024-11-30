package org.system.Modules.User;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.system.database.Account.Account;
import org.system.database.Account.AccountDatabase;
import org.system.database.Exam.ExamDatabase;
import org.system.database.Subject.SubjectDatabase;

import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

import static org.system.Modules.User.AdminMenu.adminLogin;
import static org.system.Modules.User.LecturerMenu.lecturerLogin;
import static org.system.Modules.User.StudentMenu.studentLogin;

public class User {
    public static void start() {
        AccountDatabase accountDatabase = new AccountDatabase();
        SubjectDatabase subjectDatabase = new SubjectDatabase();
        ExamDatabase examDatabase = new ExamDatabase();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter user email: ");
        String email = sc.nextLine();
        System.out.println("Enter the password: ");
        String password = sc.nextLine();
        User.login(email, password);
    }

    public static void login(String email, String password) {
        Account account = AccountDatabase.checkLogin(email, password);
        if (account != null) {
            if (account.getStatus().equals("activated")) {
                if (account.getVerificationStatus()) {
                    if (account.getUserRole().equals("admin")) {
                        adminLogin(account);
                    } else if (account.getUserRole().equals("student")) {
                        studentLogin(account);
                    } else if (account.getUserRole().equals("lecturer")) {
                        lecturerLogin(account);
                    }
                } else {
                    verify(account);
                }

            } else {
                System.out.println("Your account is not activated please contact your admin");
                start();
            }

        } else {
            System.out.println("Invalid User");
            start();
        }

    }

    public static void sendEmail(String mail, int code) {
        System.out.println("A code will be sent to your email address: " + mail);

        String recipient = mail;
        String sender = "Adhams.botmail@gmail.com";
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587"); // or your SMTP port
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        // Get the default Session object.
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication("Adhams.botmail@gmail.com", "dxqx flmj ymlx srzx");
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sender));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: header field
            message.setSubject("Verification Code");

            // Set the actual message
            message.setText("Your Verification code is: " + code);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static int generateRandomNumber() {
        return (int) (Math.random() * 900000) + 100000;
    }

    private static void verify(Account account) {
        int code = generateRandomNumber();
        String mail = account.getMail();
        sendEmail(mail, code);
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the verification code: ");
        int input = sc.nextInt();
        if (input == code) {
            AccountDatabase.verifyAccount(account);
            System.out.println("Your account has been verified");
            login(account.getMail(), account.getPassword());
        } else {
            System.out.println("Wrong code try again");
            verify(account);
        }


    }
    

}






















