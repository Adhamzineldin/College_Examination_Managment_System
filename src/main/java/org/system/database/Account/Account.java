package org.system.database.Account;

public class Account {
    private int id;
    private String name;
    private String mail;
    private String password;
    private String userRole;
    private String status;
    private int isVerfified;


    public Account(String name, String mail, String password, String userRole, String status) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.userRole = userRole;
        this.status = status;
    }

    public Account(int id, String name, String mail, String password, String userRole, String status, int isVerfified) {
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

    public int getVerificationStatus() {
        return isVerfified;
    }

    public void setName(String name) {
        this.name = name;
        AccountDatabase.updateAccount(this);
    }

    public void setMail(String mail) {
        this.mail = mail;
        AccountDatabase.updateAccount(this);
    }

    public void setPassword(String password) {
        this.password = password;
        AccountDatabase.updateAccount(this);
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
        AccountDatabase.updateAccount(this);

    }

    public void setStatus(String status) {
        this.status = status;
        AccountDatabase.updateAccount(this);
    }

    public void setisVerfified(int isVerfified) {
        this.isVerfified = isVerfified;
        AccountDatabase.verifyAccount(this);
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        String verificationStatus;
        if (isVerfified == 1) {
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
                '}';
    }
}
