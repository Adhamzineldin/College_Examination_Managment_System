package org.system.Modules.User;

import org.system.database.Account.Account;

public class StudentMenu {
    public static void studentLogin(Account account) {
        System.out.println("Welcome back " + account.getName() + " " + account.getUserRole());
    }
}
