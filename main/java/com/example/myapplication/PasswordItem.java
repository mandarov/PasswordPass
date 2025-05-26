package com.example.myapplication;

public class PasswordItem {
    private int id;
    private String serviceName;
    private String login;
    private String password;

    public PasswordItem(int id, String serviceName, String login, String password) {
        this.id = id;
        this.serviceName = serviceName;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
