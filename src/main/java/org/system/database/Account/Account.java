package org.system.database.Account;

import static org.system.Modules.User.TitleCase.toTitleCase;

public class Account {
    private int id;
    private String name;
    private String mail;
    private String password;
    private String userRole;
    private String status;
    private boolean isVerfified;


    public Account(int id, String name, String mail, String userRole, String status, boolean isVerfified) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.userRole = userRole;
        this.status = status;
        this.isVerfified = isVerfified;
    }


    public Account(String name, String mail, String password, String userRole, String status) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
    }

    public Account(int id, String name, String mail, String password, String userRole, String status, boolean isVerfified) {
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
        this.isVerfified = isVerfified;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getStatus() {
        return status;
    }

    public boolean getVerificationStatus() {
        if (isVerfified) {
            return true;
        }
        return false;
    }

    public void setName(String name) {
        this.name = toTitleCase(name);
    }

    public void setMail(String mail) {
        this.mail = toTitleCase(mail);
        this.isVerfified = false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserRole(String userRole) {
        this.userRole = toTitleCase(userRole);

    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void verify() {
        this.isVerfified = true;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        String verificationStatus;
        if (isVerfified) {
            verificationStatus = "Verified";
        } else {
            verificationStatus = "Not Verified";
        }
        return "Account{" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", mail = '" + mail + '\'' +
                ", password = '" + password + '\'' +
                ", userRole = '" + userRole + '\'' +
                ", status = '" + status + '\'' +
                ", verificationStatus = " + verificationStatus +
                "}\n";
    }
}
