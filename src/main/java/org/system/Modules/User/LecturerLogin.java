package org.system.Modules.User;

import org.system.database.Account.Account;

public class LecturerLogin {
    public static void lecturerLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
    }
}
